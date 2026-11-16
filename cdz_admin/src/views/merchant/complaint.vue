<template>
  <div class="nf-page complaint-page">
    <div class="nf-page-hero">
      <span class="nf-page-hero__badge">投诉管理</span>
      <h2 class="nf-page-hero__title">投诉管理</h2>
      <p class="nf-page-hero__desc">查看与管理店铺投诉，支持按店铺名称、状态筛选，可查询、重置与处理投诉。</p>
      <div class="nf-page-hero__bar"></div>
    </div>

    <div class="nf-filter-card">
      <div class="nf-filter-card__head">
        <i class="el-icon-search nf-filter-card__head-icon"></i>
        <span>筛选条件</span>
      </div>
      <el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
        <el-form-item label="店铺名称">
          <el-input
            class="complaint-page__filter-input"
            @keydown.enter.native="select"
            clearable
            placeholder="请输入店铺名称"
            v-model="storeName">
          </el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-select
            class="complaint-page__filter-select"
            v-model="status"
            placeholder="请选择店铺类型"
            @change="selectTrigger(status)">
            <el-option v-for="item in statusnum" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item class="complaint-page__filter-actions">
          <el-button type="primary" icon="el-icon-search" @click="select">查询</el-button>
          <el-button type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-tabs v-model="activeName" class="complaint-page__tabs">
      <el-tab-pane label="投诉列表" name="first">
        <div class="nf-table-card">
          <el-table v-loading="tableDataLoading" :data="tableData.data">
            <el-table-column fixed prop="id" label="id" width="80">
            </el-table-column>
            <el-table-column prop="store_name" label="店铺名称" width="120">
            </el-table-column>
            <el-table-column prop="nick_name" label="用户昵称">
              <template slot-scope="scope">
                <span>{{scope.row.nick_name ? scope.row.nick_name : '无'}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="phone" label="手机号">
            </el-table-column>
            <el-table-column prop="img" label="投诉图片">
              <template slot-scope="scope">
                <div v-if="scope.row.img == null || scope.row.img == ''">
                  暂无图片
                </div>
                <div v-else-if="scope.row.img.includes(',')" class="complaint-page__img-list">
                  <div v-for="item in scope.row.img.split(',')" :key="item" class="complaint-page__img-item">
                    <el-popover placement="top-start" title="" trigger="hover">
                      <img class="complaint-page__img-thumb" :src="item" alt="" slot="reference">
                      <img class="complaint-page__img-preview" :src="item" alt="">
                    </el-popover>
                  </div>
                </div>
                <div v-else>
                  <el-popover placement="top-start" title="" trigger="hover">
                    <img class="complaint-page__img-thumb" :src="scope.row.img" alt="" slot="reference">
                    <img class="complaint-page__img-preview" :src="scope.row.img" alt="">
                  </el-popover>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="content" label="投诉内容">
              <template slot-scope="scope">
                <span>{{scope.row.content}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="refund_reason" label="驳回原因">
              <template slot-scope="scope">
                <span>{{scope.row.refund_reason ? scope.row.refund_reason : '未驳回'}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态">
              <template slot-scope="scope">
                <span class="complaint-page__status--pending" v-if="scope.row.status == 1">待处理</span>
                <span v-if="scope.row.status == 2">投诉驳回</span>
                <span v-if="scope.row.status == 3">商家处理中</span>
                <span v-if="scope.row.status == 4">商家已处理</span>
                <span v-if="scope.row.status == 5">总后台已处理</span>
              </template>
            </el-table-column>
            <el-table-column prop="create_time" label="创建时间" width="160">
            </el-table-column>
            <el-table-column label="操作" width="100" fixed="right">
              <template slot-scope="scope">
                <el-button size="mini" type="primary" :disabled="!isAuth('complaint:dispose')"
                  v-if="scope.row.status == 1 || scope.row.status == 4" @click="merchLook(scope.row)">处理
                </el-button>
                <el-button v-else size="mini" type="primary" disabled>处理
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <div class="complaint-page__pagination">
            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
              :page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
              layout="total,sizes, prev, pager, next" :total="tableData.total">
            </el-pagination>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>

    <!-- 处理投诉 -->
    <el-dialog title="处理投诉" :visible.sync="dialogFormVisible" center>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">状态：</span>
        <el-radio-group v-model="status" @change="rejectChange">
          <el-radio :label="2">投诉驳回</el-radio>
          <el-radio :label="3">商家去处理</el-radio>
        </el-radio-group>
      </div>
      <div style="margin-bottom: 10px;" v-if="isshow">
        <span style="width: 200px;display: inline-block;text-align: right;position: relative;top: -65px;">驳回原因：</span>
        <el-input style="width:50%;" type="textarea" :rows="4" v-model="refundReason" placeholder="请输入驳回原因"></el-input>
      </div>
      <div style="margin-bottom: 10px;" v-if="!isshow">
        <span style="width: 200px;display: inline-block;text-align: right;">商家处理：</span>
        <span>发送给商家处理</span>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="releasNoticeTo()">确 定</el-button>
      </div>
    </el-dialog>
    <!-- 处理投诉 -->
    <el-dialog title="处理投诉" :visible.sync="dialogFormVisible1" center>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">状态：</span>
        <el-radio-group v-model="status">
          <el-radio :label="3">继续处理</el-radio>
          <el-radio :label="5">审核通过</el-radio>
        </el-radio-group>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible1 = false">取 消</el-button>
        <el-button type="primary" @click="releasNoticeTo2()">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        size: 10,
        page: 1,
        id: '',
        storeName: '',
        isshow: false,
        status: 0,
        refundReason: '',
        statusnum: [{
            value: 0,
            label: '全部'
          },
          {
            value: 1,
            label: '待处理'
          }, {
            value: 2,
            label: '投诉驳回'
          }, {
            value: 3,
            label: '商家处理中'
          }, {
            value: 4,
            label: '商家已处理'
          },
          {
            value: 5,
            label: '总后台已处理'
          }
        ],
        formLabelWidth: '200px',
        tableDataLoading: true,
        dialogFormVisible: false,
        dialogFormVisible1: false,
        tableData: [],
        activeName: 'first',
      }
    },
    methods: {
      // 驳回
      rejectChange(val) {
        if (val == 2) {
          this.isshow = true
        } else {
          this.isshow = false
        }
      },
      handleSizeChange(val) {
        this.size = val
        this.dataSelect()
      },
      handleCurrentChange(val) {
        this.page = val
        this.dataSelect()
      },
      // select自动选择
      selectTrigger() {
        this.dataSelect()
      },
      // 查询
      select() {
        this.dataSelect()
      },
      // 重置
      cleans() {
        this.status = 0
        this.storeName = ''
        this.dataSelect()
      },
      // 处理
      merchLook(row) {
        if (row.status == 1) {
          this.status = 2
          this.isshow = true
          this.dialogFormVisible = true
          this.id = row.id
        } else {
          this.status = 3
          this.dialogFormVisible1 = true
          this.id = row.id
        }
      },
      // 处理
      releasNoticeTo() {
        if (this.status == 2) {
          if (this.refundReason == '') {
            this.$message({
              message: '请输入驳回原因',
              type: 'error',
              duration: 1500
            })
            return
          }
          this.$http({
            url: this.$http.adornUrl2('/admin/storecomplain/admin_complain'),
            method: 'get',
            params: this.$http.adornParams({
              'complain_id': this.id,
              'content': this.refundReason,
              'status': this.status
            })
          }).then(({
            data
          }) => {
            if (data.code == 0) {
              this.$message({
                message: '处理成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.dialogFormVisible = false
                  this.status = 0
                  this.dataSelect()
                }
              })
            }
          })
        } else {
          this.$http({
            url: this.$http.adornUrl2('/admin/storecomplain/admin_complain'),
            method: 'get',
            params: this.$http.adornParams({
              'complain_id': this.id,
              'content': this.refundReason || '',
              'status': this.status
            })
          }).then(({
            data
          }) => {
            if (data.code == 0) {
              this.$message({
                message: '处理成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.dialogFormVisible = false
                  this.status = 0
                  this.dataSelect()
                }
              })
            }
          })
        }

      },
      // 处理
      releasNoticeTo2() {
        this.$http({
          url: this.$http.adornUrl2('/admin/storecomplain/admin_complain'),
          method: 'get',
          params: this.$http.adornParams({
            'complain_id': this.id,
            'content': this.refundReason || '',
            'status': this.status
          })
        }).then(({
          data
        }) => {
          if (data.code == 0) {
            this.$message({
              message: '处理成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.dialogFormVisible1 = false
                this.status = 0
                this.dataSelect()
              }
            })
          }
        })
      },
      // 获取数据列表
      dataSelect() {
        this.tableDataLoading = true
        this.$http({
          url: this.$http.adornUrl2('/admin/storecomplain/store_complain_list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.page,
            'num': this.size,
            'search_name': this.storeName,
            'search_status': this.status
          })
        }).then(({
          data
        }) => {
          this.tableDataLoading = false
          let returnData = data.data
          this.tableData = returnData
        })
      }
    },
    mounted() {
      this.dataSelect()
    }
  }
