<template>
	<div class="nf-page shop-admin-z-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">门店餐饮</span>
			<h2 class="nf-page-hero__title">商品管理</h2>
			<p class="nf-page-hero__desc">查看与管理外卖商品信息，支持按标题、类型、状态筛选查询，以及发布、修改与删除商品。</p>
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
						class="shop-admin-z-page__filter-input"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入商品标题"
						v-model="title">
					</el-input>
				</el-form-item>
				<el-form-item label="商品类型">
					<el-select
						class="shop-admin-z-page__filter-select"
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
						class="shop-admin-z-page__filter-select"
						v-model="status"
						placeholder="请选择商品状态"
						@change="select(status)">
						<el-option v-for="item in statusmain" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item class="shop-admin-z-page__filter-actions">
					<el-button type="primary" icon="el-icon-search" @click="select">查询</el-button>
					<el-button type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
				</el-form-item>
			</el-form>
		</div>

		<div class="nf-table-card">
			<div class="shop-admin-z-page__toolbar">
				<el-button type="primary" icon="el-icon-plus" :disabled="!isAuth('shopAdminZ:add')" @click="addShop">发布商品</el-button>
			</div>
			<el-table v-loading="tableDataLoading" :data="tableData.list">
				<el-table-column fixed prop="goodsId" label="编号" width="80">
				</el-table-column>
				<el-table-column fixed prop="goodsCover" label="商品封面图">
					<template slot-scope="scope">
						<img :src="scope.row.goodsCover" alt="" width="60" height="60">
					</template>
				</el-table-column>
				<el-table-column fixed prop="goodsCover" label="商品图" width="150">
					<template slot-scope="scope">
						<div v-for="(item,index) in scope.row.goodsPicture" :key="index"
							class="shop-admin-z-page__thumb">
							<el-popover placement="top-start" title="" trigger="hover">
								<img class="shop-admin-z-page__thumb-img" :src="item" alt="" slot="reference">
								<img class="shop-admin-z-page__preview-img" :src="item" alt="">
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
				<el-table-column prop="goodsMoney" label="售价">
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
						<span class="shop-admin-z-page__status" v-if="scope.row.status === 0 ">上架</span>
						<span class="shop-admin-z-page__status" v-if="scope.row.status === 1 ">下架</span>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160">
				</el-table-column>
				<el-table-column fixed="right" label="操作" width="150">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('shopAdminZ:update')"
							@click="shopUpdates(scope.$index, scope.row)">修改
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('shopAdminZ:delete')"
							@click="shopdeletes(scope.row)">
							删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="shop-admin-z-page__pagination">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10,  20, 30, 50, 100]" :page-size="size" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
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
			// page() {
			//   this.pagelist = this.$store.state.common.pagelist;
			//   var page = 1;
			//   for (let val of this.pagelist) {
			//     if (val.router == "shopAdmin") {
			//       page = val.page;
			//     }
			//   }
			//   return page;
			// },
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
				this.status = ''
				this.classifyId = ''
				this.shopSelect()
			},
			//发布商品
			addShop() {
				this.$router.push({
					path: '/shopPublishS',
					query: {
						shopId: 0
					}
				})
			},
			// 修改商品
			shopUpdates(index, rows) {
				this.$router.push({
					path: '/shopAmendS',
					query: {
						goodsId: rows.goodsId
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
						url: this.$http.adornUrl('admin/takeoutGoods/deleteGoodsById'),
						method: 'post',
						params: this.$http.adornParams({
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
			// 获取商品数据
			shopSelect() {
				let page = this.page
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/takeoutGoods/selectGoodsList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': page,
						'limit': this.size,
						'goodsName': this.title,
						'classifyId': this.classifyId,
						'status': this.status
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
			// 商品分类
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/takeoutGoods/selectAllClassify'),
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
.shop-admin-z-page__filter-input {
	width: 150px;
}

.shop-admin-z-page__filter-select {
	width: 200px;
}

.shop-admin-z-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.shop-admin-z-page__toolbar {
	display: flex;
	flex-wrap: wrap;
	align-items: center;
	gap: 12px;
	margin-bottom: 12px;
}

.shop-admin-z-page__thumb {
	display: inline-block;
	margin: 3px;
}

.shop-admin-z-page__thumb-img {
	width: 50px;
	height: 50px;
}

.shop-admin-z-page__preview-img {
	width: 300px;
	height: auto;
}

.shop-admin-z-page__status {
	color: #5cc477;
	cursor: pointer;
}

.shop-admin-z-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

.shop-admin-z-page .nf-table-card .el-button + .el-button {
	margin-left: 0;
	margin-top: 5px;
}
</style>
