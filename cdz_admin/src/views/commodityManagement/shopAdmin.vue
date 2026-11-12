<template>
	<div class="nf-page shop-admin-sm-page">
		<div class="shop-admin-sm-page__back">
			<a href="#" @click.prevent="prev" class="shop-admin-sm-page__back-link">
				<icon-svg name="jiantou" class="shop-admin-sm-page__back-icon"></icon-svg>
				返回
			</a>
		</div>

		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">门店餐饮</span>
			<h2 class="nf-page-hero__title">商品列表</h2>
			<p class="nf-page-hero__desc">查看与管理门店商品信息，支持按标题、类型、状态筛选，以及新增、复用、修改库存与批量上下架操作。</p>
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
						class="shop-admin-sm-page__filter-input"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入商品标题"
						v-model="title">
					</el-input>
				</el-form-item>
				<el-form-item label="商品类型">
					<el-select
						class="shop-admin-sm-page__filter-select"
						v-model="classifyId"
						placeholder="请选择商品类型"
						@change="handleChange()">
						<el-option v-for="(item,index) in classDatas" :key="index" :label="item.classifyName"
							:value="item.classifyId">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="商品状态">
					<el-select
						class="shop-admin-sm-page__filter-select shop-admin-sm-page__filter-select--status"
						v-model="status"
						placeholder="请选择商品状态"
						@change="select(status)">
						<el-option v-for="item in statusmain" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item class="shop-admin-sm-page__filter-actions">
					<el-button type="primary" icon="el-icon-search" @click="select">查询</el-button>
					<el-button type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
				</el-form-item>
			</el-form>
		</div>

		<div class="nf-table-card">
			<div class="shop-admin-sm-page__toolbar">
				<el-button type="warning" icon="el-icon-copy-document" :disabled="!isAuth('shopAdminZ:add')" @click="addShop">复用商品</el-button>
				<el-button type="primary" icon="el-icon-plus" :disabled="!isAuth('shopsListSm:add')" @click="toAddShop">新增商品</el-button>
				<el-button type="primary" icon="el-icon-sort" @click="transferClcik()" :disabled="checkBoxData.length <= 0 || !isAuth('shopsListSm:update')">
					批量上下架
				</el-button>
			</div>
			<el-table v-loading="tableDataLoading" :data="tableData.list" @selection-change="changeFun">
				<el-table-column type="selection">
				</el-table-column>
				<el-table-column fixed prop="goodsId" label="编号" width="80">
				</el-table-column>
				<el-table-column fixed prop="goodsCover" label="商品封面图">
					<template slot-scope="scope">
						<img :src="scope.row.goodsCover" alt="" width="60" height="60">
					</template>
				</el-table-column>
				<el-table-column fixed prop="goodsCover" label="商品轮播图" width="150">
					<template slot-scope="scope">
						<div v-for="(item,index) in scope.row.goodsPicture" :key="index"
							class="shop-admin-sm-page__thumb">
							<el-popover placement="top-start" title="" trigger="hover">
								<img class="shop-admin-sm-page__thumb-img" :src="item" alt="" slot="reference">
								<img class="shop-admin-sm-page__preview-img" :src="item" alt="">
							</el-popover>
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="goodsName" label="商品类型">
					<template slot-scope="scope">
						<span v-if="scope.row.classifyName == null">暂无分类</span>
						<span v-else>{{scope.row.classifyName}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="goodsName" label="商品标题" width="200">
				</el-table-column>
				<el-table-column prop="goodsDescribe" label="商品描述" width="300">
				</el-table-column>
				<el-table-column prop="inventory" label="库存" width="180">
					<template slot-scope="scope">
						<span>{{scope.row.inventory}}</span>
						<div class="shop-admin-sm-page__inventory-edit">
							<el-input class="shop-admin-sm-page__inventory-input" @keydown.enter.native="kcselect(scope.row,scope.row.kucun)"
								clearable placeholder="请输入修改库存" v-model="scope.row.kucun">
							</el-input>
							<span @click="kcselect(scope.row,scope.row.kucun)" class="shop-admin-sm-page__inventory-icon">
								<icon-svg name="bianji" class="site-sidebar__menu-icon"></icon-svg>
							</span>
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="goodsMoney" label="商品售价">
					<template slot-scope="scope">
						<span>{{scope.row.goodsMoney | numFilter}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="packMoney" label="打包费">
					<template slot-scope="scope">
						<span>{{scope.row.packMoney | numFilter}}/单</span>
					</template>
				</el-table-column>
				<el-table-column prop="goodsLabel" label="标签">
				</el-table-column>
				<el-table-column prop="status" label="商品状态">
					<template slot-scope="scope">
						<el-switch v-model="scope.row.gsrStatus" @change="change(scope.row.gsrStatus,scope.row.goodsId)"
							:active-value="openValue" :inactive-value="closeValue" active-color="#5cc477"
							inactive-color="#ff4949">
						</el-switch>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160">
				</el-table-column>
				<el-table-column fixed="right" label="操作" width="150">
					<template slot-scope="scope">
						<el-button size="mini" v-if="scope.row.shopId==0" type="warning" disabled style="margin: 3px;">
							平台商品不可修改
						</el-button>
						<el-button size="mini" v-if="scope.row.shopId!=0" type="primary"
							:disabled="!isAuth('shopsListSm:update')" @click="shopUpdates(scope.row)" style="margin: 3px;">
							修改
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('shopsListSm:delete')"
							@click="shopdeletes(scope.row)" style="margin: 3px;">
							删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="shop-admin-sm-page__pagination">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10,  20, 30, 50, 100]" :page-size="size" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
				</el-pagination>
			</div>
		</div>

		<el-dialog :visible.sync="dialogFormVisible" center title="商品列表" width="80%" v-loading="tableDataLoading2">
			<div class="shop-admin-sm-page__dialog-filter">
				<span>商品标题：</span>
				<el-input class="shop-admin-sm-page__filter-input" @keydown.enter.native="select" clearable placeholder="请输入商品标题"
					v-model="title1">
				</el-input>
				<el-button type="primary" icon="el-icon-search" @click="select1">查询</el-button>
				<el-button type="primary" icon="el-icon-refresh-left" @click="cleans1">重置</el-button>
			</div>
			<el-table v-loading="tableDataLoading1" :data="tableData1.list">
				<el-table-column label="编号" prop="shopId" width="80" fixed="left">
				</el-table-column>
				<el-table-column label="店铺名称" prop="shopName" fixed="left">
				</el-table-column>
				<el-table-column label="手机号" prop="shopPhone">
				</el-table-column>
				<el-table-column prop="goodsCover" label="轮播图" width="150">
					<template slot-scope="scope">
						<div v-if="scope.row.shopBanner">
							<div v-for="(item,index) in scope.row.shopBanner.split(',')" :key="index"
								class="shop-admin-sm-page__thumb">
								<el-popover placement="top-start" title="" trigger="hover">
									<img class="shop-admin-sm-page__thumb-img" :src="item" alt="" slot="reference">
									<img class="shop-admin-sm-page__preview-img" :src="item" alt="">
								</el-popover>
							</div>
						</div>
					</template>
				</el-table-column>
				<el-table-column label="开店时间" prop="openTime">
				</el-table-column>
				<el-table-column label="闭店时间" prop="closeTime">
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160">
				</el-table-column>
				<el-table-column fixed="right" label="操作" width="150">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('shopAdminZ:add')" v-if="shopId!=scope.row.shopId" 
							@click="addFuyong(scope.row)">复用
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="shop-admin-sm-page__pagination">
				<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
					:page-sizes="[10, 20, 30,50,100]" :page-size="size1" :current-page="page1"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData1.totalCount">
				</el-pagination>
			</div>
		</el-dialog>

		<el-dialog :visible.sync="dialogFormVisible2" center title="批量修改状态">
			<div class="shop-admin-sm-page__dialog-row">
				<span class="shop-admin-sm-page__dialog-label">状态：</span>
				<el-radio-group v-model="statusUp">
					<el-radio :label="0">上架</el-radio>
					<el-radio :label="1">下架</el-radio>
				</el-radio-group>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible2 = false">取 消</el-button>
				<el-button type="primary" @click="releasNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				openValue: 0,
				closeValue: 1,
				size: 10,
				page: 1,
				size1: 10,
				page1: 1,
				pagelist: [],
				id: '',
				img: '',
				title: '',
				title1: '',
				type: '',
				typeId: '',
				readonly: true,
				merchants: '',
				price: '',
				memberPrice: '',
				originalPrice: '',
				sales: '',
				status: '',
				status2: '',
				descrition: '',
				totalnum: 0,
				imgUrl: '',
				linkUrl: '',
				classifyId: '',
				classDatas: [],
				statusmain: [{
						value: '',
						label: '全部'
					},
					{
						value: 0,
						label: '上架'
					},
					{
						value: 1,
						label: '下架'
					}
				],
				statusmain2: [{
						value: 1,
						label: '上架'
					},
					{
						value: 2,
						label: '下架'
					}
				],
				formLabelWidth: '200px',
				tableDataLoading: false,
				dialogFormVisible: false,
				tableDataLoading1: false,
				dialogFormVisible1: false,
				dialogFormVisible2: false,
				dialogFormVisible3: false,
				typeDatas: [],
				advertData: [],
				tableData: [],
				tableData1: [],
				shopId: '',
				xuanze: [],
				kucun: '',
				checkBoxData: [], //多选框选择的值
				statusUp:'',
				types:1,
				tableDataLoading2:false,
			}
		},
		filters: {
			numFilter(value) {
				let realVal = ''
				if (!isNaN(value) && value !== '') {
					// 截取当前数据到小数点后三位
					let tempVal = parseFloat(value).toFixed(3)
					realVal = tempVal.substring(0, tempVal.length - 1)
				} else {
					realVal = '--'
				}
				return realVal
			}
		},
		methods: {
			handleChange(value) {
				this.shopSelect()
			},

			handleAvatarSuccess(file) {
				this.img = file.data;
			},
			handleSizeChange(val) {
				this.size = val;
				this.shopSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.shopSelect()
			},
			handleSizeChange1(val) {
				this.size1 = val;
				this.shopSelect1()
			},
			handleCurrentChange1(val) {
				this.page1 = val;
				this.shopSelect1()
			},
			//搜索
			select() {
				this.page = 1
				this.shopSelect()
			},
			// 重置
			cleans() {
				this.page = 1
				this.title = ''
				this.status = ''
				this.classifyId = ''
				this.shopSelect()
			},
			//搜索
			select1() {
				this.shopSelect1()
			},
			// 重置
			cleans1() {
				this.page = 1
				this.title1 = ''
				this.shopSelect1()
			},
			//添加商品
			addShop() {
				this.page1 = 1
				this.title1 = ''
				this.shopSelect1()
				this.dialogFormVisible = true
			},
			// 修改库存
			kcselect(row, kucun) {
				console.log('修改库存', row, kucun)
				this.$http({
					url: this.$http.adornUrl('admin/goodsShop/updateGoodsByShopId'),
					method: 'post',
					data: this.$http.adornData({
						'goodsId': row.goodsId,
						'shopId': this.shopId,
						'inventory': kucun
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							message: '修改成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.shopSelect()
							}
						})
						this.xuanze = []
						this.dialogFormVisible = false
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
			// 选择商品
			handleSelectionChange(val) {
				console.log('选择商品', val)
				var arr = []

				if (val.length > 0) {
					for (var i in val) {
						var data = {}
						data.shopId = this.shopId
						data.goodsId = val[i].goodsId
						arr.push(data)
					}

				}
				this.xuanze = arr
			},
			add(row) {
				var goodsShopRelevancyList = []
				var datas = {}
				if (row == 1) {

					goodsShopRelevancyList = this.xuanze
					console.log('多个添加商品', goodsShopRelevancyList)
				} else {
					datas.shopId = this.shopId
					datas.goodsId = row.goodsId
					goodsShopRelevancyList.push(datas)
					console.log('单个添加商品', goodsShopRelevancyList)

				}
				if (goodsShopRelevancyList.length == 0) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择商品',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl('admin/goodsShop/addGoodsByShopId'),
					method: 'post',
					data: this.$http.adornData(
						goodsShopRelevancyList)
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							message: '添加成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.shopSelect()
							}
						})
						this.xuanze = []
						this.dialogFormVisible = false
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
			// 修改商品
			shopUpdates(rows) {
				this.$router.push({
					path: '/shopAmendS',
					query: {
						goodsId: rows.goodsId,
						shopId: rows.shopId
					}
				});
			},
			// 删除商品
			shopdeletes(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/goodsShop/deleteGoodsByShopId'),
						method: 'post',
						params: this.$http.adornParams({
							'shopId': this.shopId,
							'goodsId': row.goodsId
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.shopSelect()
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
				}).catch(() => {})
			},
			// 获取门店商品数据
			shopSelect() {
				let page = this.page
				this.tableDataLoading = true
				this.shopId = this.$route.query.shopId
				this.$http({
					url: this.$http.adornUrl('admin/goodsShop/selectGoodsByShopId'),
					method: 'get',
					params: this.$http.adornParams({
						'page': page,
						'limit': this.size,
						'goodsName': this.title,
						'classifyId': this.classifyId,
						'status': this.status,
						'shopId': this.shopId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					for (var i in data.data.list) {
						if (data.data.list[i].goodsPicture) {
							data.data.list[i].goodsPicture = data.data.list[i].goodsPicture.split(',')
						}

					}
					let returnData = data.data;

					this.tableData = returnData
					this.totalnum = returnData.totalElements
				})
			},
			// 获取添加的商品数据
			shopSelect1() {
				let page = this.page1
				this.tableDataLoading1 = true
				this.$http({
					url: this.$http.adornUrl('admin/takeoutGoods/selectAllShop'),
					method: 'get',
					params: this.$http.adornParams({
						'page': page,
						'limit': this.size1,
						'shopName': this.title1,
						'shopPhone': '',
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading1 = false
					for (var i in data.data.list) {
						if (data.data.list[i].goodsPicture) {
							data.data.list[i].goodsPicture = data.data.list[i].goodsPicture.split(',')
						}
					}
					let returnData = data.data;
					this.tableData1 = returnData
					for (var i in this.tableData1.list) {
						this.tableData1.list[i].kucun = ''
					}

					this.totalnum = returnData.totalElements
				})
			},
			// 商品分类
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/takeoutGoods/selectAllClassify'),
					method: 'get',
					params: this.$http.adornParams({
						'shopId': this.shopId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.classDatas = returnData
				})
			},
			// 返回上一级
			prev() {
				this.$router.go(-1)
			},
			// 状态
			change(status, goodsId) {
				this.$http({
					url: this.$http.adornUrl('admin/goodsShop/soldOutOrPutaway'),
					method: 'post',
					params: this.$http.adornParams({
						'shopId': this.shopId,
						'goodsId': goodsId,
						'status': status
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
								this.shopSelect()
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {
								this.shopSelect()
							}
						})
					}

				})
			},
			// 新增商品跳转
			toAddShop() {
				this.$router.push({
					path: '/shopPublishS',
					query: {
						shopId: this.shopId
					}
				})
			},
			// 多选
			changeFun(val) {
				this.checkBoxData = val
			},
			// 批量上下架
			transferClcik() {
				this.types = 1
				this.statusUp = ''
				this.dialogFormVisible2 = true
				
			},
			releasNoticeTo(id){
				 
				if (this.statusUp === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择状态',
						type: 'warning'
					})
					return
				}
				
				var ids = id ? [id] : this.checkBoxData.map(item => {
					return item.goodsId
				})
				// for (var i in ids) {
					this.$http({
						url: this.$http.adornUrl('admin/goodsShop/soldOutOrPutaway'),
						method: 'post',
						params: this.$http.adornParams({
							'shopId': this.shopId,
							'goodsId': ids.toString(),
							'status': this.statusUp
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$notify({
								title: '提示',
								duration: 1800,
								message: '操作成功',
								type: 'warning'
							})
							this.dialogFormVisible2 = false
							this.shopSelect()
							
						}else{
							this.$notify({
								title: '提示',
								duration: 1800,
								message: data.msg,
								type: 'error'
							})
							return
							
						}
					})
					
				// }
				// var ttimes = setTimeout(() =>{
				// 	if(this.types == 2){
						
				// 		return
				// 	}
				// 	if(this.types == 3){
						
				// 	}
				// },1000)
				
			},
			// 复用商品
			addFuyong(row){ 
				this.tableDataLoading2 = true
				
				this.$http({
					url: this.$http.adornUrl('admin/goodsShop/copyGoodsByShopId'),
					method: 'post',
					params: this.$http.adornParams({
						'shopId': this.shopId,
						'byShopId': row.shopId,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '操作成功',
							type: 'warning'
						})
						this.tableDataLoading2 = false
						this.dialogFormVisible = false
						this.shopSelect()
						
					}else{
						this.tableDataLoading2 = false
						this.$notify({
							title: '提示',
							duration: 1800,
							message: data.msg,
							type: 'error'
						})
						return
						
					}
				})
			},
		},
		mounted() {
			this.shopId = this.$route.query.shopId
			this.shopSelect()
			this.dataSelect()
		},
		watch: {
			'$route': 'shopSelect'

		}
	};
