<template>
	<div class="nf-page shop-admin-zy-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">自营商城</span>
			<h2 class="nf-page-hero__title">商品管理</h2>
			<p class="nf-page-hero__desc">管理自营商城商品列表，支持按标题、类型、状态筛选查询，可发布、修改、删除商品及跳转评价管理。</p>
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
						class="shop-admin-zy-page__filter-input"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入商品标题"
						v-model="title">
					</el-input>
				</el-form-item>
				<el-form-item label="商品类型">
					<el-cascader
						class="shop-admin-zy-page__filter-cascader"
						v-model="value"
						:options="classDatas"
						:show-all-levels="false"
						:change-on-select="true"
						:props="{ expandTrigger: 'hover' }"
						@change="handleChange">
					</el-cascader>
				</el-form-item>
				<el-form-item label="商品状态">
					<el-select
						class="shop-admin-zy-page__filter-select"
						v-model="status"
						placeholder="请选择商品状态"
						@change="select(status)">
						<el-option v-for="item in statusmain" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item class="shop-admin-zy-page__filter-actions">
					<el-button type="primary" icon="el-icon-search" @click="select">查询</el-button>
					<el-button type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
					<el-button
						:disabled="!isAuth('shopAdminZy:add')"
						type="primary"
						icon="el-icon-plus"
						@click="addShop">发布商品</el-button>
				</el-form-item>
			</el-form>
		</div>

		<div class="nf-table-card">
			<el-table v-loading="tableDataLoading" :data="tableData.content">
				<el-table-column fixed prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column fixed prop="coverImg" label="商品图片">
					<template slot-scope="scope">
						<img :src="scope.row.coverImg" alt="" width="60" height="60">
					</template>
				</el-table-column>
				<el-table-column prop="type" label="商品类型">
					<template slot-scope="scope">
						<span v-if="scope.row.type == null">暂无分类</span>
						<span v-else>{{scope.row.type.name}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="title" label="商品标题" width="200">
				</el-table-column>
				<el-table-column prop="originalPrice" label="商品原价">
					<template slot-scope="scope">
						<span class="shop-admin-zy-page__price">{{scope.row.originalPrice | numFilter}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="price" label="商品价格">
					<template slot-scope="scope">
						<span class="shop-admin-zy-page__price">{{scope.row.price | numFilter}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="commissionPrice" label="佣金">
					<template slot-scope="scope">
						<span class="shop-admin-zy-page__price">{{scope.row.commissionPrice | numFilter}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="sales" label="商品销量">
				</el-table-column>
				<el-table-column prop="buyReason" label="必买理由" width="200">
				</el-table-column>
				<el-table-column label="精选好物" width="120">
					<template slot-scope="scope">
						<span v-if="scope.row.isSelect == 0">否</span>
						<span v-if="scope.row.isSelect == 1">是</span>
					</template>
				</el-table-column>
				<el-table-column label="首页商品" width="120">
					<template slot-scope="scope">
						<span v-if="scope.row.homeGoods== 0">否</span>
						<span v-if="scope.row.homeGoods == 1">是</span>
					</template>
				</el-table-column>
				<el-table-column label="每日推荐" width="120">
					<template slot-scope="scope">
						<span v-if="scope.row.isRecommend== 0">否</span>
						<span v-if="scope.row.isRecommend == 1">是</span>
					</template>
				</el-table-column>
				<el-table-column label="是否需要发货" width="150">
					<template slot-scope="scope">
						<span v-if="scope.row.isExpress == 2">虚拟商品无需发货</span>
						<span v-if="scope.row.isExpress == 1">普通商品需要发货</span>
						<span v-if="scope.row.isExpress == 3">门店核销无需发货</span>
					</template>
				</el-table-column>
				<el-table-column prop="status" label="商品状态">
					<template slot-scope="scope">
						<span class="shop-admin-zy-page__tag" v-if="scope.row.status === 1 ">上架</span>
						<span class="shop-admin-zy-page__tag" v-if="scope.row.status === 2 ">下架</span>
					</template>
				</el-table-column>
				<el-table-column prop="createAt" label="创建时间" width="160">
				</el-table-column>
				<el-table-column fixed="right" label="操作" width="240">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" @click="discuss(scope.$index, scope.row)">评价
						</el-button>
						<el-button size="mini" type="primary" :disabled="!isAuth('shopAdminZy:update')"
							@click="shopUpdates(scope.$index, scope.row)">修改
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('shopAdminZy:delete')"
							@click="shopdeletes(scope.row)">
							删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="shop-admin-zy-page__pagination">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[5, 10, 15, 20]" :page-size="size" :current-page="page"
					layout="total,sizes, prev, pager, next" :total="totalnum">
				</el-pagination>
			</div>
		</div>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				size: 10,
				page: 1,
				pagelist: [],
				id: '',
				img: '',
				title: '',
				type: '',
				typeId: '',
				readonly: true,
				merchants: '',
				price: '',
				memberPrice: '',
				originalPrice: '',
				sales: '',
				status: 0,
				status2: '',
				descrition: '',
				totalnum: 0,
				imgUrl: '',
				linkUrl: '',
				value: [],
				classDatas: [],
				form1: {
					id: '',
					title: '',
					img: '',
					typeId: '',
					merchants: '',
					price: '',
					memberPrice: '',
					status: '',
					sales: '',
					descrition: '',
					originalPrice: '',
					createAt: ''
				},
				statusmain: [{
						value: 0,
						label: '全部'
					},
					{
						value: 1,
						label: '上架'
					},
					{
						value: 2,
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
				dialogFormVisible2: false,
				dialogFormVisible3: false,
				typeDatas: [],
				advertData: [],
				tableData: {},
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
		computed: {

		},
		methods: {
			// 商品评价跳转
			discuss(index, rows) {
				this.$router.push({
					path: '/discuss',
					query: {
						goodsId: rows.id
					}
				});
			},
			handleChange(value) {
				this.value.forEach(element => {
					this.typeId = element;
				});
			},

			handleAvatarSuccess(file) {
				this.img = file.data;
			},
			handleAvatarSuccess2(file) {
				this.form1.img = file.data;
			},
			handleSizeChange(val) {
				this.size = val;
				this.shopSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				// for (let item of this.pagelist) {
				//   if (item.router == "shopAdmin") {
				//     item.page = val;
				//   }
				// }
				// this.$store.commit("common/updataPage", this.pagelist);
				this.shopSelect()
			},
			//搜索
			select() {
				this.shopSelect()
			},
			// 重置
			cleans() {
				this.title = ''
				this.status = 0
				this.typeId = ''
				this.value = []
				this.shopSelect()
			},
			//发布商品
			addShop() {
				this.$router.push({
					path: '/shopPublishZy'
				})
			},
			// 修改商品
			shopUpdates(index, rows) {
				this.$router.push({
					path: '/shopAmendZy',
					query: {
						id: rows.id
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
						url: this.$http.adornUrl('shopGoods/delete'),
						method: 'get',
						params: this.$http.adornParams({
							'id': row.id
						})
					}).then(({
						data
					}) => {
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.shopSelect()
							}
						})
					})
				}).catch(() => {})
			},
			// 获取商品数据
			shopSelect() {

				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('shopGoods/list'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page - 1,
						'size': this.size,
						'title': this.title,
						'type': this.typeId,
						'status': this.status
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData
					this.totalnum = returnData.totalElements
				})
			},
			// 商品分类
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('goodsType/result'),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.classDatas = returnData
				})
			}
		},
		mounted() {
			this.shopSelect()
			this.dataSelect()
		}
	};
</script>

<style scoped>
.shop-admin-zy-page__filter-input {
	width: 150px;
}

.shop-admin-zy-page__filter-cascader {
	width: 150px;
}

.shop-admin-zy-page__filter-select {
	width: 150px;
}

.shop-admin-zy-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.shop-admin-zy-page__tag {
	color: #5cc477;
	cursor: pointer;
}

.shop-admin-zy-page__price {
	color: #f56c6c;
}

.shop-admin-zy-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

.shop-admin-zy-page .nf-table-card .el-button + .el-button {
	margin-left: 0;
	margin-top: 5px;
}
</style>
