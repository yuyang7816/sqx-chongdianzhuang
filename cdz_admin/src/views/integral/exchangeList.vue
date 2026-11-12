<template>
	<div class="nf-page exchange-list-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">积分管理</span>
			<h2 class="nf-page-hero__title">充值记录</h2>
			<p class="nf-page-hero__desc">查看会员卡充值流水与支付统计，支持按状态、用户、订单号、支付方式及时间范围筛选查询。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<el-tabs v-model="activeName" @tab-click="handleClick" class="exchange-list-page__tabs">
			<el-tab-pane label="会员卡充值记录" name="first">
				<div class="nf-filter-card">
					<div class="nf-filter-card__head">
						<i class="el-icon-search nf-filter-card__head-icon"></i>
						<span>筛选条件</span>
					</div>
					<el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
						<el-form-item label="状态">
							<el-select
								class="exchange-list-page__filter-select"
								v-model="state"
								placeholder="请选择状态"
								@change="timeDate2()">
								<el-option v-for="item in states" :key="item.value" :label="item.label" :value="item.value">
								</el-option>
							</el-select>
						</el-form-item>
						<el-form-item label="用户名称">
							<el-input
								class="exchange-list-page__filter-input"
								@keydown.enter.native="timeDate2"
								clearable
								placeholder="请输入用户名称"
								v-model="userName">
							</el-input>
						</el-form-item>
						<el-form-item label="手机号">
							<el-input
								class="exchange-list-page__filter-input"
								@keydown.enter.native="timeDate2"
								clearable
								placeholder="请输入手机号"
								v-model="phone">
							</el-input>
						</el-form-item>
						<el-form-item label="支付订单号">
							<el-input
								class="exchange-list-page__filter-input"
								@keydown.enter.native="timeDate2"
								clearable
								placeholder="请输入支付订单号"
								v-model="orderId">
							</el-input>
						</el-form-item>
						<el-form-item label="支付方式">
							<el-select
								class="exchange-list-page__filter-select"
								v-model="classify"
								placeholder="请选择支付方式"
								@change="timeDate2()">
								<el-option v-for="item in classifyList2" :key="item.value" :label="item.label" :value="item.value">
								</el-option>
							</el-select>
						</el-form-item>
						<el-form-item label="开始时间">
							<el-date-picker
								class="exchange-list-page__filter-date"
								v-model="startTime"
								align="right"
								type="date"
								format="yyyy-MM-dd"
								value-format="yyyy-MM-dd"
								placeholder="选择开始时间">
							</el-date-picker>
						</el-form-item>
						<el-form-item label="截止时间">
							<el-date-picker
								class="exchange-list-page__filter-date"
								v-model="endTime"
								align="right"
								type="date"
								format="yyyy-MM-dd"
								value-format="yyyy-MM-dd"
								placeholder="选择截止时间">
							</el-date-picker>
						</el-form-item>
						<el-form-item class="exchange-list-page__filter-actions">
							<el-button type="primary" icon="el-icon-search" @click="timeDate2">查询</el-button>
							<el-button type="primary" icon="el-icon-refresh-left" @click="clares">重置</el-button>
						</el-form-item>
					</el-form>
				</div>

				<div class="nf-table-card">
					<el-table v-loading="tableDataLoading" :data="tableData.list">
						<el-table-column prop="id" label="编号" width="50"></el-table-column>
						<el-table-column label="支付类型">
							<template slot-scope="scope">
								<span class="exchange-list-page__tag" v-if="scope.row.type == 1">会员充值</span>
								<span class="exchange-list-page__tag" v-if="scope.row.type == 2">订单</span>
								<span class="exchange-list-page__tag" v-if="scope.row.type == 3">预约</span>
								<span class="exchange-list-page__tag" v-if="scope.row.type == 4">优惠券</span>
							</template>
						</el-table-column>
						<el-table-column prop="userName" label="用户名">
							<template slot-scope="scope">
								<span class="exchange-list-page__link"
									@click="updates(scope.row)">{{ scope.row.userName }}</span>
							</template>
						</el-table-column>
						<el-table-column prop="phone" label="手机号" width="250">
						</el-table-column>
						<el-table-column prop="orderId" label="充值订单号" width="200"></el-table-column>
						<el-table-column prop="money" label="充值金额"></el-table-column>
						<el-table-column prop="userId" label="会员编号"></el-table-column>
						<el-table-column label="分类">
							<template slot-scope="scope">
								<span class="exchange-list-page__tag" v-if="scope.row.classify == 1">app微信</span>
								<span class="exchange-list-page__tag" v-if="scope.row.classify == 2">微信公众号</span>
								<span class="exchange-list-page__tag" v-if="scope.row.classify == 3">微信小程序</span>
								<span class="exchange-list-page__tag" v-if="scope.row.classify == 4">支付宝</span>
							</template>
						</el-table-column>
						<el-table-column label="状态">
							<template slot-scope="scope">
								<span class="exchange-list-page__tag" v-if="scope.row.state == 0">待支付</span>
								<span class="exchange-list-page__tag" v-if="scope.row.state == 1">支付成功</span>
								<span class="exchange-list-page__tag" v-if="scope.row.state == 2">支付失败</span>
							</template>
						</el-table-column>
						<el-table-column prop="createTime" label="创建时间" width="170">
						</el-table-column>
						<el-table-column prop="payTime" label="支付时间" width="170">
						</el-table-column>
					</el-table>
					<div class="exchange-list-page__pagination">
						<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
							:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
							layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
						</el-pagination>
					</div>
				</div>
			</el-tab-pane>

			<el-tab-pane label="会员卡充值统计" name="sixth">
				<div class="nf-filter-card">
					<div class="nf-filter-card__head">
						<i class="el-icon-date nf-filter-card__head-icon"></i>
						<span>筛选条件</span>
					</div>
					<div class="exchange-list-page__stat-filter">
						<el-select v-model="flag" class="exchange-list-page__filter-select" @change="animeDat3">
							<el-option v-for="item in flags" :key="item.value" :label="item.label" :value="item.value">
							</el-option>
						</el-select>
						<el-date-picker
							class="exchange-list-page__filter-date"
							v-model="info.stockDate"
							align="right"
							type="date"
							format="yyyy-MM-dd"
							value-format="yyyy-MM-dd"
							placeholder="选择开始时间"
							@change="animeSelect3">
						</el-date-picker>
					</div>
				</div>

				<div class="exchange-list-page__kpi-grid">
					<div class="exchange-list-page__kpi-card">
						<div class="exchange-list-page__kpi-label">支付总金额</div>
						<div class="exchange-list-page__kpi-value">
							<span>{{ rechgeData.sumMoney ? rechgeData.sumMoney : 0 }}</span>元
						</div>
					</div>
					<div class="exchange-list-page__kpi-card">
						<div class="exchange-list-page__kpi-label">微信小程序支付金额</div>
						<div class="exchange-list-page__kpi-value">
							<span>{{ rechgeData.weiXinXCXMoney ? rechgeData.weiXinXCXMoney : 0 }}</span>元
						</div>
					</div>
					<div class="exchange-list-page__kpi-card">
						<div class="exchange-list-page__kpi-label">微信APP支付金额</div>
						<div class="exchange-list-page__kpi-value">
							<span>{{ rechgeData.weiXinAppMoney ? rechgeData.weiXinAppMoney : 0 }}</span>元
						</div>
					</div>
					<div class="exchange-list-page__kpi-card">
						<div class="exchange-list-page__kpi-label">微信公众号支付金额</div>
						<div class="exchange-list-page__kpi-value">
							<span>{{ rechgeData.weiXinGZHMoney ? rechgeData.weiXinGZHMoney : 0 }}</span>元
						</div>
					</div>
					<div class="exchange-list-page__kpi-card">
						<div class="exchange-list-page__kpi-label">支付宝APP支付金额</div>
						<div class="exchange-list-page__kpi-value">
							<span>{{ rechgeData.zhiFuBaoAppMoney ? rechgeData.zhiFuBaoAppMoney : 0 }}</span>元
						</div>
					</div>
					<div class="exchange-list-page__kpi-card">
						<div class="exchange-list-page__kpi-label">支付宝H5支付金额</div>
						<div class="exchange-list-page__kpi-value">
							<span>{{ rechgeData.zhiFuBaoH5Money ? rechgeData.zhiFuBaoH5Money : 0 }}</span>元
						</div>
					</div>
				</div>
			</el-tab-pane>
		</el-tabs>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				activeName: 'first',
				page: 1,
				limit: 10,
				tableDataLoading: true,
				dialogFormVisible: false,
				tableData: [],
				memberName: '',
				memberImg: '',
				memberId: '',
				sort: '',
				title: '添加',
				startTime: '',
				endTime: '',
				info: {
					stockDate: this.getNowTime(), //日期
				},
				info2: {
					stockDate2: this.getNowTime2(), //日期
				},
				state: -1,
				states: [{
						label: '全部',
						value: -1
					},
					{
						label: '待支付',
						value: 0
					},
					{
						label: '支付成功',
						value: 1
					},
					{
						label: '支付失败',
						value: 2
					}
				],
				userName: '',
				phone: '',
				orderId: '',
				classify: '',
				classifyList2: [{
						label: '全部',
						value: ''
					},
					{
						label: 'app微信',
						value: 1
					},
					{
						label: '微信公众号',
						value: 2
					},
					{
						label: '微信小程序',
						value: 3
					},
					{
						label: '支付宝',
						value: 4
					},
					{
						label: '支付宝H5',
						value: 5
					},
				],
				typeT:1,
				typeTs: [{
						label: '全部',
						value: ''
					},
					{
						label: '会员充值',
						value: 1
					},
					{
						label: '订单',
						value: 2
					},
					{
						label: '预约',
						value: 3
					},
					{
						label: '优惠券',
						value: 4
					}
				],
				rechgeData: {},
				flag: 1,
				flags: [{
					value: 1,
					label: '按天查询'
				}, {
					value: 2,
					label: '按月查询'
				}, {
					value: 3,
					label: '按年查询'
				}],
			}
		},
		methods: {
			handleClick(tab, event) {
				if (tab._props.label == '会员卡充值记录') {
					this.page = 1
					this.limit = 10
					this.dataSelect()
				}
				
				if (tab._props.label == '会员卡充值统计') {
					this.flag = 1
					this.rechSelect()
				}
			},
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
			// 详情跳转
			updates(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
					}
				})
			},
			clares() {
				this.page = 1
				this.userName = ''
				this.phone = ''
				this.orderId = ''
				this.endTime = ''
				this.startTime = ''
				this.typeT =1
				this.dataSelect()
			},
			// 获取数据列表
			dataSelect() {

				this.tableDataLoading = true
				if (this.endTime == '') {
					this.endTime = this.info.stockDate
				}
				if (this.startTime == '') {
					this.startTime = this.info2.stockDate2
				}
				this.$http({
					url: this.$http.adornUrl('cash/selectUserRechargeByUserId'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'endTime': this.endTime,
						'startTime': this.startTime,
						'state': this.state,
						'userName': this.userName,
						'phone': this.phone,
						'orderId': this.orderId,
						'type': this.typeT,
					})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						console.log('会员列表', data)
						this.tableDataLoading = false
						let returnData = data.data;
						this.tableData = returnData;
					}

				})
			},
			timeDate2() {
				this.dataSelect()
			},
			
			handleCurrentChange(val) {
				this.page = val
				this.dataSelect()
			},
			handleSizeChange(val) {
				this.limit = val
				this.dataSelect()
			},
			// 充值统计
			rechSelect() {
				this.$http({
					url: this.$http.adornUrl('cash/payUserMoney'),
					method: 'get',
					params: this.$http.adornParams({
						'time': this.info.stockDate,
						'flag': this.flag
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						let returnData = data.data
						this.rechgeData = returnData
					}
				})
			},
			// 日期选择3
			animeDat3(value) {
				let vanumber = value
				if (vanumber === 1) {
					this.flag = 1
					this.rechSelect()
				}
				if (vanumber === 2) {
			
					this.flag = 2
					this.rechSelect()
				}
				if (vanumber === 3) {
					this.flag = 3
					this.rechSelect()
				}
			},
			animeSelect3() {
				this.rechSelect()
			},
			
		},
		mounted() {
			this.dataSelect()
		}
	}
