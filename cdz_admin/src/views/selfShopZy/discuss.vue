<template>
  <div class="nf-page discuss-zy-page">
    <a class="discuss-zy-page__back" href="#" @click.prevent="prev">
      <icon-svg name="jiantou" class="discuss-zy-page__back-icon"></icon-svg>
      返回
    </a>

    <div class="nf-page-hero">
      <span class="nf-page-hero__badge">自营商城</span>
      <h2 class="nf-page-hero__title">评价列表</h2>
      <p class="nf-page-hero__desc">查看与管理商品用户评价，支持按评价状态筛选，可刷新、回复与删除评论。</p>
      <div class="nf-page-hero__bar"></div>
    </div>

    <div class="nf-filter-card">
      <div class="nf-filter-card__head">
        <i class="el-icon-search nf-filter-card__head-icon"></i>
        <span>筛选条件</span>
      </div>
      <el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
        <el-form-item label="评价状态">
          <el-select
            class="discuss-zy-page__filter-select"
            v-model="scoreType"
            placeholder="请选择评价状态"
            @change="select(scoreType)">
            <el-option v-for="item in scoreTypes" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item class="discuss-zy-page__filter-actions">
          <el-button type="primary" icon="el-icon-refresh" @click="refresh">刷新</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-tabs value="first" class="discuss-zy-page__tabs">
      <el-tab-pane label="评论列表" name="first">
        <div class="nf-table-card">
          <el-table v-loading="tableDataLoading" :data="tableData.content">
            <el-table-column prop="commentId" label="编号" width="80">
            </el-table-column>
            <el-table-column prop="userName" label="昵称">
              <template slot-scope="scope">
                <span class="discuss-zy-page__link"
                  @click="updates(scope.row)">{{scope.row.userName ? scope.row.userName : '未绑定'}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="userHeader" label="头像">
              <template slot-scope="scope">
                <img :src="scope.row.userHeader" alt="" width="60" height="60">
              </template>
            </el-table-column>
            <el-table-column prop="content" label="评论内容" width="180">
            </el-table-column>
            <el-table-column label="评论图片" width="150">
              <template slot-scope="scope">
                <div v-if="scope.row.img == null || scope.row.img == ''">
                  暂无图片
                </div>
                <div v-else-if="scope.row.img.includes(',')" style="display:flex;flex-wrap: wrap;">
                  <div v-for="(item, index) in scope.row.img.split(',')" :key="index" style="margin: 2px;">
                    <el-popover placement="top-start" title="" trigger="hover">
                      <img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
                      <img style="width: 200px; height: 200px" :src="item" alt="">
                    </el-popover>
                  </div>
                </div>
                <div v-else>
                  <el-popover placement="top-start" title="" trigger="hover">
                    <img style="width: 50px; height: 50px" :src="scope.row.img" alt="" slot="reference">
                    <img style="width: 200px; height: 200px" :src="scope.row.img" alt="">
                  </el-popover>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="score" label="评分">
              <template slot-scope="scope">
                <span>{{scope.row.score?scope.row.score:'暂无评分'}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="score" label="评价状态">
              <template slot-scope="scope">
                <span v-if="scope.row.scoreType==1">好评</span>
                <span v-if="scope.row.scoreType==2">中评</span>
                <span v-if="scope.row.scoreType==3">差评</span>
              </template>
            </el-table-column>
            <el-table-column prop="sku" label="商品规格" width="180">
              <template slot-scope="scope">
                <span v-if="scope.row.sku==''">无</span>
                <span v-else>{{scope.row.sku}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="reply" label="回复内容" width="180">
              <template slot-scope="scope">
                <span>{{scope.row.reply?scope.row.reply:'未回复'}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间" width="180">
            </el-table-column>
            <el-table-column fixed="right" label="操作" width="150">
              <template slot-scope="scope">
                <el-button size="mini" type="danger" :disabled="!isAuth('discuss:delete')" @click="deletes(scope.row)">删除
                </el-button>
                <el-button size="mini" type="primary" :disabled="!isAuth('discuss:huifu')" @click="replyClick(scope.row)">
                  回复
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <div class="discuss-zy-page__pagination">
            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
              :page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
              layout="total,sizes, prev, pager, next" :total="tableData.totalElements">
            </el-pagination>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>

    <!-- 回复 -->
    <el-dialog title="回复" :visible.sync="dialogFormVisible" center>
      <el-form :model="form">
        <el-form-item label="回复内容：" :label-width="formLabelWidth">
          <el-input v-model="form.reply" type="textarea" :rows="4" style="width:65%;" placeholder="请输入回复内容">
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="replyNoticeTo()">确 定</el-button>
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
        goodsId: '',
        form: {
          commentId: '',
          reply: ''
        },
        scoreType: 0,
        scoreTypes: [{
            value: 0,
            label: '全部'
          },
          {
            value: 1,
            label: '好评'
          },
          {
            value: 2,
            label: '中评'
          },
          {
            value: 3,
            label: '差评'
          }
        ],
        formLabelWidth: '200px',
        tableDataLoading: false,
        dialogFormVisible: false,
        tableData: [],
      }
    },
    methods: {
      // 返回上一级
      prev() {
        this.$router.back()
      },
      handleSizeChange(val) {
        this.size = val;
        this.dataSelect()
      },
      handleCurrentChange(val) {
        this.page = val;
        this.dataSelect()
      },
      // 刷新
      refresh() {
        this.dataSelect()
      },
      //搜索
      select() {
        this.dataSelect()
      },
      // 回复
      replyClick(row) {
        this.dialogFormVisible = true
        this.form.commentId = row.commentId
        this.form.reply = row.reply
      },
      replyNoticeTo() {
        this.$http({
          url: this.$http.adornUrl('selfGoodsComment/reply'),
          method: 'get',
          params: this.$http.adornParams({
            'commentId': this.form.commentId,
            'reply': this.form.reply,
          })
        }).then(({
          data
        }) => {
          this.$message({
            message: '回复成功',
            type: 'success',
            duration: 1500,
            onClose: () => {
              this.dataSelect()
            }
          })
          this.dialogFormVisible = false
        })
      },
      // 删除评论
      deletes(row) {
        this.$confirm(`确定删除此条信息?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl(`selfGoodsComment/delete?id=${row.commentId}`),
            method: 'get',
            params: this.$http.adornParams({})
          }).then(({
            data
          }) => {
            if (data.status == 0) {
              this.$message({
                message: '删除成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.dataSelect()
                }
              })
            }
          })
        }).catch(() => {})
      },
      // 获取地址数据
      dataSelect() {
        let goodsId = this.$route.query.goodsId
        let page = this.page - 1
        this.tableDataLoading = true
        this.$http({
          url: this.$http.adornUrl('selfGoodsComment/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': page,
            'size': this.size,
            'goodsId': goodsId,
            'scoreType': this.scoreType
          })
        }).then(({
          data
        }) => {
          this.tableDataLoading = false
          let returnData = data.data;
          this.tableData = returnData
        })
      },
      // 详情跳转
      updates(row) {
        this.$router.push({
          path: '/userDetail',
          query: {
            userId: row.userId
          }
        })
      },
    },
    activated() {
      this.dataSelect()
    }
  };
</script>

<style scoped>
.discuss-zy-page__back {
  display: inline-flex;
  align-items: center;
  margin-bottom: 12px;
  font-size: 14px;
  color: #606266;
  text-decoration: none;
}

.discuss-zy-page__back:hover {
  color: #5cc477;
}

.discuss-zy-page__back-icon {
  width: 1.2em;
  height: 1.2em;
  margin-right: 4px;
}

.discuss-zy-page__filter-select {
  width: 150px;
}

.discuss-zy-page__filter-actions .el-button + .el-button {
  margin-left: 12px;
}

.discuss-zy-page__tabs {
  margin-bottom: 0;
}

.discuss-zy-page__tabs >>> .el-tabs__header {
  margin-bottom: 0;
}

.discuss-zy-page__tabs .nf-table-card {
  margin-bottom: 0;
}

.discuss-zy-page__tabs >>> .el-tabs__item.is-active {
  color: #5cc477;
}

.discuss-zy-page__tabs >>> .el-tabs__active-bar {
  background-color: #5cc477;
}

.discuss-zy-page__link {
  color: #5cc477;
  cursor: pointer;
}

.discuss-zy-page__pagination {
  display: flex;
  justify-content: center;
  padding-top: 12px;
}

.nf-table-card .el-button + .el-button {
  margin-left: 0 !important;
  margin-top: 5px !important;
}
</style>
