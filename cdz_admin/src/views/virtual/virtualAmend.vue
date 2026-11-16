<template>
  <div class="nf-page virtual-amend-page">
    <div class="nf-page-hero">
      <span class="nf-page-hero__badge">虚拟商品</span>
      <h2 class="nf-page-hero__title">兑换码列表</h2>
      <p class="nf-page-hero__desc">查看与管理当前虚拟商品的兑换码、兑换地址及关联订单信息，支持修改兑换码状态与内容。</p>
      <div class="nf-page-hero__bar"></div>
    </div>

    <div class="nf-filter-card">
      <div class="nf-filter-card__head">
        <i class="el-icon-setting nf-filter-card__head-icon"></i>
        <span>快捷操作</span>
      </div>
      <el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
        <el-form-item class="virtual-amend-page__filter-actions">
          <el-button type="primary" icon="el-icon-back" @click="prev">返回上一页</el-button>
          <el-button type="primary" icon="el-icon-refresh" @click="refresh">刷新</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="nf-table-card">
      <el-table
        v-loading="tableDataLoading"
        :data="tableData.data">
        <el-table-column
          prop="id"
          label="编号"
          width="80">
        </el-table-column>
        <el-table-column
          prop="coverImg"
          label="商品图片">
          <template slot-scope="scope">
            <img :src="scope.row.cover_img" alt="" width="60" height="60">
          </template>
        </el-table-column>
        <el-table-column
          prop="title"
          label="商品标题"
          width="200">
        </el-table-column>
        <el-table-column
          prop="content"
          label="兑换码">
        </el-table-column>
        <el-table-column
          prop="link_url"
          label="兑换地址"
          width="200">
        </el-table-column>
        <el-table-column
          label="订单号"
          width="180">
          <template slot-scope="scope">
            <span
              class="virtual-amend-page__link"
              @click="orderDetails(scope.row.data.orderNum)">{{ scope.row.data.orderNum ? scope.row.data.orderNum : '暂无' }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="支付金额">
          <template slot-scope="scope">
            <span>{{ scope.row.data.payMoney ? scope.row.data.payMoney : '暂无' }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="支付方式">
          <template slot-scope="scope">
            <span class="virtual-amend-page__brand-text" v-if="scope.row.paymenttype === 'wechat'">微信</span>
            <span class="virtual-amend-page__brand-text" v-if="scope.row.paymenttype === 'alipay'">微信</span>
            <span class="virtual-amend-page__brand-text" v-if="scope.row.paymenttype === 'lingqian'">零钱</span>
            <span class="virtual-amend-page__brand-text" v-if="scope.row.paymenttype === 'jifen'">积分</span>
            <span class="virtual-amend-page__brand-text" v-if="scope.row.paymenttype === null">未支付</span>
          </template>
        </el-table-column>
        <el-table-column
          label="用户名">
          <template slot-scope="scope">
            <span>{{ scope.row.data.nickName ? scope.row.data.nickName : '暂无' }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="电话"
          width="150">
          <template slot-scope="scope">
            <span>{{ scope.row.data.phone ? scope.row.data.phone : '暂无' }}</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="status"
          label="状态"
          fixed="right">
          <template slot-scope="scope">
            <span class="virtual-amend-page__brand-text" v-if="scope.row.status == 1">正常</span>
            <span v-if="scope.row.status == 2">关闭</span>
            <span v-if="scope.row.status == 3">已使用</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="expressTime"
          label="出售时间"
          width="160">
          <template slot-scope="scope">
            <span>{{ scope.row.data.expressTime ? scope.row.data.expressTime : '未出售' }}</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="create_time"
          label="创建时间"
          width="160">
        </el-table-column>
        <el-table-column
          label="操作"
          width="100"
          fixed="right">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="primary"
              v-if="scope.row.status != '3'"
              @click="compile(scope.row)">修改
            </el-button>
            <el-button
              v-else
              size="mini"
              type="primary"
              disabled>修改
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="virtual-amend-page__pagination">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :page-sizes="[10, 15, 20, 25]"
          :page-size="size"
          :current-page="page"
          layout="total,sizes, prev, pager, next"
          :total="tableData.total">
        </el-pagination>
      </div>
    </div>

    <!-- 修改兑换码 -->
    <el-dialog title="兑换码修改" :visible.sync="dialogFormVisible" center>
      <el-form :model="form">
        <el-form-item label="兑换地址：" :label-width="formLabelWidth">
          <el-input v-model="form.linkUrl" style="width:65%;" placeholder="请输入兑换地址"></el-input>
        </el-form-item>
        <el-form-item label="兑换码：" :label-width="formLabelWidth">
          <el-input v-model="form.content" style="width:65%;" placeholder="请输入兑换码"></el-input>
        </el-form-item>
        <el-form-item label="状态：" :label-width="formLabelWidth">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">正常</el-radio>
            <el-radio :label="2">关闭</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="compileNoticeTo()">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
	export default {
        data() {
          return {
          size:10,
          page:1,
          title:'',
          linkUrl:'',
          content:'',
          goodsId:'',
          coverImg:'',
          content:'',
          type:'',
          status:0,
          form:{
            id:'',
            goodsId:'',
            title:'',
            linkUrl:'',
            content:'',
            coverImg:'',
            status:'',
            createTime:'',
          },
          formLabelWidth:'200px',
          tableDataLoading:false,
          dialogFormVisible:false,
          tableData:[],
        }
      },
      methods: {
		// 详情
		orderDetails (id) {
		    this.$router.push({path: '/orderDetails', query: {id: id}})
		},
        // 返回上一级
        prev(){
        	this.$router.back()
        },
        handleSizeChange(val) {
          this.size = val;
          this.shopSelect()
        },
        handleCurrentChange(val) {
          this.page = val;
          this.shopSelect()
        },
        // 刷新
        refresh(){
          this.shopSelect()
        },
        // 补货弹框
        compile(row){
          console.log(row);
          this.dialogFormVisible = true
          this.form.title = row.title
          this.form.coverImg = row.cover_img
          this.form.goodsId = row.goods_id
          this.form.linkUrl = row.link_url
          this.form.content = row.content
          this.form.id = row.id
          this.form.status = row.status
          this.form.createTime = row.create_time
        },
        // 补货
        compileNoticeTo(){
          this.$http({
            url: this.$http.adornUrl2('/admin/goodsxuni/bianji_edit'),
            method: 'post',
            data: this.$http.adornData({
            'duihuan_pwd':this.form.content,
            'duihuan_url':this.form.linkUrl,
            'xuni_id':this.form.id,
            'status':this.form.status,
            })
          }).then(({data}) => {
            this.dialogFormVisible = false
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                 this.shopSelect()
                }
              })
          })
        },
        // 获取商品数据
        shopSelect () {
          let goodsId = this.$route.query.goodsId
          this.tableDataLoading = true
          this.$http({
           url: this.$http.adornUrl2('/admin/goodsxuni/xuni_cardpass'),
           method: 'get',
           params: this.$http.adornParams({
            'page':this.page,
            'num':this.size,
            'goods_id':goodsId,
           })
          }).then(({data}) => {
            this.tableDataLoading = false
            let returnData = data.data;
            this.tableData = returnData
          })
        },
      },
      mounted() {
        this.shopSelect()
      }
	  };
</script>

<style scoped>
.virtual-amend-page__filter-actions {
	margin-bottom: 0;
}

.virtual-amend-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.virtual-amend-page__link {
	color: #5cc477;
	cursor: pointer;
}

.virtual-amend-page__brand-text {
	color: #5cc477;
}

.virtual-amend-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

.nf-table-card .el-button + .el-button {
	margin-left: 8px;
}
</style>