</script>

<style scoped>
.exchange-list-page__filter-select {
	width: 150px;
}

.exchange-list-page__filter-input {
	width: 200px;
}

.exchange-list-page__filter-date {
	width: 160px;
}

.exchange-list-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.exchange-list-page__stat-filter {
	display: flex;
	flex-wrap: wrap;
	align-items: center;
	gap: 12px;
}

.exchange-list-page__tabs {
	margin-bottom: 0;
}

.exchange-list-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.exchange-list-page__tabs >>> .el-tabs__item.is-active {
	color: #5cc477;
}

.exchange-list-page__tabs >>> .el-tabs__active-bar {
	background-color: #5cc477;
}

.exchange-list-page__tabs .nf-filter-card {
	margin-top: 12px;
}

.exchange-list-page__tabs .nf-table-card,
.exchange-list-page__tabs .exchange-list-page__kpi-grid {
	margin-top: 12px;
	margin-bottom: 0;
}

.exchange-list-page__link {
	color: #5cc477;
	cursor: pointer;
}

.exchange-list-page__link:hover {
	color: #3fae5c;
}

.exchange-list-page__tag {
	color: #5cc477;
	cursor: pointer;
}

.exchange-list-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

.exchange-list-page__kpi-grid {
	display: grid;
	grid-template-columns: repeat(4, 1fr);
	gap: 12px;
	background: #fff;
	border: 1px solid #e8eaed;
	border-radius: 8px;
	padding: 16px;
	box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
}

.exchange-list-page__kpi-card {
	padding: 16px;
	background: #f7f8fa;
	border: 1px solid #e8eaed;
	border-radius: 6px;
}

.exchange-list-page__kpi-label {
	font-size: 13px;
	color: #909399;
	margin-bottom: 8px;
}

.exchange-list-page__kpi-value {
	font-size: 14px;
	color: #303133;
}

.exchange-list-page__kpi-value span {
	font-size: 22px;
	font-weight: 600;
	color: #5cc477;
	margin-right: 4px;
}

@media (max-width: 1200px) {
	.exchange-list-page__kpi-grid {
		grid-template-columns: repeat(2, 1fr);
	}
}

@media (max-width: 768px) {
	.exchange-list-page__kpi-grid {
		grid-template-columns: 1fr;
	}
}
</style>
