<template>
	<div class="nf-page shop-admin1-page">
		<div class="shop-admin1-page__back">
			<a href="#" @click.prevent="prev" class="shop-admin1-page__back-link">
				<icon-svg name="jiantou" class="shop-admin1-page__back-icon"></icon-svg>
				返回
			</a>
		</div>

		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">商品管理</span>
			<h2 class="nf-page-hero__title">商品列表</h2>
			<p class="nf-page-hero__desc">查看与管理商品信息，支持按标题、类型、状态筛选查询，以及发布商品、上下架与评价管理。</p>
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
						class="shop-admin1-page__filter-input"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入商品标题"
						v-model="title">
					</el-input>
				</el-form-item>
				<el-form-item label="商品类型">
					<el-cascader
						class="shop-admin1-page__filter-cascader"
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
						class="shop-admin1-page__filter-select"
						v-model="status"
						placeholder="请选择商品状态"
						@change="select(status)">
						<el-option v-for="item in statusmain" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item class="shop-admin1-page__filter-actions">
					<el-button type="primary" icon="el-icon-search" @click="select">查询</el-button>
					<el-button type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
				</el-form-item>
			</el-form>
		</div>

		<div class="nf-table-card">
			<div class="shop-admin1-page__toolbar">
				<el-button type="primary" icon="el-icon-plus" :disabled="!isAuth('shopAdmin:add')" @click="addShop">发布商品</el-button>
				<el-button type="primary" icon="el-icon-refresh" @click="refresh">刷新</el-button>
			</div>
			<el-table v-loading="tableDataLoading" :data="tableData.data">
				<el-table-column fixed prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column fixed prop="goods_image" label="商品图片">
					<template slot-scope="scope">
						<img :src="scope.row.goods_image" alt="" width="60" height="60">
					</template>
				</el-table-column>
				<el-table-column prop="type" label="商品类型">
					<template slot-scope="scope">
						<span v-if="scope.row.type == null ">无数据</span>
						<span v-else>{{scope.row.type.name}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="title" label="商品标题" width="200">
				</el-table-column>
				<el-table-column prop="goods_money" label="商品原价">
					<template slot-scope="scope">
						<span style="color: #f56c6c;">{{scope.row.goods_money.old_price | numFilter}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="price" label="商品价格">
					<template slot-scope="scope">
						<span style="color: #f56c6c;">{{scope.row.goods_money.new_price | numFilter}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="memberPrice" label="会员价格">
					<template slot-scope="scope">
						<span style="color: #f56c6c;">{{scope.row.goods_money.vip_price | numFilter}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="commissionPrice" label="佣金">
					<template slot-scope="scope">
						<span style="color: #f56c6c;">{{scope.row.goods_money.com_price | numFilter}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="commissionPrice" label="积分">
					<template slot-scope="scope">
						<span style="color: #f56c6c;">{{scope.row.goods_jifen | numFilter}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="goods_sales" label="商品销量">
				</el-table-column>
				<el-table-column prop="goods_money.buy_reason" label="必买理由" width="200">
				</el-table-column>
				<el-table-column label="精选好物" width="120">
					<template slot-scope="scope">
						<span v-if="scope.row.is_jingxuan == 0">否</span>
						<span v-if="scope.row.is_jingxuan == 1">是</span>
					</template>
				</el-table-column>
				<el-table-column label="首页商品" width="120">
					<template slot-scope="scope">
						<span v-if="scope.row.is_index== 0">否</span>
						<span v-if="scope.row.is_index == 1">是</span>
					</template>
				</el-table-column>
				<el-table-column label="每日推荐" width="120">
					<template slot-scope="scope">
						<span v-if="scope.row.is_tuijian== 0">否</span>
						<span v-if="scope.row.is_tuijian == 1">是</span>
					</template>
				</el-table-column>
				<el-table-column prop="is_fahuo" label="是否需要发货" width="150">
				</el-table-column>
				<el-table-column prop="createtime" label="创建时间" width="160">
				</el-table-column>
				<el-table-column fixed='right' label="上下架">
					<template slot-scope="scope">
						<el-switch v-model="scope.row.status" @change="change(scope.row.id)" :active-value="openValue"
							:inactive-value="closeValue" active-color="#5cc477" inactive-color="#ff4949">
						</el-switch>
					</template>
				</el-table-column>
				<el-table-column prop="status" label="商品状态" fixed="right">
					<template slot-scope="scope">
						<span class="shop-admin1-page__status" v-if="scope.row.status === 1 ">上架</span>
						<span class="shop-admin1-page__status" v-if="scope.row.status === 2 ">下架</span>
					</template>
				</el-table-column>
				<el-table-column fixed="right" label="操作" width="180">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" @click="discuss(scope.$index, scope.row)">商品评价
						</el-button>
						<el-button size="mini" type="primary" :disabled="!isAuth('shopAdmin:update')"
							@click="shopUpdates(scope.$index, scope.row)">修改
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="shop-admin1-page__pagination">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
					layout="total,sizes, prev, pager, next" :total="tableData.total">
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
				// page:1,
				pagelist: [],
				openValue: 1,
				closeValue: 2,
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
				status: 'all',
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
						value: 'all',
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
				tableData: [],
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
			page() {
				this.pagelist = this.$store.state.common.pagelist;
				var page = 1;
				for (let val of this.pagelist) {
					if (val.router == "shopAdmin") {
						page = val.page;
					}
				}
				return page;
			}
		},
		methods: {
			// 上下架
			change(id) {
				this.$http({
					url: this.$http.adornUrl2('/goods/updateStatus'),
					method: 'get',
					params: this.$http.adornParams({
						'goodsId': id
					})
				}).then(({
					data
				}) => {
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
					})
				})
			},
			// 刷新
			refresh() {
				this.shopSelect()
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
				// this.page = val;
				for (let item of this.pagelist) {
					if (item.router == "shopAdmin") {
						item.page = val;
					}
				}
				this.$store.commit("common/updataPage", this.pagelist);
				this.shopSelect()
			},
			//搜索
			select() {
				// this.page = 1
				for (let item of this.pagelist) {
					if (item.router == "shopAdmin") {
						item.page = 1;
					}
				}
				this.$store.commit("common/updataPage", this.pagelist);
				this.size = 10
				this.shopSelect()
			},
			// 重置
			cleans() {
				this.title = ''
				this.status = 'all'
				this.typeId = ''
				this.value = []
				this.shopSelect()
			},
			//发布商品
			addShop() {
				this.$router.push({
					path: '/shopPublish'
				})
			},
			// 修改商品
			shopUpdates(index, rows) {
				this.$router.push({
					path: '/shopAmend',
					query: {
						id: rows.id
					}
				});
			},
			// 商品评价跳转
			discuss(index, rows) {
				this.$router.push({
					path: '/discuss',
					query: {
						goodsId: rows.id
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
						url: this.$http.adornUrl2('/goods/delete'),
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
					url: this.$http.adornUrl2('/admin/goods/goods_list'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'num': this.size,
						'search_title': this.title,
						'search_goods_cate': this.typeId,
						'status': this.status
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData
				})
			},
			// 商品分类
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2('/admin/goods/goods_cate'),
					method: 'get',
					params: this.$http.adornParams({})
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
				this.$router.back()
			},
		},
		mounted() {
			this.title = this.$route.query.title
			console.log('标题：', this.title)
			this.shopSelect()
			this.dataSelect()
		},
		watch: {
			'$route': 'title',
			'$route': 'shopSelect'
		}
	};
</script>

<style scoped>
.shop-admin1-page__back {
	margin-bottom: 12px;
}

.shop-admin1-page__back-link {
	display: inline-flex;
	align-items: center;
	font-size: 14px;
	color: #606266;
	text-decoration: none;
}

.shop-admin1-page__back-link:hover {
	color: #5cc477;
}

.shop-admin1-page__back-icon {
	width: 1.2em;
	height: 1.2em;
	margin-right: 4px;
}

.shop-admin1-page__filter-input {
	width: 200px;
}

.shop-admin1-page__filter-cascader {
	width: 150px;
}

.shop-admin1-page__filter-select {
	width: 150px;
}

.shop-admin1-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.shop-admin1-page__toolbar {
	display: flex;
	flex-wrap: wrap;
	align-items: center;
	gap: 12px;
	margin-bottom: 12px;
}

.shop-admin1-page__status {
	color: #5cc477;
	cursor: pointer;
}

.shop-admin1-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}
</style>
