<template>
	<div class="nf-page shop-money-sm-page">
		<div class="shop-money-sm-page__back">
			<a href="#" @click.prevent="prev" class="shop-money-sm-page__back-link">
				<icon-svg name="jiantou" class="shop-money-sm-page__back-icon"></icon-svg>
				返回
			</a>
		</div>

		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">门店餐饮</span>
			<h2 class="nf-page-hero__title">收银台统计</h2>
			<p class="nf-page-hero__desc">查看收银台年、月、日收益汇总与明细流水，支持按日期范围筛选与重置查询。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<div class="shop-money-sm-page__kpi-grid">
			<div class="shop-money-sm-page__kpi-card">
				<div class="shop-money-sm-page__kpi-label">年收益</div>
				<div class="shop-money-sm-page__kpi-value">
					<span>{{ taskStat.cashierYearMoney ? taskStat.cashierYearMoney : 0 }}</span>元
				</div>
			</div>
			<div class="shop-money-sm-page__kpi-card">
				<div class="shop-money-sm-page__kpi-label">月收益</div>
				<div class="shop-money-sm-page__kpi-value">
					<span>{{ taskStat.cashierMonthMoney ? taskStat.cashierMonthMoney : 0 }}</span>元
				</div>
			</div>
			<div class="shop-money-sm-page__kpi-card">
				<div class="shop-money-sm-page__kpi-label">日收益</div>
				<div class="shop-money-sm-page__kpi-value">
					<span>{{ taskStat.cashierDayMoney ? taskStat.cashierDayMoney : 0 }}</span>元
				</div>
			</div>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">
				<i class="el-icon-date nf-filter-card__head-icon"></i>
				<span>筛选条件</span>
			</div>
			<el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
				<el-form-item label="日期范围">
					<el-date-picker
						class="shop-money-sm-page__filter-date"
						v-model="times"
						value-format="yyyy-MM-dd"
						type="daterange"
						range-separator="至"
						start-placeholder="开始日期"
						end-placeholder="结束日期"
						@change="selectTime">
					</el-date-picker>
				</el-form-item>
				<el-form-item class="shop-money-sm-page__filter-actions">
					<el-button type="primary" icon="el-icon-refresh-left" @click="result()">重置</el-button>
				</el-form-item>
				<el-form-item label="收益" class="shop-money-sm-page__total-item">
					<span class="shop-money-sm-page__total-value">¥{{ zongMoney }}</span>
				</el-form-item>
			</el-form>
		</div>

		<div class="nf-table-card">
			<el-table v-loading="tableDataLoading" :data="list.records">
				<el-table-column fixed prop="id" label="编号" width="80">
					<template slot-scope="scope">
						{{ scope.$index + 1 }}
					</template>
				</el-table-column>
				<el-table-column fixed prop="title" label="标题">
				</el-table-column>
				<el-table-column fixed prop="money" label="金额">
				</el-table-column>
				<el-table-column fixed prop="content" label="内容">
				</el-table-column>
				<el-table-column fixed prop="createTime" label="时间">
				</el-table-column>
			</el-table>
			<div class="shop-money-sm-page__pagination">
				<el-pagination
					@size-change="handleSizeChange"
					@current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 40, 50, 100]"
					:page-size="limit"
					:current-page="page"
					layout="total,sizes, prev, pager, next"
					:total="list.total">
				</el-pagination>
			</div>
		</div>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				times: '', //选择的时间区间
				limit: 5,
				page: 1,
				shopId: '',
				list: {},
				tableDataLoading: false,
				startTime: '', //开始时间
				endTime: '', //结束时间
				zongMoney: 0, //总价格
				seMoney: 0, //当前价格
				baifen: 0, //百分比
				taskStat: {},
			}
		},
		methods: {
			//重置日期筛选
			result() {
				this.page = 1
				this.times = ''
				this.startTime = ''
				this.endTime = ''
				this.baifen = 0
				this.zongMoney = 0
				this.seMoney = 0
				this.getList()
			},
			//日期选择
			selectTime(e) {
				this.startTime = e[0]
				this.endTime = e[1]
				this.getList()
			},
			//获取收银台明细数据
			getList() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/dataCentre/selectShopMoney'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'shopId': this.shopId,
						'startTime': this.startTime,
						'endTime': this.endTime,
						'type': 1
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					if (data.code == 0) {
						this.taskStat = data.data
					}
				})
				this.$http({
					url: this.$http.adornUrl('Details/queryUserMoneyDetails'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'shopId': this.shopId,
						'startTime': this.startTime,
						'endTime': this.endTime,
						'type': 1
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					if (data.code == 0) {
						this.zongMoney = data.data.money
						this.list = data.data.pageUtils
					}
				})
			},
			//分页
			handleSizeChange(e) {
				this.limit = e
				this.getList()
			},
			handleCurrentChange(e) {
				this.page = e
				this.getList()
			},
			// 返回上一级
			prev() {
				this.$router.back()
			},
		},
		mounted() {
			if (this.$cookie.get('shopId')) {
				this.shopId = this.$cookie.get('shopId')
			} else {
				this.shopId = this.$route.query.shopId
			}
			this.getList()
		},
		watch: {
			'$route': 'taskdataSelect'

		}
	}
</script>

<style scoped>
.shop-money-sm-page__back {
	margin-bottom: 12px;
}

.shop-money-sm-page__back-link {
	display: inline-flex;
	align-items: center;
	font-size: 14px;
	color: #606266;
	text-decoration: none;
}

.shop-money-sm-page__back-link:hover {
	color: #5cc477;
}

.shop-money-sm-page__back-icon {
	width: 1.2em;
	height: 1.2em;
	margin-right: 4px;
}

.shop-money-sm-page__kpi-grid {
	display: grid;
	grid-template-columns: repeat(3, 1fr);
	gap: 12px;
	background: #fff;
	border: 1px solid #e8eaed;
	border-radius: 8px;
	padding: 16px;
	margin-bottom: 12px;
	box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
}

.shop-money-sm-page__kpi-card {
	padding: 16px;
	background: #f7f8fa;
	border: 1px solid #e8eaed;
	border-radius: 6px;
}

.shop-money-sm-page__kpi-label {
	font-size: 13px;
	color: #909399;
	margin-bottom: 8px;
}

.shop-money-sm-page__kpi-value {
	font-size: 14px;
	color: #303133;
}

.shop-money-sm-page__kpi-value span {
	font-size: 22px;
	font-weight: 600;
	color: #5cc477;
	margin-right: 4px;
}

.shop-money-sm-page__filter-date {
	width: 400px;
}

.shop-money-sm-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.shop-money-sm-page__total-item {
	margin-left: auto;
}

.shop-money-sm-page__total-value {
	font-size: 20px;
	font-weight: 600;
	color: #5cc477;
}

.shop-money-sm-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

@media (max-width: 992px) {
	.shop-money-sm-page__kpi-grid {
		grid-template-columns: 1fr;
	}

	.shop-money-sm-page__filter-date {
		width: 100%;
	}
}
</style>
