<template>
	<div class="nf-page shops-order-sm-page">
		<div class="shops-order-sm-page__back">
			<a href="#" class="shops-order-sm-page__back-link" @click.prevent="prev">
				<icon-svg name="jiantou" class="shops-order-sm-page__back-icon"></icon-svg>
				返回
			</a>
		</div>

		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">商品管理</span>
			<h2 class="nf-page-hero__title">门店订单</h2>
			<p class="nf-page-hero__desc">查看当前门店全部订单，支持按状态、台球馆名称、订单号、手机号及昵称筛选，可查看订单商品、打印小票及修改订单状态与备注。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">
				<i class="el-icon-search nf-filter-card__head-icon"></i>
				<span>筛选条件</span>
			</div>
			<el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
				<el-form-item label="状态">
					<el-select
						class="shops-order-sm-page__filter-select"
						v-model="status"
						placeholder="请选择状态"
						@change="phoneSelect">
						<el-option v-for="item in taskstates" :key="item.value" :label="item.label"
							:value="item.value"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="台球馆名称">
					<el-input
						class="shops-order-sm-page__filter-input"
						@keydown.enter.native="phoneSelect"
						placeholder="请输入台球馆名称"
						v-model="tearoomName"></el-input>
				</el-form-item>
				<el-form-item label="订单号">
					<el-input
						class="shops-order-sm-page__filter-input"
						@keydown.enter.native="phoneSelect"
						placeholder="请输入订单号"
						v-model="orderNumber"></el-input>
				</el-form-item>
				<el-form-item label="手机号">
					<el-input
						class="shops-order-sm-page__filter-input"
						@keydown.enter.native="phoneSelect"
						placeholder="请输入手机号"
						v-model="phone"></el-input>
				</el-form-item>
				<el-form-item label="昵称">
					<el-input
						class="shops-order-sm-page__filter-input"
						@keydown.enter.native="phoneSelect"
						placeholder="请输入昵称"
						v-model="userName"></el-input>
				</el-form-item>
				<el-form-item>
					<el-button type="primary" icon="el-icon-search" @click="phoneSelect">查询</el-button>
					<el-button type="primary" icon="el-icon-refresh-left" @click="cleans2">重置</el-button>
				</el-form-item>
			</el-form>
		</div>

		<el-tabs v-model="activeName" @tab-click="handleClick" class="shops-order-sm-page__tabs">
			<el-tab-pane label="全部订单" name="first">
				<div class="nf-table-card">
					<el-table v-loading="tableDataLoading" :data="tableData2.list">
						<el-table-column fixed prop="orderId" label="编号" width="80"></el-table-column>
						<el-table-column fixed prop="userName" label="下单用户">
							<template slot-scope="scope">
								<span class="shops-order-sm-page__link" @click="updates(scope.row)">
									{{ scope.row.userName ? scope.row.userName : '未绑定' }}
								</span>
							</template>
						</el-table-column>
						<el-table-column prop="phone" label="手机号"></el-table-column>
						<el-table-column prop="tearoomName" label="台球馆名称"></el-table-column>
						<el-table-column prop="goodsName" label="桌台名称"></el-table-column>
						<el-table-column prop="orderNumber" label="订单号" width="180"></el-table-column>
						<el-table-column prop="orderGoodsList" label="内容" width="300">
							<template slot-scope="scope">
								<el-popover placement="top-start" title="" trigger="hover">
									<div style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;"
										slot="reference">
										<div v-for="(item, index) in scope.row.orderGoodsList" :key="index">
											<span>{{ index + 1 }}.
												【商铺：{{item.shopName}}】商品名：{{ item.goodsName }},</span>
											<span>单价：{{item.goodsPrice}}*{{ item.goodsNum }}; </span>
											<span>规格：{{ item.skuMessage }}; </span>
											<span>打包费：{{ item.goodsPack }}; </span>
											<span v-if="item.goodRemarks">备注：{{ item.goodRemarks }}</span>
										</div>
									</div>
									<div style="width: 500px; height: auto;">
										<div v-for="(item, index) in scope.row.orderGoodsList" :key="index">
											<span>{{ index + 1 }}.
												【商铺：{{item.shopName}}】商品名：{{ item.goodsName }},</span>
											<span>单价：{{item.goodsPrice}}*{{ item.goodsNum }}; </span>
											<span>规格：{{ item.skuMessage }}; </span>
											<span>打包费：{{ item.goodsPack }}; </span>
											<span v-if="item.goodRemarks">备注：{{ item.goodRemarks }}</span>
										</div>
									</div>
								</el-popover>
							</template>
						</el-table-column>
						<el-table-column prop="orderRemarks" label="订单备注" width="200">
							<template slot-scope="scope">
								{{scope.row.orderRemarks}}
								<el-button type="primary" size="mini" plain :disabled="!isAuth('missionSm:update')"
									@click="selectGgBtn(scope.row)">修改</el-button>
							</template>
						</el-table-column>
						<el-table-column prop="orderMoney" label="商品总价" width="100">
						</el-table-column>
						<el-table-column prop="vipMoney" label="优惠金额" width="100">
							<template slot-scope="scope">
								<span>{{ scope.row.vipMoney ? scope.row.vipMoney : '0' }}</span>
							</template>
						</el-table-column>
						<el-table-column fixed="right" prop="status" label="状态" width="100">
							<template slot-scope="scope">
								<span class="shops-order-sm-page__tag" v-if="scope.row.status === 2">已下单</span>
								<span class="shops-order-sm-page__done" v-if="scope.row.status === 4">已完成</span>
								<span class="shops-order-sm-page__muted" v-if="scope.row.status === 5">已取消</span>
							</template>
						</el-table-column>
						<el-table-column prop="createTime" label="创建时间">
						</el-table-column>
						<el-table-column fixed="right" prop="status" label="操作" width="120">
							<template slot-scope="scope">
								<el-button type="primary" @click="orderGoodsBtn(scope.row)" plain size="mini"
									style="margin: 3px;">订单商品</el-button>
								<el-button type="primary" @click="dayin(scope.row)" style="margin: 3px;" size="mini">打印小票</el-button>
								<el-button type="danger" :disabled="!isAuth('missionSm:update')" @click="updateStatusBtn(scope.row)" style="margin: 3px;" size="mini">修改状态</el-button>
							</template>
						</el-table-column>
					</el-table>
					<div class="shops-order-sm-page__summary" v-if="totalMoney">本页累计交易统计：{{totalMoney}}元</div>
					<div class="shops-order-sm-page__pagination">
						<el-pagination @size-change="handleSizeChange3" @current-change="handleCurrentChange3"
							:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
							layout="total,sizes, prev, pager, next" :total="tableData2.totalCount">
						</el-pagination>
					</div>
				</div>
			</el-tab-pane>
		</el-tabs>

		<!-- 订单商品列表 -->
		<el-dialog title="订单商品列表" :visible.sync="shopOrderGoods" width="80%">
			<el-table :data="orderGoodsList.orderGoodsList" style="width: 100%">
				<el-table-column prop="id" label="编号" width="80px">
				</el-table-column>
				<el-table-column prop="shopName" label="店铺名称">
				</el-table-column>
				<el-table-column prop="goodsName" label="商品名称">
				</el-table-column>
				<el-table-column prop="goodsCover" label="商品图片" width="120px">
					<template slot-scope="scope">
						<img :src="scope.row.goodsCover" style="width: 100px;height: 100px;" alt=""
							srcset="">
					</template>
				</el-table-column>
				<el-table-column prop="skuMessage" label="商品规格">
				</el-table-column>
				<el-table-column prop="goodsPrice" label="商品价格">
				</el-table-column>
				<el-table-column prop="goodsPack" label="打包费" width="100"></el-table-column>
				<el-table-column prop="goodsNum" label="商品数量">
					<template slot-scope="scope">
						{{scope.row.goodsNum}}
						<el-button type="danger" :disabled="!isAuth('missionSm:update')" @click="updateGoodsNum(scope.row)" size="mini" plain>修改</el-button>
					</template>
				</el-table-column>
				<el-table-column prop="goodRemarks" label="商品备注">
				</el-table-column>
				<el-table-column prop="goodsPicture" label="操作">
					<template slot-scope="scope">
						<el-button type="warning" @click="oncetk(scope.row)">删除</el-button>
					</template>
				</el-table-column>
			</el-table>
			<span slot="footer" class="dialog-footer">
				<el-button @click="shopOrderGoods = false">取 消</el-button>
				<el-button type="primary" @click="shopOrderGoods = false">确 定</el-button>
			</span>
		</el-dialog>
		<!-- 订单备注 -->
		<el-dialog title="订单备注" :visible.sync="dialogVisibleGgBz" width="50%">

			<div style="margin-bottom: 10px;">
				<span
					style="width: 200px;display: inline-block;text-align: right;position: relative;top: -70px;">备注：</span>
				<el-input style="width:50%;" v-model="content" type="textarea" :rows="4" placeholder="请输入备注"></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogVisibleGgBz = false">取 消</el-button>
				<el-button type="primary" @click="selectGg()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 订单商品数量 -->
		<el-dialog title="订单商品数量" :visible.sync="shopOrderGoodsUp" width="60%">
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">修改类型：</span>
				<el-radio-group v-model="type">
					<el-radio :label="1">添加</el-radio>
					<el-radio :label="2">减少</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">数量：</span>
				<el-input style="width:50%;" v-model="num" type="number" :min="0" placeholder="请输入数量"></el-input>
			</div>
			<span slot="footer" class="dialog-footer">
				<el-button @click="shopOrderGoodsUp = false">取 消</el-button>
				<el-button type="primary" @click="goodsNumBtn()">确 定</el-button>
			</span>
		</el-dialog>
		<!-- 修改状态 -->
		<el-dialog title="修改状态" :visible.sync="shopOrderStatus" width="60%" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">状态：</span>
				<el-radio-group v-model="statusUp">
					<el-radio :label="2">已下单</el-radio>
					<el-radio :label="4">已完成</el-radio>
					<el-radio :label="5">已取消</el-radio>
				</el-radio-group>
			</div>
			<span slot="footer" class="dialog-footer">
				<el-button @click="shopOrderStatus = false">取 消</el-button>
				<el-button type="primary" @click="StatusBtn()">确 定</el-button>
			</span>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				
				limit: 10,
				page: 1,
				totalMoney: 0,
				phone: '',
				status: '',
				describes: '',
				taskstates: [{
						label: '全部',
						value: ''
					},
					{
						label: '已下单',
						value: 2
					},
					
					{
						label: '已完成',
						value: 4
					},
					{
						label: '已取消',
						value: 5
					}
				],
				orderType: '',
				shopId: '',
				activeName: 'first',
				tableDataLoading: false,
				tableData2: {},
				userName: '',
				orderNumber: '',
				tearoomName:'',
				dialogVisibleGgBz: false,
				content: '',
				shopOrderGoods: false,
				orderId: '',
				shopOrderGoodsUp: false,
				type: 1,
				num: '',
				shopOrderStatus:false,
				statusUp:'',
				orderGoodsList: {},
			}
		},
		methods: {
			// 详情跳转
			updates(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
					}
				})
			},
			
			
			handleSizeChange3(val) {
				this.limit = val
				this.taskdataSelect()
			},
			handleCurrentChange3(val) {
				this.page = val
				this.taskdataSelect()
			},
			// tabs切换
			handleClick(tab, event) {
				if (tab._props.label == '全部订单') {
					this.page = 1
					this.limit = 10
					this.status = ''
					this.taskdataSelect()
				}
				
			},
			// 查询
			phoneSelect() {
				this.page = 1
				this.limit = 10
				this.taskdataSelect()
			},
			// 重置
			cleans2() {
				this.phone = ''
				this.status = ''
				this.userName = ''
				this.orderNumber = ''
				this.tearoomName = ''
				this.orderType = ''
				this.page = 1
				this.taskdataSelect()
			},
			
			// 获取接单数据列表
			taskdataSelect() {
				this.shopId = this.$route.query.shopId
				this.tableDataLoading = true
				this.totalMoney = 0
				this.$http({
					url: this.$http.adornUrl('admin/order/selectAllOrder'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'phone': this.phone,
						'userName': this.userName,
						'orderNumber': this.orderNumber,
						'status': this.status,
						'shopId': this.shopId,
						'orderType': this.orderType,
						'tearoomName':this.tearoomName,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.tableData2 = returnData
					for (var i in this.tableData2.list) {
						this.tableData2.list[i].goodsPriceZ = 0
						this.totalMoney = this.totalMoney + this.tableData2.list[i].payMoney
						if (this.tableData2.list[i].address) {
							this.tableData2.list[i].address = JSON.parse(this.tableData2.list[i].address)
						}
						for (var j in this.tableData2.list[i].orderGoodsList) {
							this.tableData2.list[i].orderGoodsList[j].goodsPriceZ = (this.tableData2.list[i]
								.orderGoodsList[j]
								.goodsPrice * this.tableData2.list[i].orderGoodsList[j].goodsNum).toFixed(2)
							this.tableData2.list[i].goodsPriceZ = (Number(this.tableData2.list[i].goodsPriceZ) +
								Number(this
									.tableData2.list[i]
									.orderGoodsList[j].goodsPriceZ)).toFixed(2)
						}
					}
					this.totalMoney = this.totalMoney.toFixed(2)
				})
			},
			
			// 返回上一级
			prev() {
				this.$router.back()
			},
			// 修改备注弹框
			selectGgBtn(info) {
				this.orderId = info.orderId
				this.content = ''
				this.dialogVisibleGgBz = true
			
			},
			selectGg(info) {
				let datas = {
					orderId: this.orderId,
					orderRemarks: this.content,
				}
				this.$confirm(`确认修改该订单的备注吗？`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/order/updateOrderRemarks'),
						method: 'post',
						// data: this.$http.adornData(datas)
						params: this.$http.adornParams(datas)
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '操作成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dialogVisibleGgBz = false
									//添加成功后刷新数据
									this.taskdataSelect()
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
				}).catch(() => {
					this.getorderInfo()
					console.log('取消')
				})
			
			},
			// 打印小票
			dayin(row) {
				this.$http({
					url: this.$http.adornUrl('admin/order/print'),
					method: 'post',
					params: this.$http.adornParams({
						'orderId': row.orderId
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
								this.taskdataSelect()
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
			// 订单商品弹框
			orderGoodsBtn(row) {
				this.orderId = row.orderId
				this.ordersSelect()
				this.shopOrderGoods = true
			},
			ordersSelect() {
				this.$http({
					url: this.$http.adornUrl('admin/order/selectOrderById'),
					method: 'get',
					params: this.$http.adornParams({
						'orderId': this.orderId,
						'shopId': this.shopId,
					})
				}).then(({
					data
				}) => {
					let returnData = data.data
					this.orderGoodsList = returnData
				})
			},
			
			updateGoodsNum(row) {
				this.shopId = row.shopId
				this.orderGoodsId = row.id
				this.type = 1
				this.num = ''
				this.shopOrderGoodsUp = true
			},
			// 修改订单商品数量 
			goodsNumBtn(row) {
				if (this.num === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '数量不能为空',
						type: 'warning'
					})
					return
				}
				this.$http({
					url: this.$http.adornUrl('admin/order/updateGoodsNum'),
					method: 'get',
					// data: this.$http.adornData(datas)
					params: this.$http.adornParams({
						'shopId': this.shopId,
						'orderGoodsId': this.orderGoodsId,
						'type': this.type,
						'num': this.num,
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
								this.shopOrderGoodsUp = false
								//添加成功后刷新数据
								this.ordersSelect()
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
			},
			//删除订单商品
			oncetk(row) {
				this.$confirm('确定删除该商品？', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/order/updateGoodsNum'),
						method: 'get',
						params: this.$http.adornParams({
							'shopId': row.shopId,
							'orderGoodsId': row.id,
							'type': 2,
							'num': row.goodsNum,
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								type: 'success',
								message: '操作成功!',
								duration: 1500,
								onClose: () => {
									this.ordersSelect()
								}
							});
						} else {
							this.$message({
								message: data.msg,
								type: 'error',
							})
						}
					})
			
				}).catch(() => {
					this.$message({
						type: 'info',
						message: '已取消'
					});
				});
			},
			// 修改状态弹框
			updateStatusBtn(row){
				this.orderId = row.orderId
				this.statusUp = ''
				this.shopOrderStatus = true
			},
			StatusBtn(){
				if (this.statusUp === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择修改状态',
						type: 'warning'
					})
					return
				}
				this.$http({
					url: this.$http.adornUrl('admin/order/updateOrder'),
					method: 'post',
					 data: this.$http.adornData({
						'orderId': this.orderId,
						'status': this.statusUp,
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
								this.shopOrderStatus = false
								//添加成功后刷新数据
								this.taskdataSelect()
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
			},
		},
		mounted() {
			this.shopId = this.$route.query.shopId
			this.taskdataSelect()

		},
		watch: {
			'$route': 'taskdataSelect'
		}
	}
</script>

<style scoped>
.shops-order-sm-page__back {
	margin-bottom: 12px;
}

.shops-order-sm-page__back-link {
	display: inline-flex;
	align-items: center;
	text-decoration: none;
	font-size: 14px;
	color: #606266;
}

.shops-order-sm-page__back-link:hover {
	color: #5cc477;
}

.shops-order-sm-page__back-icon {
	width: 1.2em;
	height: 1.2em;
	margin-right: 4px;
}

.shops-order-sm-page__filter-select {
	width: 150px;
}

.shops-order-sm-page__filter-input {
	width: 200px;
}

.shops-order-sm-page__tabs {
	margin-bottom: 0;
}

.shops-order-sm-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.shops-order-sm-page__tabs .nf-table-card {
	margin-bottom: 0;
}

.shops-order-sm-page__tabs >>> .el-tabs__item.is-active {
	color: #5cc477;
}

.shops-order-sm-page__tabs >>> .el-tabs__active-bar {
	background-color: #5cc477;
}

.shops-order-sm-page__link {
	color: #5cc477;
	cursor: pointer;
}

.shops-order-sm-page__link:hover {
	color: #3fae5c;
}

.shops-order-sm-page__tag {
	color: #5cc477;
}

.shops-order-sm-page__done {
	color: #f56c6c;
}

.shops-order-sm-page__muted {
	color: #999999;
}

.shops-order-sm-page__summary {
	color: #b94a48;
	font-size: 16px;
	font-weight: 500;
	padding-top: 12px;
}

.shops-order-sm-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

.shops-order-sm-page .nf-table-card .el-button + .el-button {
	margin-left: 0;
	margin-top: 5px;
}
</style>