</script>

<style scoped>
.complaint-page__filter-input {
  width: 160px;
}

.complaint-page__filter-select {
  width: 150px;
}

.complaint-page__filter-actions .el-button + .el-button {
  margin-left: 12px;
}

.complaint-page__tabs {
  margin-bottom: 0;
}

.complaint-page__tabs >>> .el-tabs__header {
  margin-bottom: 0;
}

.complaint-page__tabs .nf-table-card {
  margin-bottom: 0;
}

.complaint-page__tabs >>> .el-tabs__item.is-active {
  color: #5cc477;
}

.complaint-page__tabs >>> .el-tabs__active-bar {
  background-color: #5cc477;
}

.complaint-page__status--pending {
  color: #5cc477;
}

.complaint-page__img-list {
  display: flex;
  flex-wrap: wrap;
}

.complaint-page__img-item {
  margin: 2px;
}

.complaint-page__img-thumb {
  width: 50px;
  height: 50px;
}

.complaint-page__img-preview {
  width: 200px;
  height: 200px;
}

.complaint-page__pagination {
  display: flex;
  justify-content: center;
  padding-top: 12px;
}

.complaint-page .el-button + .el-button {
  margin-left: 12px;
}

.complaint-page .nf-table-card .el-button + .el-button {
  margin-left: 0;
  margin-top: 5px;
}
</style>
