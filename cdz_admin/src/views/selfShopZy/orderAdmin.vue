<template>
	<div class="nf-page order-admin-zy-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">自营商城</span>
			<h2 class="nf-page-hero__title">自营订单</h2>
			<p class="nf-page-hero__desc">查看自营商城订单流水与收入成交统计，支持按状态、订单号、手机号、商品标题及时间范围筛选，可查询、重置、导出及退款处理。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<el-tabs v-model="activeName" @tab-click="handleClick" class="order-admin-zy-page__tabs">
		<el-tab-pane label="订单中心" name="first">
			<div class="nf-filter-card">
				<div class="nf-filter-card__head">
					<i class="el-icon-search nf-filter-card__head-icon"></i>
					<span>筛选条件</span>
				</div>
				<el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
					<el-form-item label="订单状态">
						<el-select
							class="order-admin-zy-page__filter-select"
							v-model="status"
							placeholder="请选择订单状态"
							@change="select(status)">
							<el-option v-for="item in statusmain" :key="item.value" :label="item.label" :value="item.value">
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item label="订单号">
						<el-input
							class="order-admin-zy-page__filter-input"
							@keydown.enter.native="select"
							clearable
							placeholder="请输入订单号"
							v-model="orderNum">
						</el-input>
					</el-form-item>
					<el-form-item label="手机号">
						<el-input
							class="order-admin-zy-page__filter-input"
							@keydown.enter.native="select"
							clearable
							placeholder="请输入手机号"
							v-model="mobile">
						</el-input>
					</el-form-item>
					<el-form-item label="商品标题">
						<el-input
							class="order-admin-zy-page__filter-input"
							@keydown.enter.native="select"
							clearable
							placeholder="请输入商品标题"
							v-model="title">
						</el-input>
					</el-form-item>
					<el-form-item label="开始时间">
						<el-date-picker
							class="order-admin-zy-page__filter-date"
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
							class="order-admin-zy-page__filter-date"
							v-model="endTime"
							align="right"
							type="datetime"
							format="yyyy-MM-dd"
							value-format="yyyy-MM-dd"
							placeholder="选择截止时间">
						</el-date-picker>
					</el-form-item>
					<el-form-item class="order-admin-zy-page__filter-actions">
						<el-button type="primary" icon="el-icon-search" @click="select">查询</el-button>
						<el-button type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
						<el-button type="primary" icon="el-icon-download" @click="exportBtn">导出</el-button>
					</el-form-item>
				</el-form>
			</div>

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
						<span class="order-admin-zy-page__price">{{scope.row.price}}</span>
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
				<el-table-column prop="isExpress" label="是否虚拟商品" width="120">
					<template slot-scope="scope">
						<span v-if="scope.row.isExpress == 1">否</span>
						<span v-else>是</span>
					</template>
				</el-table-column>
				<el-table-column prop="mobile" label="手机号" width="120">
				</el-table-column>
				<el-table-column prop="detail" label="详细地址" width="120">
					<template slot-scope="scope">
						<span>{{scope.row.provinces}}{{scope.row.detail}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="postagePrice" label="运费" width="120">
					<template slot-scope="scope">
						<span class="order-admin-zy-page__price">{{scope.row.postagePrice | numFilter}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="payMoney" label="支付金额" width="120">
					<template slot-scope="scope">
						<span class="order-admin-zy-page__price">{{scope.row.payMoney | numFilter}}</span>
					</template>
				</el-table-column>

				
				<el-table-column prop="refund" label="退款原因" width="160">
					<template slot-scope="scope">
						<span>{{scope.row.refund ? scope.row.refund : '未退款'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="refusedRefund" label="驳回原因" width="160">
					<template slot-scope="scope">
						<span>{{scope.row.refusedRefund ? scope.row.refusedRefund : '未拒绝'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="payWay" label="支付方式">
					<template slot-scope="scope">
						<span class="order-admin-zy-page__tag" v-if="scope.row.payWay === 1 ">app微信</span>
						<span class="order-admin-zy-page__tag" v-if="scope.row.payWay === 2 ">微信公众号</span>
						<span class="order-admin-zy-page__tag" v-if="scope.row.payWay === 3 ">微信小程序</span>
						<span class="order-admin-zy-page__tag" v-if="scope.row.payWay === 4 ">app支付宝</span>
						<span class="order-admin-zy-page__tag" v-if="scope.row.payWay === 5 ">H5支付宝</span>
						<span class="order-admin-zy-page__tag" v-if="scope.row.payWay === 6 ">零钱</span>
						<span class="order-admin-zy-page__tag" v-if="scope.row.payWay === 7 ">积分</span>
						<span class="order-admin-zy-page__tag" v-if="scope.row.payWay === null ">未支付</span>
					</template>
				</el-table-column>
				<el-table-column fixed="right" prop="status" label="订单状态">
					<template slot-scope="scope">
						<span class="order-admin-zy-page__tag" v-if="scope.row.status === 1 ">待付款</span>
						<span class="order-admin-zy-page__tag" v-if="scope.row.status === 2 ">待发货 </span>
						<span class="order-admin-zy-page__muted" v-if="scope.row.status === 3 ">已发货 </span>
						<span class="order-admin-zy-page__muted" v-if="scope.row.status === 4 ">已收货</span>
						<span class="order-admin-zy-page__muted" v-if="scope.row.status === 5 ">已取消</span>
						<span class="order-admin-zy-page__tag" v-if="scope.row.status === 6 ">退款中</span>
						<span class="order-admin-zy-page__muted" v-if="scope.row.status === 7 ">已退款</span>
						<span class="order-admin-zy-page__muted" v-if="scope.row.status === 8 ">拒绝退款</span>
						<span class="order-admin-zy-page__price" v-if="scope.row.status === 9 ">拼团中</span>
						<span class="order-admin-zy-page__muted" v-if="scope.row.status === 10 ">已评价</span>
					</template>
				</el-table-column>
				<el-table-column prop="createAt" label="创建时间" width="160">
				</el-table-column>
				<el-table-column prop="payTime" label="付款时间" width="160">
					<template slot-scope="scope">
						<span>{{scope.row.payTime ? scope.row.payTime : '未付款'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="finishTime" label="收货时间" width="160">
					<template slot-scope="scope">
						<span>{{scope.row.finishTime ? scope.row.finishTime : '未收货'}}</span>
					</template>
				</el-table-column>
				<el-table-column fixed="right" label="操作" width="100">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" @click="orderDetails(scope.row.id)" style="margin: 3px;">详情
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('orderAdminZy:refund')" style="margin: 3px;"
							@click="refundClick(scope.row)">退款
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="order-admin-zy-page__pagination">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[5, 10, 15, 20]" :page-size="size" :current-page="page"
					layout="total,sizes, prev, pager, next" :total="totalnum">
				</el-pagination>
			</div>
			</div>
		</el-tab-pane>
		<el-tab-pane label="数据中心" name="second">
			<div class="nf-filter-card">
				<div class="nf-filter-card__head">
					<i class="el-icon-date nf-filter-card__head-icon"></i>
					<span>统计筛选</span>
				</div>
				<div class="order-admin-zy-page__stat-filter">
					<el-select v-model="way" class="order-admin-zy-page__filter-select" @change="animeDat">
						<el-option v-for="item in ways" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
					<el-date-picker
						class="order-admin-zy-page__filter-date"
						v-model="info.stockDate"
						align="right"
						type="datetime"
						format="yyyy-MM-dd"
						value-format="yyyy-MM-dd"
						placeholder="选择开始时间"
						@change="timeDate">
					</el-date-picker>
				</div>
			</div>

			<div class="order-admin-zy-page__kpi-grid">
				<div class="order-admin-zy-page__kpi-card">
					<div class="order-admin-zy-page__kpi-label">总收入</div>
					<div class="order-admin-zy-page__kpi-value">
						<span>{{sumincome | numFilter}}</span>元
					</div>
				</div>
				<div class="order-admin-zy-page__kpi-card">
					<div class="order-admin-zy-page__kpi-label">当前收入</div>
					<div class="order-admin-zy-page__kpi-value">
						<span>{{income | numFilter}}</span>元
					</div>
				</div>
				<div class="order-admin-zy-page__kpi-card">
					<div class="order-admin-zy-page__kpi-label">微信收入</div>
					<div class="order-admin-zy-page__kpi-value">
						<span>{{wxincome | numFilter}}</span>元
					</div>
				</div>
				<div class="order-admin-zy-page__kpi-card">
					<div class="order-admin-zy-page__kpi-label">支付宝收入</div>
					<div class="order-admin-zy-page__kpi-value">
						<span>{{zfbincome | numFilter}}</span>元
					</div>
				</div>
			</div>

			<div class="order-admin-zy-page__detail-grid">
				<div class="order-admin-zy-page__detail-card">
					<div class="order-admin-zy-page__kpi-label">余额</div>
					<div class="order-admin-zy-page__kpi-value">
						<span>{{yueicome | numFilter}}</span>元
					</div>
				</div>
				<div class="order-admin-zy-page__detail-card">
					<div class="order-admin-zy-page__kpi-label">订单年成交量</div>
					<div class="order-admin-zy-page__kpi-value">
						<span>{{ordersum0}}</span>件
					</div>
				</div>
				<div class="order-admin-zy-page__detail-card">
					<div class="order-admin-zy-page__kpi-label">订单月成交量</div>
					<div class="order-admin-zy-page__kpi-value">
						<span>{{ordersum1}}</span>件
					</div>
				</div>
				<div class="order-admin-zy-page__detail-card">
					<div class="order-admin-zy-page__kpi-label">订单日成交量</div>
					<div class="order-admin-zy-page__kpi-value">
						<span>{{ordersum2}}</span>件
					</div>
				</div>
			</div>
		</el-tab-pane>
		<!-- <el-tab-pane label="分销订单" name="fourth">
			<div>
				<div style="margin:2% 0;display: inline-block;">
					<span>分销状态：</span>
					<el-select v-model="status" placeholder="请选择分销类型" style="width:150px;" @change="select1()">
						<el-option v-for="item in statusnum" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>&nbsp;&nbsp;&nbsp;&nbsp;
					<span>手机号：</span>
					<el-input style="width: 150px;" @keydown.enter.native="select1" clearable placeholder="请输入手机号"
						v-model="mobile"></el-input>&nbsp;&nbsp;&nbsp;&nbsp;
				</div>
				<div style="display: inline-block;">
					<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="select1">查询
					</el-button>
					<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="cleans1">重置
					</el-button>
					<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="refresh1">刷新
					</el-button>
				</div>
				<el-table v-loading="tableDataLoading" :data="fenxiaoData">
					<el-table-column prop="id" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="userId" label="用户id" width="80">
						<template slot-scope="scope">
							<span style="color: #5cc477;cursor:pointer;"
								@click="updates(scope.row)">{{scope.row.userId}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="手机号">
						<template slot-scope="scope">
							<span>{{scope.row.phone?scope.row.phone:'未绑定'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="goodsTitle" label="订单名称">
					</el-table-column>
					<el-table-column prop="commissionPrice" label="订单图片" width="100">
						<template slot-scope="scope">
							<img :src="scope.row.goodsImg" width="80" height="80">
						</template>
					</el-table-column>
					<el-table-column prop="commissionPrice" label="订单佣金">
					</el-table-column>
					<el-table-column prop="status" label="分销状态">
						<template slot-scope="scope">
							<span v-if="scope.row.status == 1">未到账</span>
							<span v-if="scope.row.status == 2">已到账</span>
						</template>
					</el-table-column>
					<el-table-column prop="detail" label="分销明细">
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
						:page-sizes="[5, 10, 15, 20]" :page-size="size" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="totalnum">
					</el-pagination>
				</div>
			</div>
		</el-tab-pane> -->
		</el-tabs>
	</div>
</template>
<script>
	export default {
		data() {
			return {
				size: 10,
				page: 1,
				id: '',
				title: '',
				mobile: '',
				orderNum: '',
				status: 0,
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
				statusmain: [{
						value: 0,
						label: '全部'
					},
					{
						value: 1,
						label: '待付款'
					},
					{
						value: 2,
						label: '待发货'
					},
					{
						value: 3,
						label: '已发货'
					},
					{
						value: 4,
						label: '已收货'
					},
					{
						value: 5,
						label: '已取消'
					},
					{
						value: 6,
						label: '退款中'
					},
					{
						value: 7,
						label: '已退款'
					},
					{
						value: 8,
						label: '拒绝退款'
					},
					{
						value: 9,
						label: '拼团中'
					},
					{
						value: 10,
						label: '已评价'
					}
				],
				formLabelWidth: '200px',
				activeName: 'first',
				tableDataLoading: false,
				dialogFormVisible: false,
				tableData: [],
				fenxiaoData: [],
				userId: '',
				phone: '',
				statusnum: [{
					value: 0,
					label: '全部'
				}, {
					value: 1,
					label: '未到账'
				}, {
					value: 2,
					label: '已到账'
				}],
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
				if (rows.status == 2) {
					this.dialogFormVisible = true
					this.id = rows.id
				} else {
					this.$message({
						message: '只有待发货订单才能发货',
						type: 'error',
						duration: 2500
					})
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
			// 详情
			orderDetails(id) {
				this.$router.push({
					path: '/orderDetailsZy',
					query: {
						id: id
					}
				})
			},
			// //处理默认选中当前日期
			// getNowTime() {
			// 	var now = new Date()
			// 	var year = now.getFullYear() //得到年份
			// 	var month = now.getMonth() //得到月份
			// 	var date = now.getDate() //得到日期
			// 	month = month + 1
			// 	month = month.toString().padStart(2, '0')
			// 	date = date.toString().padStart(2, '0')
			// 	var defaultDate = `${year}-${month}-${date}`
			// 	return defaultDate
			// 	this.$set(this.info, 'stockDate', defaultDate)
			// },
			// 日期选择
			animeDat(value) {
				let vanumber = value
				this.moneySelect()
				this.orderSelect()
				// if (vanumber == 3) {
				// 	this.moneySelect()
				// 	this.orderSelect()
				// }
				// if (vanumber == 2) {
				// 	// this.info.stockDate = this.info.stockDate.substring(0, 7)
				// 	this.moneySelect()
				// 	this.orderSelect()
				// 	// this.info.stockDate = this.getNowTime()
				// }
				// if (vanumber == 1) {
				// 	// this.info.stockDate = this.info.stockDate.substring(0, 4)
				// 	this.moneySelect()
				// 	this.orderSelect()
				// 	// this.info.stockDate = this.getNowTime()
				// }
			},
			timeDate() {
				this.moneySelect()
				this.orderSelect()
			},
			handleSizeChange(val) {
				this.size = val;
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.dataSelect()
			},
			handleSizeChange1(val) {
				this.size = val;
				this.dataSelect1()
			},
			handleCurrentChange1(val) {
				this.page = val;
				this.dataSelect1()
			},
			handleClick(tab, event) {
				if (tab._props.label == '订单中心') {
					this.page = 1
					this.size = 10
					this.dataSelect()
				}
				if (tab._props.label == '数据中心') {
					this.moneySelect()
					this.orderSelect()
				}
				if (tab._props.label == '分销订单') {
					this.page = 1
					this.size = 10
					this.dataSelect1()
				}
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
				this.orderNum = ''
				this.title = ''
				this.mobile = ''
				this.status = 0
				this.startTime = ''
				this.endTime = ''
				this.dataSelect()
			},

			// 退款
			refundClick(row) {
				if (row.status != 1&&row.status != 5&&row.status != 6&&row.status != 7&&row.status != 8) {
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
							}
							if (data.status == -1) {
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
					}).catch(() => {})
				} else {
					this.$message({
						message: '只有状态为退款中订单才能退款',
						type: 'error',
						duration: 2500
					})
				}
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
						'title': this.title,
						'mobile': this.mobile,
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
			// 获取订单数据
			dataSelect1() {
				let page = this.page - 1
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('ordersRelation/list'),
					method: 'get',
					params: this.$http.adornParams({
						'page': page,
						'size': this.size,
						'userId': '',
						'phone': this.mobile,
						'status': this.status,
						'moneyFrom': 0
					})
				}).then(({
					data
				}) => {
					console.log('订单数据', data)
					this.tableDataLoading = false
					let returnData = data.data;
					this.fenxiaoData = returnData.content
					this.totalnum = returnData.totalElements
				})
			},
			select1() {
				this.page = 1
				this.size = 5
				this.dataSelect1()
			},
			cleans1() {
				this.page = 1
				this.mobile = ''
				this.status = 0
				this.dataSelect1()
			},
			// 刷新
			refresh1() {
				this.dataSelect1()
			},
			// 详情跳转
			updates(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
					}
				})
			},
			// 收入金额统计
			moneySelect() {
				let page = this.page - 1
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('shopOrders/income'),
					method: 'get',
					params: this.$http.adornParams({
						'data': this.info.stockDate,
						'way': this.way
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.sumincome = returnData.sum0
					this.income = returnData.sum1
					this.wxincome = returnData.pay1
					this.zfbincome = returnData.pay2
					this.yueicome = returnData.pay3
				})
			},
			// 订单成交量统计
			orderSelect() {
				let page = this.page - 1
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('shopOrders/statistical'),
					method: 'get',
					params: this.$http.adornParams({
						'data': this.info.stockDate,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.ordersum0 = returnData.sum0
					this.ordersum1 = returnData.sum1
					this.ordersum2 = returnData.sum2

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
						'title': this.title,
						'mobile': this.mobile,
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
.order-admin-zy-page__filter-select {
	width: 150px;
}

.order-admin-zy-page__filter-input {
	width: 150px;
}

.order-admin-zy-page__filter-date {
	width: 160px;
}

.order-admin-zy-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.order-admin-zy-page__stat-filter {
	display: flex;
	flex-wrap: wrap;
	align-items: center;
	gap: 12px;
}

.order-admin-zy-page__tabs {
	margin-bottom: 0;
}

.order-admin-zy-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.order-admin-zy-page__tabs >>> .el-tabs__item.is-active {
	color: #5cc477;
}

.order-admin-zy-page__tabs >>> .el-tabs__active-bar {
	background-color: #5cc477;
}

.order-admin-zy-page__tabs .nf-filter-card,
.order-admin-zy-page__tabs .order-admin-zy-page__kpi-grid,
.order-admin-zy-page__tabs .order-admin-zy-page__detail-grid,
.order-admin-zy-page__tabs .nf-table-card {
	margin-top: 12px;
	margin-bottom: 0;
}

.order-admin-zy-page__kpi-grid {
	display: grid;
	grid-template-columns: repeat(4, 1fr);
	gap: 12px;
	background: #fff;
	border: 1px solid #e8eaed;
	border-radius: 8px;
	padding: 16px;
	box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
}

.order-admin-zy-page__detail-grid {
	display: grid;
	grid-template-columns: repeat(2, 1fr);
	gap: 12px;
	background: #fff;
	border: 1px solid #e8eaed;
	border-radius: 8px;
	padding: 16px;
	box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
}

.order-admin-zy-page__kpi-card,
.order-admin-zy-page__detail-card {
	padding: 16px;
	background: #f7f8fa;
	border: 1px solid #e8eaed;
	border-radius: 6px;
}

.order-admin-zy-page__kpi-label {
	font-size: 13px;
	color: #909399;
	margin-bottom: 8px;
}

.order-admin-zy-page__kpi-value {
	font-size: 14px;
	color: #303133;
}

.order-admin-zy-page__kpi-value span {
	font-size: 22px;
	font-weight: 600;
	color: #5cc477;
	margin-right: 4px;
}

.order-admin-zy-page__tag {
	color: #5cc477;
}

.order-admin-zy-page__muted {
	color: #999999;
}

.order-admin-zy-page__price {
	color: #f56c6c;
}

.order-admin-zy-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

.order-admin-zy-page .nf-table-card .el-button + .el-button {
	margin-left: 0;
	margin-top: 5px;
}

@media (max-width: 1200px) {
	.order-admin-zy-page__kpi-grid {
		grid-template-columns: repeat(2, 1fr);
	}
}

@media (max-width: 768px) {
	.order-admin-zy-page__kpi-grid,
	.order-admin-zy-page__detail-grid {
		grid-template-columns: 1fr;
	}
}
</style>