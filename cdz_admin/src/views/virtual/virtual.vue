<template>
	<div class="nf-page virtual-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">虚拟商品</span>
			<h2 class="nf-page-hero__title">虚拟商品</h2>
			<p class="nf-page-hero__desc">管理虚拟商品库存与卡密，支持按商品标题筛选，可进行补货、编辑及查看积分商品与价格信息。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">
				<i class="el-icon-search nf-filter-card__head-icon"></i>
				<span>筛选条件</span>
			</div>
			<el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
				<el-form-item label="商品标题">
					<el-input
						class="virtual-page__filter-input"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入商品标题"
						v-model="title">
					</el-input>
				</el-form-item>
				<el-form-item class="virtual-page__filter-actions">
					<el-button type="primary" icon="el-icon-search" @click="select">查询</el-button>
					<el-button type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
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
						<img :src="scope.row.goods_image" alt="" width="60" height="60">
					</template>
				</el-table-column>
				<el-table-column
					prop="title"
					label="商品标题"
					width="200">
				</el-table-column>
				<el-table-column
					prop="isJiFenGoods"
					label="是否积分商品">
					<template slot-scope="scope">
						<span v-if="scope.row.xuni_is_jifen == 1">是</span>
						<span v-else>否</span>
					</template>
				</el-table-column>
				<el-table-column
					prop="goods_info.old_price"
					label="商品原价">
				</el-table-column>
				<el-table-column
					prop="price"
					label="商品价格">
					<template slot-scope="scope">
						<span v-if="scope.row.isJiFenGoods == 1">{{scope.row.goods_info.new_price}} 积分</span>
						<span v-else>{{scope.row.goods_info.new_price}}</span>
					</template>
				</el-table-column>
				<el-table-column
					prop="goods_info.vip_price"
					label="会员价格">
				</el-table-column>
				<el-table-column
					prop="xuni_sum"
					label="虚拟商品总数">
				</el-table-column>
				<el-table-column
					prop="xuni_kucun"
					label="虚拟商品库存">
				</el-table-column>
				<el-table-column
					fixed="right"
					label="操作"
					width="150">
					<template slot-scope="scope">
						<el-button
							size="mini"
							type="primary"
							:disabled="!isAuth('virtual:replen')"
							@click="program(scope.row)">补货
						</el-button>
						<el-button
							size="mini"
							type="primary"
							@click="compile(scope.row)">编辑
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="virtual-page__pagination">
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

		<!-- 补货 -->
		<el-dialog title="补货" :visible.sync="dialogFormVisible" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">卡密链接：</span>
				<el-input style="width:60%;" v-model="linkUrl" placeholder="请输入卡密链接"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;position: relative;top: -110px;">卡密内容：</span>
				<el-input style="width:60%;" v-model="content" type="textarea" :rows="6" wrap="virtual" placeholder="请输入卡密内容"></el-input>
				<p style="font-size: 13px;color: #999999;padding-left: 200px;margin-top: 5px;">*可以批量新增，每行一个，可以智能去重</p>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="programNoticeTo()">确 定</el-button>
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
          title1:'',
          linkUrl:'',
          goodsId:'',
          coverImg:'',
          content:'',
		  isJiFenGoods:0,
          type:'',
          status:0,
          isExpress:2,
          formLabelWidth:'200px',
          tableDataLoading:false,
          dialogFormVisible:false,
          tableData:[],
        }
      },
      methods: {
        handleSizeChange(val) {
          this.size = val;
          this.shopSelect()
        },
        handleCurrentChange(val) {
          this.page = val;
          this.shopSelect()
        },
        //搜索
        select(){
           this.shopSelect()
        },
        // 重置
        cleans(){
          this.title = ''
          this.status = 0
          this.shopSelect()
        },
        // 刷新
        refresh(){
          this.shopSelect()
        },
        // 编辑
        compile(rows){
           this.$router.push({path: '/virtualAmend', query: {goodsId: rows.id}});
        },
        // 补货弹框
        program(row){
          this.dialogFormVisible = true
          this.title1 = row.title
          this.coverImg = row.coverImg
          this.goodsId = row.id
        },
        // 补货
        programNoticeTo(){
          let content = this.content.replace(/\r\n/g, ',').replace(/\n/g, ',').replace(/\s/g, ',')
          let cont = content.split(',')
          cont  = [...new Set(cont)]
          this.content = cont.join(',')
          if (this.linkUrl == '') {
              this.$notify({
                  title: '提示',
                  duration: 1800,
                  message: '请输入卡密链接',
                  type: 'warning'
              });
              return
          }
          if (this.content == '') {
              this.$notify({
                  title: '提示',
                  duration: 1800,
                  message: '请输入卡密内容',
                  type: 'warning'
              });
              return
          }
          this.$http({
            url: this.$http.adornUrl2('/admin/goodsxuni/xuni_buhuo'),
            method: 'post',
            data: this.$http.adornData({
            'content':this.content,
            'link_url':this.linkUrl,
            'xuni_goods_id':this.goodsId,
            })
          }).then(({data}) => {
            if(data.code == 0){
              this.dialogFormVisible = false
              this.$message({
                message: data.data,
                type: 'success',
                duration: 3500,
                onClose: () => {
                 this.shopSelect()
                }
              })
            }else{
              this.$message({
                message: data.data,
                type: 'error',
                duration: 3500,
                onClose: () => {
                 this.shopSelect()
                }
              })
            }
          })
        },
        // 获取商品数据
        shopSelect () {
          let status = 0
          this.tableDataLoading = true
          this.$http({
           url: this.$http.adornUrl2('/admin/goodsxuni/goods_xuni_list'),
           method: 'get',
           params: this.$http.adornParams({
            'page':this.page,
            'num':this.size,
            'search_name':this.title,
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
.virtual-page__filter-input {
	width: 200px;
}

.virtual-page__filter-actions {
	margin-bottom: 0;
}

.virtual-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.virtual-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

.nf-table-card .el-button + .el-button {
	margin-left: 8px;
}
</style>
