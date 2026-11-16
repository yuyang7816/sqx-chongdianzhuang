<template>
	<div class="nf-page dispose-order-zy-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">自营商城</span>
			<h2 class="nf-page-hero__title">待处理订单</h2>
			<p class="nf-page-hero__desc">处理待发货与待退款订单，支持按订单号、时间范围筛选，可查询、重置、导出及发货、退款操作。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">
				<i class="el-icon-search nf-filter-card__head-icon"></i>
				<span>筛选条件</span>
			</div>
			<el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
				<el-form-item v-if="activeName === 'first'" label="订单号">
					<el-input
						class="dispose-order-zy-page__filter-input"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入订单号"
						v-model="orderNum">
					</el-input>
				</el-form-item>
				<el-form-item label="开始时间">
					<el-date-picker
						class="dispose-order-zy-page__filter-date"
						v-model="startTime"
						align="right"
						type="datetime"
						format="yyyy-MM-dd"
						value-format="yyyy-MM-dd"
						placeholder="选择开始时间">
					</el-date-picker>
				</el-form-item>
				<el-form-item label="截止时间">
					<el-date-picker
						class="dispose-order-zy-page__filter-date"
						v-model="endTime"
						align="right"
						type="datetime"
						format="yyyy-MM-dd"
						value-format="yyyy-MM-dd"
						placeholder="选择截止时间">
					</el-date-picker>
				</el-form-item>
				<el-form-item class="dispose-order-zy-page__filter-actions">
					<el-button type="primary" icon="el-icon-search" @click="select">查询</el-button>
					<el-button type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
					<el-button type="primary" icon="el-icon-download" @click="exportBtn">导出</el-button>
				</el-form-item>
			</el-form>
		</div>

		<el-tabs v-model="activeName" @tab-click="handleClick" class="dispose-order-zy-page__tabs">
			<el-tab-pane label="待发货订单" name="first">
				<div class="nf-table-card">
					<el-table v-loading="tableDataLoading" :data="tableData">
						<el-table-column fixed prop="img" label="商品图片">
							<template slot-scope="scope">
								<img :src="scope.row.img" alt="" width="60" height="60">
							</template>
						</el-table-column>
						<el-table-column prop="title" label="商品标题" width="200">
						</el-table-column>
						<el-table-column prop="detailJson" label="规格" width="200">
						</el-table-column>
						<el-table-column prop="goodsType.name" label="商品类型">
						</el-table-column>
						<el-table-column prop="price" label="商品价格">
							<template slot-scope="scope">
								<span class="dispose-order-zy-page__price">{{scope.row.price | numFilter}}</span>
							</template>
						</el-table-column>
						<el-table-column prop="number" label="商品个数">
						</el-table-column>
						<el-table-column prop="descrition" label="订单备注">
							<template slot-scope="scope">
								<span>{{scope.row.descrition ? scope.row.descrition : '未填写'}}</span>
							</template>
						</el-table-column>
						<el-table-column prop="orderNum" label="订单号" width="200">
							<template slot-scope="scope">
								<span>{{scope.row.orderNum}}</span>
							</template>
						</el-table-column>
						<el-table-column prop="consignee" label="收货人" width="120">
						</el-table-column>
						<el-table-column prop="mobile" label="手机号" width="120">
						</el-table-column>
						<el-table-column prop="detail" label="详细地址" width="120">
							<template slot-scope="scope">
								<span>{{scope.row.provinces}}{{scope.row.detail}}</span>
							</template>
						</el-table-column>
						<el-table-column prop="payMoney" label="支付金额" width="120">
							<template slot-scope="scope">
								<span class="dispose-order-zy-page__price">{{scope.row.payMoney | numFilter}}</span>
							</template>
						</el-table-column>
						<el-table-column prop="payWay" label="支付方式">
							<template slot-scope="scope">
								<span class="text_color" v-if="scope.row.payWay === 1 ">app微信</span>
								<span class="text_color" v-if="scope.row.payWay === 2 ">微信公众号</span>
								<span class="text_color" v-if="scope.row.payWay === 3 ">微信小程序</span>
								<span class="text_color" v-if="scope.row.payWay === 4 ">app支付宝</span>
								<span class="text_color" v-if="scope.row.payWay === 5 ">H5支付宝</span>
								<span class="text_color" v-if="scope.row.payWay === 6 ">零钱</span>
								<span class="text_color" v-if="scope.row.payWay === 7 ">积分</span>
								<span class="text_color" v-if="scope.row.payWay === null ">未支付</span>
							</template>
						</el-table-column>
						<el-table-column fixed="right" prop="status" label="订单状态">
							<template slot-scope="scope">
								<span class="text_color" v-if="scope.row.status === 2 ">待发货 </span>
							</template>
						</el-table-column>
						<el-table-column prop="createAt" label="创建时间" width="160">
						</el-table-column>
						<el-table-column prop="payTime" label="付款时间" width="160">
							<template slot-scope="scope">
								<span>{{scope.row.payTime ? scope.row.payTime : '未付款'}}</span>
							</template>
						</el-table-column>
						<el-table-column fixed="right" label="操作" width="150">
							<template slot-scope="scope">
								<el-button size="mini" type="danger" :disabled="!isAuth('orderAdminZy:refund')" style="margin: 3px;"
									@click="refundClick(scope.row)">退款
								</el-button>
								<el-button size="mini" type="primary" :disabled="!isAuth('orderAdminZy:deliver')" style="margin: 3px;"
									@click="colonelClick(scope.row)">发货
								</el-button>
								<el-button size="mini" type="primary" @click="orderDetails(scope.row.id)" style="margin: 3px;">详情
								</el-button>
							</template>
						</el-table-column>
					</el-table>
					<div class="dispose-order-zy-page__pagination">
						<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
							:page-sizes="[5, 10, 15, 20]" :page-size="size" :current-page="page"
							layout="total,sizes, prev, pager, next" :total="totalnum">
						</el-pagination>
					</div>
				</div>
			</el-tab-pane>
			<el-tab-pane label="待退款订单" name="second">
				<div class="nf-table-card">
					<el-table v-loading="tableDataLoading" :data="tableData">
						<el-table-column fixed prop="img" label="商品图片">
							<template slot-scope="scope">
								<img :src="scope.row.img" alt="" width="60" height="60">
							</template>
						</el-table-column>
						<el-table-column prop="title" label="商品标题" width="200">
						</el-table-column>
						<el-table-column prop="goodsType.name" label="商品类型">
						</el-table-column>
						<el-table-column prop="price" label="商品价格">
							<template slot-scope="scope">
								<span class="dispose-order-zy-page__price">{{scope.row.price | numFilter}}</span>
							</template>
						</el-table-column>
						<el-table-column prop="number" label="商品个数">
						</el-table-column>
						<el-table-column prop="descrition" label="订单备注">
							<template slot-scope="scope">
								<span>{{scope.row.descrition ? scope.row.descrition : '未填写'}}</span>
							</template>
						</el-table-column>
						<el-table-column prop="orderNum" label="订单号" width="200">
							<template slot-scope="scope">
								<span>{{scope.row.orderNum}}</span>
							</template>
						</el-table-column>
						<el-table-column prop="consignee" label="收货人" width="120">
						</el-table-column>
						<el-table-column prop="mobile" label="手机号" width="120">
						</el-table-column>
						<el-table-column prop="detail" label="详细地址" width="120">
							<template slot-scope="scope">
								<span>{{scope.row.provinces}}{{scope.row.detail}}</span>
							</template>
						</el-table-column>
						<el-table-column prop="payMoney" label="支付金额" width="120">
							<template slot-scope="scope">
								<span class="dispose-order-zy-page__price">{{scope.row.payMoney | numFilter}}</span>
							</template>
						</el-table-column>
						<el-table-column prop="refund" label="退款原因" width="200">
						</el-table-column>
						<el-table-column prop="payWay" label="支付方式">
							<template slot-scope="scope">
								<span class="text_color" v-if="scope.row.payWay === 1 ">app微信</span>
								<span class="text_color" v-if="scope.row.payWay === 2 ">微信公众号</span>
								<span class="text_color" v-if="scope.row.payWay === 3 ">微信小程序</span>
								<span class="text_color" v-if="scope.row.payWay === 4 ">app支付宝</span>
								<span class="text_color" v-if="scope.row.payWay === 5 ">H5支付宝</span>
								<span class="text_color" v-if="scope.row.payWay === 6 ">零钱</span>
								<span class="text_color" v-if="scope.row.payWay === 7 ">积分</span>
								<span class="text_color" v-if="scope.row.payWay === null ">未支付</span>
							</template>
						</el-table-column>
						<el-table-column fixed="right" prop="status" label="订单状态">
							<template slot-scope="scope">
								<span class="text_color" v-if="scope.row.status === 1 ">待付款</span>
								<span class="text_color" v-if="scope.row.status === 2 ">待发货 </span>
								<span class="text_color" v-if="scope.row.status === 3 ">已发货 </span>
								<span class="text_color" v-if="scope.row.status === 4 ">已收货</span>
								<span class="text_color" v-if="scope.row.status === 5 ">已取消</span>
								<span class="text_color" v-if="scope.row.status === 6 ">退款中</span>
								<span class="text_color" v-if="scope.row.status === 7 ">已退款</span>
							</template>
						</el-table-column>
						<el-table-column prop="createAt" label="创建时间" width="160">
						</el-table-column>
						<el-table-column prop="payTime" label="付款时间" width="160">
							<template slot-scope="scope">
								<span>{{scope.row.payTime ? scope.row.payTime : '未付款'}}</span>
							</template>
						</el-table-column>
						<el-table-column fixed="right" label="操作" width="280">
							<template slot-scope="scope">
								<el-button size="mini" type="primary" :disabled="!isAuth('orderAdminZy:refund')"
									@click="refundClick(scope.row)">确定退款
								</el-button>
								<el-button size="mini" type="primary" @click="refuseClick(scope.row)">拒绝退款
								</el-button>
								<el-button size="mini" type="primary" @click="orderDetails(scope.row.id)">详情
								</el-button>
							</template>
						</el-table-column>
					</el-table>
					<div class="dispose-order-zy-page__pagination">
						<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
							:page-sizes="[5, 10, 15, 20]" :page-size="size" :current-page="page"
							layout="total,sizes, prev, pager, next" :total="totalnum">
						</el-pagination>
					</div>
				</div>
			</el-tab-pane>
		</el-tabs>

		<!-- 发货弹框 -->
		<el-dialog title="发货" :visible.sync="dialogFormVisible" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">快递公司：</span>
				<el-input style="width:50%;" v-model="expressName" placeholder="请输入快递公司"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">快递单号：</span>
				<el-input style="width: 50%;" v-model="expressNumber" placeholder="请输入快递单号"></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="deliverNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 拒绝退款 -->
		<el-dialog title="拒绝退款" :visible.sync="dialogFormVisible1" center>
			<div style="margin-bottom: 10px;">
				<span
					style="width: 200px;display: inline-block;text-align: right;position: relative;top: -65px;">拒绝退款理由：</span>
				<el-input style="width:50%;" type="textarea" rows="4" v-model="refusedRefund"
					placeholder="请输入拒绝退款理由"></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible1 = false">取 消</el-button>
				<el-button type="primary" @click="refuseNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>