</script>

<style scoped>
.shop-admin-sm-page__back {
	margin-bottom: 12px;
}

.shop-admin-sm-page__back-link {
	display: inline-flex;
	align-items: center;
	font-size: 14px;
	color: #606266;
	text-decoration: none;
}

.shop-admin-sm-page__back-link:hover {
	color: #5cc477;
}

.shop-admin-sm-page__back-icon {
	width: 1.2em;
	height: 1.2em;
	margin-right: 4px;
}

.shop-admin-sm-page__filter-input {
	width: 150px;
}

.shop-admin-sm-page__filter-select {
	width: 200px;
}

.shop-admin-sm-page__filter-select--status {
	width: 150px;
}

.shop-admin-sm-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.shop-admin-sm-page__toolbar {
	display: flex;
	flex-wrap: wrap;
	align-items: center;
	gap: 12px;
	margin-bottom: 12px;
}

.shop-admin-sm-page__thumb {
	display: inline-block;
	margin: 3px;
}

.shop-admin-sm-page__thumb-img {
	width: 50px;
	height: 50px;
}

.shop-admin-sm-page__preview-img {
	width: 300px;
	height: auto;
}

.shop-admin-sm-page__inventory-edit {
	position: relative;
	display: inline-block;
}

.shop-admin-sm-page__inventory-input {
	width: 160px;
}

.shop-admin-sm-page__inventory-icon {
	position: absolute;
	right: 18px;
	top: 8px;
	cursor: pointer;
}

.shop-admin-sm-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

.shop-admin-sm-page__dialog-filter {
	display: flex;
	flex-wrap: wrap;
	align-items: center;
	gap: 12px;
	margin-bottom: 16px;
	font-size: 14px;
	color: #606266;
}

.shop-admin-sm-page__dialog-row {
	margin-bottom: 10px;
}

.shop-admin-sm-page__dialog-label {
	width: 200px;
	display: inline-block;
	text-align: right;
	margin-right: 8px;
}

.shop-admin-sm-page .nf-table-card .el-button + .el-button {
	margin-left: 0;
	margin-top: 5px;
}
</style>
