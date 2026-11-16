<template>
  <div class="nf-page merchant-enter-page">
    <div class="nf-page-hero">
      <span class="nf-page-hero__badge">商家管理</span>
      <h2 class="nf-page-hero__title">入驻申请</h2>
      <p class="nf-page-hero__desc">查看商家入驻申请列表，支持按负责人手机号、姓名筛选查询与重置，可审核处理入驻申请。</p>
      <div class="nf-page-hero__bar"></div>
    </div>

    <div class="nf-filter-card">
      <div class="nf-filter-card__head">
        <i class="el-icon-search nf-filter-card__head-icon"></i>
        <span>筛选条件</span>
      </div>
      <el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
        <!-- <el-form-item label="状态">
          <el-select v-model="storeType" placeholder="请选择入状态" class="merchant-enter-page__filter-select" @change="select()">
            <el-option v-for="item in storeTypes" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item> -->
        <el-form-item label="负责人手机号">
          <el-input
            class="merchant-enter-page__filter-input"
            @keydown.enter.native="select"
            clearable
            placeholder="请输入负责人手机号"
            v-model="legalPhone">
          </el-input>
        </el-form-item>
        <el-form-item label="负责人姓名">
          <el-input
            class="merchant-enter-page__filter-input"
            @keydown.enter.native="select"
            clearable
            placeholder="负责人姓名筛选"
            v-model="storeName">
          </el-input>
        </el-form-item>
        <el-form-item class="merchant-enter-page__filter-actions">
          <el-button type="primary" icon="el-icon-search" @click="select">查询</el-button>
          <el-button type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-tabs v-model="activeName" @tab-click="handleClick" class="merchant-enter-page__tabs">
      <el-tab-pane label="入驻列表" name="first">
        <div class="nf-table-card">
          <el-table v-loading="tableDataLoading" :data="tableData.list">
            <el-table-column prop="applyId" label="编号" width="80">
            </el-table-column>
            <!-- <el-table-column prop="shopName" label="商家名称" width="120">
            </el-table-column> -->
            <!-- <el-table-column label="证件类型" width="120">
              <template slot-scope="scope">
                <span v-if="scope.row.cardType == 1">个人</span>
                <span v-if="scope.row.cardType == 2">个体工商户</span>
                <span v-if="scope.row.cardType == 3">企业</span>
                <span v-if="scope.row.cardType == 4">其他组织</span>
              </template>
            </el-table-column> -->
            <!-- <el-table-column prop="cardNo" label="申请人证件号" width="120">
            </el-table-column> -->
            <el-table-column prop="applyName" label="负责人姓名">
            </el-table-column>
            <el-table-column prop="applyPhone" label="负责人手机号">
            </el-table-column>
            <el-table-column prop="shopAddressDetails" label="店铺地址">
              <!-- <template slot-scope="scope">
                <span>{{scope.row.shopAddress}}{{scope.row.shopAddress}}</span>
              </template> -->
            </el-table-column>
            <!-- <el-table-column prop="shopAddressDetails" label="店铺详细地址" width="150">
            </el-table-column>
            <el-table-column prop="cardFrontImg" label="身份证正面照" width="100">
              <template slot-scope="scope">
                <div v-if="scope.row.cardFrontImg">
                  <span v-for="(item,index) in scope.row.cardFrontImg.split(',')">
                    <el-popover placement="top-start" title="" trigger="hover">
                      <img style="width: 50px; height: 50px;margin: 3px;" :src="item" alt="" slot="reference">
                      <img style="width: 300px; height: 300px" :src="item" alt="">
                    </el-popover>
                  </span>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="cardReverseImg" label="身份证背面照" width="100">
              <template slot-scope="scope">
                <div v-if="scope.row.cardReverseImg">
                  <span v-for="(item,index) in scope.row.cardReverseImg.split(',')">
                    <el-popover placement="top-start" title="" trigger="hover">
                      <img style="width: 50px; height: 50px;margin: 3px;" :src="item" alt="" slot="reference">
                      <img style="width: 300px; height: 300px" :src="item" alt="">
                    </el-popover>
                  </span>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="shopImg" label="商家图片" width="200">
              <template slot-scope="scope">
                <div v-if="scope.row.shopImg">
                  <span v-for="(item,index) in scope.row.shopImg.split(',')">
                    <el-popover placement="top-start" title="" trigger="hover">
                      <img style="width: 50px; height: 50px;margin: 3px;" :src="item" alt="" slot="reference">
                      <img style="width: 300px; height: 300px" :src="item" alt="">
                    </el-popover>
                  </span>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="businessLicenseImg" label="营业执照" width="200">
              <template slot-scope="scope">
                <div v-if="scope.row.businessLicenseImg">
                  <span v-for="(item,index) in scope.row.businessLicenseImg.split(',')">
                    <el-popover placement="top-start" title="" trigger="hover">
                      <img style="width: 50px; height: 50px;margin: 3px;" :src="item" alt="" slot="reference">
                      <img style="width: 300px; height: 300px" :src="item" alt="">
                    </el-popover>
                  </span>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="auditContent" label="审核内容" width="100">
            </el-table-column> -->
            <el-table-column prop="createTime" label="创建时间" width="160">
            </el-table-column>
            <!-- <el-table-column label="状态" width="80">
              <template slot-scope="scope">
                <span v-if="scope.row.status == 1">待审核</span>
                <span v-if="scope.row.status == 2">通过</span>
                <span v-if="scope.row.status == 3">拒绝</span>
              </template>
            </el-table-column> -->
            <!-- <el-table-column label="操作" width="120">
              <template slot-scope="scope">
                <el-button size="mini" type="primary" v-if="scope.row.status == 1"
                  @click="dispose(scope.row)">处理
                </el-button>
                <el-button size="mini" type="primary" :disabled="!isAuth('merchantEnter:update')"
                  @click="couponUpdates(scope.row)">修改
                </el-button>
              </template>
            </el-table-column> -->
          </el-table>
          <div class="merchant-enter-page__pagination">
            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
              :page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
              layout="total,sizes, prev, pager, next" :total="tableData.totalCount">
            </el-pagination>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>

    <!-- 处理弹框-->
    <el-dialog title="处理审核" :visible.sync="dialogFormVisible" center>
      <div style="margin-bottom: 10px;display: flex;">
        <span
          style="width: 200px;display: inline-block;text-align: right;position: relative;top: -5px;">入驻处理：</span>
        <el-radio-group v-model="status" @change="statusChange">
          <el-radio :label="1">同意入驻</el-radio>
          <el-radio :label="2">拒绝入驻</el-radio>
        </el-radio-group>
      </div>
      <!-- <div style="margin-bottom: 10px;" v-if="isshow">
        <span style="width: 200px;display: inline-block;text-align: right;">生效日期：</span>
        <el-date-picker
          style="width:215px;"
          v-model="info.stockDate"
          type="date"
          placeholder="生效日期">
        </el-date-picker>
      </div>
      <div style="margin-bottom: 10px;" v-if="isshow">
        <span style="width: 200px;display: inline-block;text-align: right;">生效年限：</span>
        <el-input style="width:50%;" v-model="auditYears" placeholder="请输入生效年限"></el-input>&nbsp;&nbsp;&nbsp;&nbsp;年
      </div> -->
      <div style="margin-bottom: 10px;" v-if="status==2">
        <span
          style="width: 200px;display: inline-block;text-align: right;position:relative;top: -70px;">拒绝理由：</span>
        <el-input style="width:50%;" type="textarea" :rows="4" v-model="refundReason"
          placeholder="请输入拒绝理由"></el-input>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="disposeNoticeTo()">确 定</el-button>
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
        storeName: '',
        legalPhone: '',
        activeName: 'first',
        storeType: '',
        storeTypes: [{
            value: '',
            label: '全部'
          },
          {
            value: 1,
            label: '审核中'
          }, {
            value: 2,
            label: '通过'
          }, {
            value: 3,
            label: '拒绝'
          }
        ],
        status: 1,
        info: {
          stockDate: this.getNowTime(), //日期
        },
        id: '',
        createTimeStart: '',
        createTimeEnd: '',
        refundReason: '',
        auditTime: '',
        auditYears: '',
        isshow: true,
        tableDataLoading: true,
        dialogFormVisible: false,
        tableData: [],
        applyId:'',
      }
    },
    methods: {
      formatDate(row) {
        let date = new Date(row['create_time'] * 1000);
        let Y = date.getFullYear() + '-';
        let M = date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) + '-' : date.getMonth() + 1 + '-';
        let D = date.getDate() < 10 ? '0' + date.getDate() + ' ' : date.getDate() + ' ';
        let h = date.getHours() < 10 ? '0' + date.getHours() + ':' : date.getHours() + ':';
        let m = date.getMinutes() < 10 ? '0' + date.getMinutes() + ':' : date.getMinutes() + ':';
        let s = date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds();
        return Y + M + D + h + m + s;
      },
      //处理默认选中当前日期
      getNowTime() {
        var now = new Date();
        var year = now.getFullYear(); //得到年份
        var month = now.getMonth(); //得到月份
        var date = now.getDate(); //得到日期
        month = month + 1;
        month = month.toString().padStart(2, "0");
        date = date.toString().padStart(2, "0");
        var defaultDate = `${year}-${month}-${date}`;
        return defaultDate;
        this.$set(this.info, "stockDate", defaultDate);
      },
      // 团长跳转详情页
      handleSizeChange(val) {
        this.size = val
        this.dataSelect()
      },
      handleCurrentChange(val) {
        this.page = val
        this.dataSelect()
      },
      handleClick(tab, event) {
        if (tab._props.label == '入驻列表') {

          this.size = 10
          this.page = 1
          this.dataSelect()
        }
        // if (tab._props.label == '待处理') {
        //  this.status = 1
        //  this.size = 10
        //  this.page = 1
        //  this.dataSelect()
        // }
        // if (tab._props.label == '已通过') {
        //  this.size = 10
        //  this.page = 1
        //  this.status = 2
        //  this.dataSelect()
        // }
        // if (tab._props.label == '已拒绝') {
        //  this.size = 10
        //  this.page = 1
        //  this.status = 3
        //  this.dataSelect()
        // }
      },
      // select自动选择
      selectTrigger() {
        this.dataSelect()
      },
      // 查询
      select() {
        this.page = 1
        this.dataSelect()
      },
      // 重置
      cleans() {
        this.storeName = ''
        this.legalPhone = ''
        this.storeType = ''
        this.page = 1
        this.dataSelect()
      },
      // 是否入驻
      statusChange(val) {
        if (val == 2) {
          this.isshow = true
          this.refundReason = ''
        } else {
          this.isshow = false
          this.refundReason = '同意'
        }
      },
      // // 处理跳转
      // dispose(row) {
      //   this.$router.push({
      //     path: '/merchDetails',
      //     query: {
      //       id: row.id
      //     }
      //   })
      // },
      //修改跳转
      couponUpdates(row) {
        this.$router.push({
          path: '/merchDetails',
          query: {
            id: row.id
          }
        })
      },
      // 获取数据列表
      dataSelect() {
        this.tableDataLoading = true
        this.$http({
          url: this.$http.adornUrl('apply/selectApplyList'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.page,
            'limit': this.size,
            'applyName': this.storeName,
            'applyPhone': this.legalPhone,
            'status': this.storeType,
          })
        }).then(({
          data
        }) => {
          this.tableDataLoading = false
          let returnData = data.data
          this.tableData = returnData
        })
      },
      // 处理弹框
      dispose(row) {
        this.applyId = row.applyId
        this.isshow = true
        this.refundReason = '同意'
        this.dialogFormVisible = true
      },
      disposeNoticeTo() {
        if (this.refundReason == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入拒绝理由',
            type: 'warning'
          });
          return
        }
        let id = this.$route.query.id
        this.$http({
          url: this.$http.adornUrl('apply/auditApply'),
          method: 'post',
          params: this.$http.adornParams({
            'ids': this.applyId,
            'status': this.status,
            'content': this.refundReason,
          })
        }).then(({
          data
        }) => {
          if (data.code == 0) {
            this.$message({
              message: '提交成功',
              type: 'success',
              duration: 1500,
            })
            this.dataSelect()
            this.dialogFormVisible = false
            this.status = 2
            this.refundReason = ''
            // this.$router.replace({
            //   name: 'merchantEnter'
            // })
          }
        })
      },
    },
    activated() {
      this.dataSelect()
    }
  }
</script>

<style scoped>
.merchant-enter-page__filter-input {
  width: 160px;
}

.merchant-enter-page__filter-select {
  width: 150px;
}

.merchant-enter-page__filter-actions .el-button + .el-button {
  margin-left: 12px;
}

.merchant-enter-page__tabs {
  margin-bottom: 0;
}

.merchant-enter-page__tabs >>> .el-tabs__header {
  margin-bottom: 0;
}

.merchant-enter-page__tabs .nf-table-card {
  margin-top: 12px;
  margin-bottom: 0;
}

.merchant-enter-page__tabs >>> .el-tabs__item.is-active {
  color: #5cc477;
}

.merchant-enter-page__tabs >>> .el-tabs__active-bar {
  background-color: #5cc477;
}

.merchant-enter-page__pagination {
  display: flex;
  justify-content: center;
  padding-top: 12px;
}

.merchant-enter-page .nf-table-card .el-button + .el-button {
  margin-left: 0;
  margin-top: 5px;
}
</style>
