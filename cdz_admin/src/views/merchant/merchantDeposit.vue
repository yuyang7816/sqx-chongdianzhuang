<template>
	<div class="nf-page merchant-deposit-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">商家管理</span>
			<h2 class="nf-page-hero__title">保证金</h2>
			<p class="nf-page-hero__desc">查看保证金缴纳统计与店铺明细，支持按时间维度筛选统计、按店铺名称查询，以及保证金退款审核处理。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<el-tabs v-model="activeName" @tab-click="handleClick" class="merchant-deposit-page__tabs">
			<el-tab-pane label="保证金信息" name="first">
				<div class="nf-filter-card">
					<div class="nf-filter-card__head">
						<i class="el-icon-date nf-filter-card__head-icon"></i>
						<span>统计筛选</span>
					</div>
					<div class="merchant-deposit-page__stat-filter">
						<el-select v-model="way" class="merchant-deposit-page__filter-select" @change="orderfenxi">
							<el-option v-for="item in ways" :key="item.value" :label="item.label" :value="item.value">
							</el-option>
						</el-select>
						<el-date-picker
							class="merchant-deposit-page__filter-date"
							v-model="info.stockDate"
							align="right"
							type="datetime"
							format="yyyy-MM-dd"
							value-format="yyyy-MM-dd"
							placeholder="选择开始时间"
							@change="animeOrder">
						</el-date-picker>
					</div>
				</div>

				<div class="merchant-deposit-page__kpi-grid">
					<div class="merchant-deposit-page__kpi-card">
						<div class="merchant-deposit-page__kpi-label">累计收入</div>
						<div class="merchant-deposit-page__kpi-value">
							<span>{{statiData.sum_money?statiData.sum_money:0 | numFilter}}</span>元
						</div>
					</div>
					<div class="merchant-deposit-page__kpi-card">
						<div class="merchant-deposit-page__kpi-label">当前收入</div>
						<div class="merchant-deposit-page__kpi-value">
							<span>{{statiData.sum_current?statiData.sum_current:0 | numFilter}}</span>元
						</div>
					</div>
					<div class="merchant-deposit-page__kpi-card">
						<div class="merchant-deposit-page__kpi-label">店铺总数</div>
						<div class="merchant-deposit-page__kpi-value">
							<span>{{statiData.sum_store_num?statiData.sum_store_num:0}}</span>个
						</div>
					</div>
					<div class="merchant-deposit-page__kpi-card">
						<div class="merchant-deposit-page__kpi-label">已缴纳保证金店铺</div>
						<div class="merchant-deposit-page__kpi-value">
							<span>{{statiData.margin_status_num?statiData.margin_status_num:0}}</span>个
						</div>
					</div>
				</div>

				<div class="nf-filter-card">
					<div class="nf-filter-card__head">
						<i class="el-icon-search nf-filter-card__head-icon"></i>
						<span>筛选条件</span>
					</div>
					<el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
						<el-form-item label="店铺名称">
							<el-input
								class="merchant-deposit-page__filter-input"
								@keydown.enter.native="select"
								clearable
								placeholder="请输入店铺名称"
								v-model="storeName">
							</el-input>
						</el-form-item>
						<el-form-item class="merchant-deposit-page__filter-actions">
							<el-button type="primary" icon="el-icon-search" @click="select">查询</el-button>
							<el-button type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
							<el-button type="primary" icon="el-icon-refresh" @click="refresh">刷新</el-button>
						</el-form-item>
					</el-form>
				</div>

				<div class="nf-table-card">
					<el-table v-loading="tableDataLoading" :data="tableData.data">
						<el-table-column prop="store_name" label="店铺名称" width="100">
						</el-table-column>
						<el-table-column prop="margin_money" label="保证金金额">
							<template slot-scope="scope">
								<span class="merchant-deposit-page__amount">{{scope.row.margin_money}}</span>
							</template>
						</el-table-column>
						<el-table-column prop="margin_type" label="保证金类型">
							<template slot-scope="scope">
								<span v-if="scope.row.margin_type==null">未续费</span>
								<span v-if="scope.row.margin_type==1">永久</span>
								<span v-if="scope.row.margin_type==2">年费</span>
							</template>
						</el-table-column>
						<el-table-column label="订单号" width="180">
							<template slot-scope="scope">
								<span class="merchant-deposit-page__link"
									@click="orderDetails(scope.row.order_id)">{{scope.row.order_num?scope.row.order_num:'暂无'}}</span>
							</template>
						</el-table-column>
						<el-table-column prop="create_time" label="保证金缴纳时间" width="160">
						</el-table-column>
						<el-table-column prop="end_time" label="保证金到期时间" width="160">
						</el-table-column>
					</el-table>
					<div class="merchant-deposit-page__pagination">
						<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
							:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
							layout="total,sizes, prev, pager, next" :total="tableData.total">
						</el-pagination>
					</div>
				</div>
			</el-tab-pane>
			<el-tab-pane label="保证金退款" name="refund">
				<div class="nf-filter-card">
					<div class="nf-filter-card__head">
						<i class="el-icon-search nf-filter-card__head-icon"></i>
						<span>筛选条件</span>
					</div>
					<el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
						<el-form-item label="订单号">
							<el-input
								class="merchant-deposit-page__filter-input"
								@keydown.enter.native="select"
								clearable
								placeholder="请输入订单号"
								v-model="orderNum">
							</el-input>
						</el-form-item>
						<el-form-item class="merchant-deposit-page__filter-actions">
							<el-button type="primary" icon="el-icon-search" @click="select">查询</el-button>
							<el-button type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
							<el-button type="primary" icon="el-icon-refresh" @click="refresh2">刷新</el-button>
						</el-form-item>
					</el-form>
				</div>

				<div class="nf-table-card">
					<el-table v-loading="tableDataLoading" :data="orderData.data">
						<el-table-column prop="title" label="商品标题" width="200">
						</el-table-column>
						<el-table-column prop="tuikuan_reason" label="退款原因">
						</el-table-column>
						<el-table-column label="订单类型" width="120">
							<template slot-scope="scope">
								<span v-if="scope.row.order_type == 1">普通订单</span>
								<span v-if="scope.row.order_type == 2">拼团订单</span>
								<span v-if="scope.row.order_type == 3">秒杀订单</span>
								<span v-if="scope.row.order_type == 4">积分订单</span>
								<span v-if="scope.row.order_type == 5">虚拟商品订单</span>
								<span v-if="scope.row.order_type == 6">保证金订单</span>
							</template>
						</el-table-column>
						<el-table-column prop="order_id" label="订单号" width="200">
							<template slot-scope="scope">
								<span class="merchant-deposit-page__link"
									@click="orderDetails(scope.row.id)">{{scope.row.order_id}}</span>
							</template>
						</el-table-column>
						<el-table-column prop="store_name" label="退款店铺" width="120">
						</el-table-column>
						<el-table-column prop="payment" label="退款金额">
							<template slot-scope="scope">
								<span class="merchant-deposit-page__amount">{{scope.row.payment | numFilter}}</span>
							</template>
						</el-table-column>
						<el-table-column prop="pay_way" label="支付方式" width="120">
							<template slot-scope="scope">
								<span class="merchant-deposit-page__tag" v-if="scope.row.pay_way === 1 ">微信</span>
								<span class="merchant-deposit-page__tag" v-if="scope.row.pay_way === 2 ">微信公众号</span>
								<span class="merchant-deposit-page__tag" v-if="scope.row.pay_way === 3 ">微信小程序</span>
								<span class="merchant-deposit-page__tag" v-if="scope.row.pay_way === 4 ">app支付宝</span>
								<span class="merchant-deposit-page__tag" v-if="scope.row.pay_way === 5 ">H5支付宝</span>
								<span class="merchant-deposit-page__tag" v-if="scope.row.pay_way === 6 ">零钱</span>
								<span class="merchant-deposit-page__tag" v-if="scope.row.pay_way === 7 ">积分</span>
								<span class="merchant-deposit-page__tag" v-if="scope.row.pay_way === null ">未支付</span>
							</template>
						</el-table-column>
						<el-table-column fixed="right" prop="status" label="订单状态">
							<template slot-scope="scope">
								<span class="merchant-deposit-page__tag" v-if="scope.row.status === 0 ">已取消</span>
								<span class="merchant-deposit-page__tag" v-if="scope.row.status === 1 ">待付款</span>
								<span class="merchant-deposit-page__tag" v-if="scope.row.status === 2 ">代发货</span>
								<span class="merchant-deposit-page__tag" v-if="scope.row.status === 3 ">已发货 </span>
								<span class="merchant-deposit-page__tag" v-if="scope.row.status === 4 ">已收货</span>
								<span class="merchant-deposit-page__tag" v-if="scope.row.status === 5 ">已评价</span>
								<span class="merchant-deposit-page__tag" v-if="scope.row.status === 6 ">交易关闭</span>
								<span class="merchant-deposit-page__tag" v-if="scope.row.status === 7 ">退款中</span>
								<span class="merchant-deposit-page__tag" v-if="scope.row.status === 8 ">已退款</span>
								<span class="merchant-deposit-page__tag" v-if="scope.row.status === 9 ">拒绝退款</span>
								<span class="merchant-deposit-page__tag" v-if="scope.row.status === 10 ">拼团中</span>
							</template>
						</el-table-column>
						<el-table-column prop="createtime" label="创建时间" width="160">
							<template slot-scope="scope">
								<span>{{scope.row.createtime | formatDate}}</span>
							</template>
						</el-table-column>
						<el-table-column prop="payTime" label="付款时间" width="160">
							<template slot-scope="scope">
								<span>{{scope.row.paymenttime ? scope.row.paymenttime : '未付款'}}</span>
							</template>
						</el-table-column>
						<el-table-column fixed="right" label="操作" width="100">
							<template slot-scope="scope">
								<el-button size="mini" type="primary" :disabled="!isAuth('orderAdmin:refund')"
									@click="refundClick(scope.row)">确定退款
								</el-button>
								<el-button size="mini" type="primary" :disabled="!isAuth('orderAdmin:refuse')"
									@click="refuseClick(scope.row)">拒绝退款
								</el-button>
								<el-button size="mini" type="primary" @click="orderDetails(scope.row.id)">订单详情
								</el-button>
							</template>
						</el-table-column>
					</el-table>
					<div class="merchant-deposit-page__pagination">
						<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
							:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
							layout="total,sizes, prev, pager, next" :total="orderData.total">
						</el-pagination>
					</div>
				</div>
				<!-- 拒绝退款 -->
				<el-dialog title="拒绝退款" :visible.sync="dialogFormVisible1" center>
					<div style="margin-bottom: 10px;">
						<span
							style="width: 200px;display: inline-block;text-align: right;position: relative;top: -65px;">拒绝退款理由：</span>
						<el-input style="width:50%;" type="textarea" rows="4" v-model="refusedRefund"
							placeholder="请输入拒绝退款理由">
						</el-input>
					</div>
					<div slot="footer" class="dialog-footer">
						<el-button @click="dialogFormVisible1 = false">取 消</el-button>
						<el-button type="primary" @click="refuseNoticeTo()">确 定</el-button>
					</div>
				</el-dialog>
			</el-tab-pane>
		</el-tabs>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				size: 10,
				page: 1,
				storeName: '',
				orderType: 5,
				status: 6,
				orderNum: '',
				refusedRefund: '',
				info: {
					stockDate: this.getNowTime(), //日期
				},
				way: 3,
				ways: [{
					value: 3,
					label: '按天查询'
				}, {
					value: 2,
					label: '按月查询'
				}, {
					value: 1,
					label: '按年查询'
				}],
				activeName: 'first',
				statiData: {},
				formLabelWidth: '200px',
				tableDataLoading: false,
				dialogFormVisible1: false,
				tableData: [],
				orderData: []
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
			},
			formatDate: function(value) {
				let date = new Date(value);
				let y = date.getFullYear();
				let MM = date.getMonth() + 1;
				MM = MM < 10 ? ('0' + MM) : MM;
				let d = date.getDate();
				d = d < 10 ? ('0' + d) : d;
				let h = date.getHours();
				h = h < 10 ? ('0' + h) : h;
				let m = date.getMinutes();
				m = m < 10 ? ('0' + m) : m;
				let s = date.getSeconds();
				s = s < 10 ? ('0' + s) : s;
				return y + '-' + MM + '-' + d + ' ' + h + ':' + m + ':' + s;
			}
		},
		methods: {
			// 日期选择
			animeOrder() {
				this.statistics()
			},
			orderfenxi() {
				this.statistics()
			},
			//处理默认选中当前日期
			getNowTime() {
				var now = new Date();
				var year = now.getFullYear(); //得到年份
				var month = now.getMonth(); //得到月份
				var date = now.getDate(); //得到日期
				month = month + 1;
				month = month.toString().padStart(2, "0");
				date = date.toString().padStart(2, "0");
				var defaultDate = `${year}-${month}-${date}`;
				return defaultDate;
				this.$set(this.info, "stockDate", defaultDate);
			},
			//搜索
			select() {
				this.page = 1
				this.size = 10
				this.orderSelect()
			},
			cleans() {
				this.orderNum = ''
				this.status = 6
				this.orderType = 10
				this.orderSelect()
			},
			refresh2() {
				this.page = 1
				this.size = 10
				this.orderSelect()
			},
			// 退款
			refundClick(row) {
				this.$confirm(`确定退款?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl2('/admin/storebond/edit_store_money'),
						method: 'get',
						params: this.$http.adornParams({
							'order_id': row.order_id,
							'type': 1
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '退款成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.orderSelect()
								}
							})
						} else {
							this.$message({
								message: data.msg,
								type: 'error',
								duration: 1500,
								onClose: () => {
									this.orderSelect()
								}
							})
						}
					})
				}).catch(() => {})
			},
			// 拒绝退款
			refuseClick(row) {
				// this.ordersId = row.id
				this.ordersId = row.order_id
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
						url: this.$http.adornUrl2('/admin/storebond/edit_store_money'),
						method: 'get',
						params: this.$http.adornParams({
							'order_id': this.ordersId,
							'type': 2,
							'return_word': this.refusedRefund,
						})
					}).then(({
						data
					}) => {
						this.dialogFormVisible1 = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.refusedRefund = ''
								this.orderSelect()
							}
						})
					})
				}
			},
			// 刷新
			refresh() {
				this.dataSelect()
			},
			handleAvatarSuccess2(file) {
				this.form1.img = file.data;
			},
			handleSizeChange(val) {
				this.size = val;
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.dataSelect()
			},
			// 订单详情
			orderDetails(id) {
				this.$router.push({
					path: '/orderDetails',
					query: {
						id: id
					}
				})
			},
			//搜索
			select() {
				this.page = 1
				this.size = 10
				this.dataSelect()
			},
			// 重置
			cleans() {
				this.page = 1
				this.size = 0
				this.storeName = ''
				this.dataSelect()
			},
			handleClick(tab, event) {
				if (tab._props.label == '保证金信息') {
					this.page = 1
					this.size = 10
					this.dataSelect()
					this.statistics()
				}
				if (tab._props.label == '保证金退款') {
					this.page = 1
					this.size = 10
					this.orderSelect()
				}
			},
			// 获取保证金列表数据
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2('/admin/storebond/store_bond_list'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'num': this.size,
						'search_name': this.storeName
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData
				})
			},
			// 获取保证金统计
			statistics() {
				this.$http({
					url: this.$http.adornUrl2('/admin/storebond/store_bond_count'),
					method: 'get',
					params: this.$http.adornParams({
						'date_time': this.info.stockDate,
						'day_type': this.way
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.statiData = returnData
				})
			},
			// 获取订单数据
			orderSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2('/admin/storebond/store_money_list'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'num': this.size,
						// 'orderNum': this.orderNum,
						// 'status': this.status,
						// 'orderType': this.orderType
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.orderData = returnData
				})
			},
		},
		mounted() {
			this.dataSelect()
			this.statistics()
		}
	};
</script>

<style scoped>
.merchant-deposit-page__filter-select {
	width: 150px;
}

.merchant-deposit-page__filter-input {
	width: 160px;
}

.merchant-deposit-page__filter-date {
	width: 160px;
}

.merchant-deposit-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.merchant-deposit-page__stat-filter {
	display: flex;
	flex-wrap: wrap;
	align-items: center;
	gap: 12px;
}

.merchant-deposit-page__tabs {
	margin-bottom: 0;
}

.merchant-deposit-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.merchant-deposit-page__tabs >>> .el-tabs__item.is-active {
	color: #5cc477;
}

.merchant-deposit-page__tabs >>> .el-tabs__active-bar {
	background-color: #5cc477;
}

.merchant-deposit-page__tabs .nf-filter-card,
.merchant-deposit-page__tabs .merchant-deposit-page__kpi-grid,
.merchant-deposit-page__tabs .nf-table-card {
	margin-top: 12px;
	margin-bottom: 0;
}

.merchant-deposit-page__kpi-grid {
	display: grid;
	grid-template-columns: repeat(4, 1fr);
	gap: 12px;
	background: #fff;
	border: 1px solid #e8eaed;
	border-radius: 8px;
	padding: 16px;
	box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
}

.merchant-deposit-page__kpi-card {
	padding: 16px;
	background: #f7f8fa;
	border: 1px solid #e8eaed;
	border-radius: 6px;
}

.merchant-deposit-page__kpi-label {
	font-size: 13px;
	color: #909399;
	margin-bottom: 8px;
}

.merchant-deposit-page__kpi-value {
	font-size: 14px;
	color: #303133;
}

.merchant-deposit-page__kpi-value span {
	font-size: 22px;
	font-weight: 600;
	color: #5cc477;
	margin-right: 4px;
}

.merchant-deposit-page__link {
	color: #5cc477;
	cursor: pointer;
}

.merchant-deposit-page__link:hover {
	color: #3fae5c;
}

.merchant-deposit-page__tag {
	color: #5cc477;
}

.merchant-deposit-page__amount {
	color: #f56c6c;
}

.merchant-deposit-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

.merchant-deposit-page .nf-table-card .el-button + .el-button {
	margin-left: 0;
	margin-top: 5px;
}

@media (max-width: 1200px) {
	.merchant-deposit-page__kpi-grid {
		grid-template-columns: repeat(2, 1fr);
	}
}

@media (max-width: 768px) {
	.merchant-deposit-page__kpi-grid {
		grid-template-columns: 1fr;
	}
}
</style>
