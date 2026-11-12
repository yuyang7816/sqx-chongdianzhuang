<template>
  <div class="nf-page commodity-page">
    <div class="nf-page-hero">
      <span class="nf-page-hero__badge">展示柜管理</span>
      <h2 class="nf-page-hero__title">展示柜商品管理</h2>
      <p class="nf-page-hero__desc">管理展示柜内商品信息，支持添加、修改、删除与刷新列表，维护商品名称、图片、金额、库存与介绍。</p>
      <div class="nf-page-hero__bar"></div>
    </div>

    <div class="nf-filter-card">
      <div class="nf-filter-card__head">
        <i class="el-icon-s-operation nf-filter-card__head-icon"></i>
        <span>快捷操作</span>
      </div>
      <div class="commodity-page__filter-actions">
        <el-button type="primary" icon="el-icon-refresh" @click="refresh1">刷新</el-button>
      </div>
    </div>

    <el-tabs v-model="activeName" class="commodity-page__tabs">
      <el-tab-pane label="商品列表" name="first">
        <div class="nf-table-card">
          <div class="commodity-page__toolbar">
            <el-button :disabled="!isAuth('commodity:add')" type="primary" icon="document"
              @click="addCouponSp(1)">添加</el-button>
          </div>
          <el-table v-loading="tableDataLoading" :data="shangpinData.list">
            <el-table-column prop="id" label="编号" width="80">
            </el-table-column>
            <el-table-column prop="title" label="商品名称">
            </el-table-column>
            <el-table-column prop="image" label="商品图片" width="150">
              <template slot-scope="scope">
                <img :src="scope.row.image" alt="" width="60" height="60">
              </template>
            </el-table-column>
            <el-table-column prop="money" label="金额(元)" width="100">
            </el-table-column>
            <el-table-column prop="goodsNum" label="商品库存" width="100">
            </el-table-column>
            <el-table-column prop="goodsDesc" label="商品介绍" width="250">
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间" width="150">
            </el-table-column>
            <el-table-column label="操作" width="150">
              <template slot-scope="scope">
                <el-button size="mini" type="primary" :disabled="!isAuth('commodity:update')"
                  @click="addCouponSp(scope.row)">修改
                </el-button>
                <el-button size="mini" type="danger" :disabled="!isAuth('commodity:delete')"
                  @click="couponDeletesSp(scope.row)">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <div class="commodity-page__pagination">
            <el-pagination @size-change="handleSizeChangeSp" @current-change="handleCurrentChangeSp"
              :page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
              layout="total,sizes, prev, pager, next" :total="shangpinData.totalCount">
            </el-pagination>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>

    <el-dialog :title="titles" :visible.sync="dialogFormVisibleSp" center>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">商品名称：</span>
        <el-input style="width:50%;" v-model="title" placeholder="请输入商品名称"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">金额(元)：</span>
        <el-input style="width:50%;" v-model="money" type="number" placeholder="请输入金额(元)">
        </el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">商品库存：</span>
        <el-input style="width:50%;" v-model="goodsNum" type="number" :min="0" placeholder="请输入商品库存">
        </el-input>
      </div>
      <div style="margin-bottom: 10px;display: flex;align-items: center;">
        <span style="display: inline-block;text-align: right;width: 200px;">商品图片：</span>
        <div
          style="width:148px;height:148px;background-color: #fbfdff; border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
          <el-upload class="avatar-uploader" v-model="image" :action="$http.adornUrlUp('alioss/upload')"
            :show-file-list="false" :on-success="handleAvatarSuccess3">
            <img v-if="image" :src="image" class="avatar" style="width: 148px;height: 148px;border-radius:6px;" />
            <i v-else class="el-icon-plus avatar-uploader-icon" style="font-size: 28px;color: #8c939d"></i>
          </el-upload>
        </div>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">商品介绍：</span>
        <el-input style="width:50%;" v-model="goodsDesc" type="text" placeholder="请输入商品介绍">
        </el-input>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisibleSp = false">取 消</el-button>
        <el-button type="primary" @click="couponNoticeToSp()">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        activeName: 'first',
        page: 1,
        size: 10,
        shangpinData: {},
        titles: '添加',
        id: '',
        title: '',
        money: '',
        goodsNum: '',
        image: '',
        goodsDesc: '',
        dialogFormVisibleSp: false,
        tableDataLoading: false,
        userId: '',
      }
    },
    methods: {
      handleSizeChangeSp(val) {
        this.size = val
        this.shangpinSelect()
      },
      handleCurrentChangeSp(val) {
        this.page = val
        this.shangpinSelect()
      },
      // 刷新
      refresh1() {
        this.page = 1
        this.shangpinSelect()
      },
      // 商品管理
      shangpinSelect() {
        this.tableDataLoading = true
        this.$http({
          url: this.$http.adornUrl('smallGoods/selectSmallGoodsList'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.page,
            'limit': this.size,
          })
        }).then(({
          data
        }) => {
          this.tableDataLoading = false
          let returnData = data.data;
          this.shangpinData = returnData
        })
      },
      // 添加商品
      addCouponSp(row) {
        console.log('row```', row)
        if (row != 1) {
          this.titles = '修改'
          this.id = row.id
          this.title = row.title
          this.money = row.money
          this.goodsNum = row.goodsNum
          this.image = row.image
          this.goodsDesc = row.goodsDesc
          this.dialogFormVisibleSp = true
        } else {
          this.titles = '添加'
          this.id = ''
          this.title = ''
          this.money = ''
          this.goodsNum = ''
          this.image = ''
          this.goodsDesc = ''
          this.dialogFormVisibleSp = true
        }

      },
      // 添加商品
      couponNoticeToSp() {
        let storeId = this.$route.query.storeId
        let merchantId = this.$route.query.merchantId
        let storeName = this.$route.query.storeName
        if (this.title == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入商品名称',
            type: 'warning'
          });
          return
        }
        if (Number(this.money) === '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入金额',
            type: 'warning'
          });
          return
        }
        if (this.goodsNum === '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入商品库存',
            type: 'warning'
          });
          return
        }
        if (this.image == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请上传图片',
            type: 'warning'
          });
          return
        }
        if (this.goodsDesc == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入商品介绍',
            type: 'warning'
          });
          return
        }
        if (this.titles == '添加') {
          var url = 'smallGoods/insertSmallGoods'
        } else {
          var url = 'smallGoods/updateSmallGoods'
        }
        this.$http({
          url: this.$http.adornUrl(url),
          method: 'post',
          data: this.$http.adornData({
            'id': this.id,
            'title': this.title,
            'money': this.money,
            'goodsNum': this.goodsNum,
            'image': this.image,
            'goodsDesc': this.goodsDesc,
          })
        }).then(({
          data
        }) => {
          if (data.code == 0) {
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.dialogFormVisibleSp = false
                this.shangpinSelect()
              }
            })
          } else {
            this.$message({
              message: data.msg,
              type: 'warning',
              duration: 1500,
              onClose: () => {}
            })
          }
        })
      },
      handleAvatarSuccess3(file, fileList) {
        console.log(file)
        this.image = file.data
      },
      // 删除商品
      couponDeletesSp(row) {
        this.$confirm(`确定删除此商品?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('smallGoods/deleteSmallGoods'),
            method: 'post',
            params: this.$http.adornParams({
              'id': row.id
            })
          }).then(({
            data
          }) => {
            if (data && data.code == 0) {
              this.$message({
                message: '删除成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.shangpinSelect()
                }
              })
            } else {
              this.$message({
                message: data.msg,
                type: 'error',
                duration: 1500,
                onClose: () => {}
              })
            }
          })
        }).catch(() => {})
      },
    },
    mounted() {
      this.shangpinSelect()
    }
  }
</script>

<style scoped>
.commodity-page__filter-actions .el-button + .el-button {
  margin-left: 12px;
}

.commodity-page__tabs {
  margin-bottom: 0;
}

.commodity-page__tabs >>> .el-tabs__header {
  margin-bottom: 0;
}

.commodity-page__tabs .nf-table-card {
  margin-bottom: 0;
}

.commodity-page__tabs >>> .el-tabs__item.is-active {
  color: #5cc477;
}

.commodity-page__tabs >>> .el-tabs__active-bar {
  background-color: #5cc477;
}

.commodity-page__toolbar {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 12px;
}

.commodity-page__toolbar .el-button + .el-button {
  margin-left: 12px;
}

.commodity-page__pagination {
  display: flex;
  justify-content: center;
  padding-top: 12px;
}
</style>