<script>
	export default {
		data() {
			return {
				size: 5,
				page: 1,
				id: '',
				orderNum: '',
				status: 2,
				sumincome: '',
				totalnum: 0,
				income: '',
				wxincome: '',
				zfbincome: '',
				yueicome: '',
				ordersum0: '',
				ordersum1: '',
				ordersum2: '',
				expressName: '',
				expressNumber: '',
				refusedRefund: '',
				isExpress: '',
				formLabelWidth: '200px',
				activeName: 'first',
				tableDataLoading: false,
				dialogFormVisible: false,
				dialogFormVisible1: false,
				tableData: [],
				info: {
					stockDate: this.getNowTime(), //日期
				},
				info2: {
					stockDate2: this.getNowTime2(), //日期
				},
				startTime: '',
				endTime: '',
			}
		},
		filters: {
			numFilter(value) {
				let realVal = ''
				if (!isNaN(value) && value !== '') {
					// 截取当前数据到小数点后两位
					realVal = parseFloat(value).toFixed(2)
				} else {
					realVal = '--'
				}
				return realVal
			}
		},
		methods: {
			//处理默认选中当前日期
			getNowTime() {
				var now = new Date()
				var year = now.getFullYear() //得到年份
				var month = now.getMonth() //得到月份
				var date = now.getDate() //得到日期
				month = month + 1
				month = month.toString().padStart(2, '0')
				date = date.toString().padStart(2, '0')
				var defaultDate = `${year}-${month}-${date}`
				return defaultDate
				this.$set(this.info, 'stockDate', defaultDate)
			},
			//处理默认选中当前日期
			getNowTime2() {
				var now = new Date()
				var year = now.getFullYear() //得到年份
				var month = now.getMonth() - now.getMonth() //得到月份
				var date = now.getDate() - now.getDate() + 1 //得到日期
				month = month + 1
				month = month.toString().padStart(2, '0')
				date = date.toString().padStart(2, '0')
				var defaultDate = `${year}-${month}-${date}`
				return defaultDate
				this.$set(this.info, 'stockDate', defaultDate)
			},
			// 发货
			colonelClick(rows) {
				if (rows.isExpress == 2) {
					this.$http({
						url: this.$http.adornUrl('shopOrders/expressVirtual'),
						method: 'get',
						params: this.$http.adornParams({
							'id': rows.id,
						})
					}).then(({
						data
					}) => {
						if (data.status == 0) {
							this.dialogFormVisible = false
							let returnData = data.data;
							this.$message({
								message: '操作成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
						} else {
							this.$message({
								message: data.msg,
								type: 'error',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
						}
					})
				} else {
					this.dialogFormVisible = true
					this.id = rows.id
				}
			},
			deliverNoticeTo() {
				if (this.expressName == '') {
					this.$message({
						message: '请输入快递公司',
						type: 'error',
						duration: 1500
					})
					return
				}
				if (this.expressNumber == '') {
					this.$message({
						message: '请输入快递单号',
						type: 'error',
						duration: 1500
					})
					return
				}
				this.$http({
					url: this.$http.adornUrl('shopOrders/express'),
					method: 'get',
					params: this.$http.adornParams({
						'id': this.id,
						'expressName': this.expressName,
						'expressNumber': this.expressNumber
					})
				}).then(({
					data
				}) => {
					if (data.status == 0) {
						this.dialogFormVisible = false
						let returnData = data.data;
						this.orderName = returnData.expressName
						this.orderNumber = returnData.expressNumber
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.expressName = ''
								this.expressNumber = ''
								this.dataSelect()
							}
						})
					}
				})
			},
			// 退款
			refundClick(row) {
				this.$confirm(`确定退款?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('shopOrders/refundMoney'),
						method: 'get',
						params: this.$http.adornParams({
							'ordersId': row.id
						})
					}).then(({
						data
					}) => {
						if (data.status == 0) {
							this.$message({
								message: '退款成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
						}else {
							this.$message({
								message: data.msg,
								type: 'error',
								duration: 1500,
								onClose: () => {
									// this.dataSelect()
								}
							})
						}
					})
				}).catch(() => {})
			},
			// 拒绝退款
			refuseClick(row) {
				this.ordersId = row.id
				this.dialogFormVisible1 = true
			},
			refuseNoticeTo() {
				if (this.refusedRefund == '') {
					this.$message({
						message: '请输入拒绝退款理由',
						type: 'error',
						duration: 1500,
						onClose: () => {
							this.refusedRefund = ''
							this.dataSelect()
						}
					})
				} else {
					this.$http({
						url: this.$http.adornUrl('shopOrders/refusedRefund'),
						method: 'get',
						params: this.$http.adornParams({
							'ordersId': this.ordersId,
							'refusedRefund': this.refusedRefund,
						})
					}).then(({
						data
					}) => {
						if(data.status == 0){
							this.dialogFormVisible1 = false
							this.$message({
								message: '操作成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.refusedRefund = ''
									this.dataSelect()
								}
							})
						}else {
							this.$message({
								message: data.msg,
								type: 'error',
								duration: 1500,
								onClose: () => {
								}
							})
						}
						
					})
				}

			},
			// 详情
			orderDetails(id) {
				this.$router.push({
					path: '/orderDetailsZy',
					query: {
						id: id
					}
				})
			},
			handleSizeChange(val) {
				this.size = val;
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.dataSelect()
			},
			handleClick(tab, event) {
				this.startTime = ''
				this.endTime = ''
				if (tab._props.label == '待发货订单') {
					this.page = 1
					this.size = 5
					this.status = 2
					this.dataSelect()
				}
				if (tab._props.label == '待退款订单') {
					this.page = 1
					this.size = 5
					this.status = 6
					this.dataSelect()
				}
			},
			//搜索
			select() {
				this.page = 1
				this.size = 5
				this.dataSelect()
			},
			// 重置
			cleans() {
				this.page = 1
				this.orderNum = ''
				this.startTime = ''
				this.endTime = ''
				this.dataSelect()
			},
			// 获取订单数据
			dataSelect() {
				let page = this.page - 1
				this.tableDataLoading = true
				var endTime = this.endTime
				if (this.endTime != '') {
					endTime = this.endTime + " 23:59:59"
				}
				this.$http({
					url: this.$http.adornUrl('shopOrders/list'),
					method: 'get',
					params: this.$http.adornParams({
						'page': page,
						'size': this.size,
						'orderNum': this.orderNum,
						'status': this.status,
						'startTime': this.startTime,
						'endTime': endTime
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData.content
					this.totalnum = returnData.totalElements
				})
			},
			// 导出
			exportBtn() {
				// if (this.endTime == '') {
				// 	this.endTime = this.info.stockDate
				// }
				// if (this.startTime == '') {
				// 	this.startTime = this.info2.stockDate2
				// }
				var endTime = this.endTime
				if (this.endTime != '') {
					endTime = this.endTime + " 23:59:59"
				}
				this.$http({
					url: this.$http.adornUrl('shopOrders/listExcel'),
					method: 'get',
					responseType: 'blob',
					params: this.$http.adornParams({
						'orderNum': this.orderNum,
						'status': this.status,
						'startTime': this.startTime,
						'endTime': endTime
			
					})
				}).then(({
					data
				}) => {
					let blob = new Blob([data], {
						type: 'application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
					})
					if (window.navigator.msSaveOrOpenBlob) {
						navigator.msSaveBlob(blob)
					} else {
						let url = window.URL.createObjectURL(blob)
						let elink = document.createElement('a')
						elink.download = '订单列表.xlsx'
						elink.style.display = 'none'
						elink.href = url
						document.body.appendChild(elink)
						elink.click()
						document.body.removeChild(elink)
					}
				})
			},
		},
		mounted() {
			this.dataSelect()
		}
	};
</script>
<style scoped>
.dispose-order-zy-page__filter-input {
	width: 150px;
}

.dispose-order-zy-page__filter-date {
	width: 160px;
}

.dispose-order-zy-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.dispose-order-zy-page__tabs {
	margin-bottom: 0;
}

.dispose-order-zy-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.dispose-order-zy-page__tabs .nf-table-card {
	margin-bottom: 0;
}

.dispose-order-zy-page__tabs >>> .el-tabs__item.is-active {
	color: #5cc477;
}

.dispose-order-zy-page__tabs >>> .el-tabs__active-bar {
	background-color: #5cc477;
}

.dispose-order-zy-page__price {
	color: #f56c6c;
}

.dispose-order-zy-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

.text_color {
	color: #5cc477;
}

.text_color span {
	margin-right: 5px;
}

.nf-table-card .el-button + .el-button {
	margin-left: 0 !important;
	margin-top: 5px !important;
}
</style>
