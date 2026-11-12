<template>
	<div class="nf-page top-jl-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">教练管理</span>
			<h2 class="nf-page-hero__title">教练信用分排行榜</h2>
			<p class="nf-page-hero__desc">查看已认证教练信用分排名，支持按教练名称与城市筛选查询，点击教练昵称可跳转用户详情。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">
				<i class="el-icon-search nf-filter-card__head-icon"></i>
				<span>筛选条件</span>
			</div>
			<el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
				<el-form-item label="教练名称">
					<el-input
						class="top-jl-page__filter-input"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入教练名称"
						v-model="artificerName">
					</el-input>
				</el-form-item>
				<el-form-item label="城市">
					<el-input
						class="top-jl-page__filter-input"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入城市"
						v-model="citys">
					</el-input>
				</el-form-item>
				<el-form-item class="top-jl-page__filter-actions">
					<el-button type="primary" icon="el-icon-search" @click="select">查询</el-button>
					<el-button type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
				</el-form-item>
			</el-form>
		</div>

		<div class="nf-table-card">
			<el-table v-loading="tableDataLoading" :data="homeData.list">
				<el-table-column fixed prop="artificerId" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="artificerName" label="教练昵称">
					<template slot-scope="scope">
						<span class="top-jl-page__link" @click="updatesvideo(scope.row)">
							{{scope.row.artificerName?scope.row.artificerName:'未绑定'}}
						</span>
					</template>
				</el-table-column>
				<el-table-column prop="artificerImg" label="头像">
					<template slot-scope="scope">
						<img
							v-if="scope.row.artificerImg"
							class="top-jl-page__avatar"
							:src="scope.row.artificerImg"
							width="40"
							height="40" />
						<span v-else>暂无头像</span>
					</template>
				</el-table-column>
				<el-table-column prop="city" label="城市">
				</el-table-column>
				<el-table-column prop="creditScore" label="信用分">
				</el-table-column>
			</el-table>
			<div class="top-jl-page__pagination">
				<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
					:page-sizes="[10, 20, 30, 40]" :page-size="size" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="homeData.totalCount">
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
				state: '',
				limit: 10,
				title: '',
				type: 1,
				activeName: 'first',
				tableDataLoading: true,
				homeData: {},
				flag: 1,
				info: {
					stockDate: this.getNowTime(), //日期
				},
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
				artificerName:'',
				citys:'',
			}
		},
		methods: {
			//处理默认选中当前日期
			getNowTime() {
				var now = new Date()
				var year = now.getFullYear() //得到年份
				var month = now.getMonth() //得到月份
				var date = now.getDate() //得到日期
				var hh = now.getHours() < 10 ? '0' + now.getHours() : now.getHours()
				var mm = now.getMinutes() < 10 ? '0' + now.getMinutes() : now.getMinutes()
				var ss = now.getSeconds() < 10 ? '0' + now.getSeconds() : now.getSeconds()
				month = month + 1
				month = month.toString().padStart(2, '0')
				date = date.toString().padStart(2, '0')
				var defaultDate = `${year}-${month}-${date} ${hh}:${mm}:${ss}`
				return defaultDate
				this.$set(this.info, 'stockDate', defaultDate)
			},
			handleSizeChange1(val) {
				this.limit = val
				this.homeSelect()
			},
			handleCurrentChange1(val) {
				this.page = val
				this.homeSelect()
			},
			handleClick(tab, event) {
				this.page = 1
				if (tab._props.label == '团长收益排行榜') {
					this.type = 1
					this.homeSelect()
				}
			},
			// 信息数据
			homeSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl(`artificer/selectArtificerList`),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'artificerName': this.artificerName,
						'massageTypeId': '',
						'longitude': '',
						'latitude': '',
						'city': this.citys,
						'classifyId':'',
						'by':4,
						'authentication':2
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data

					console.log(data.data.state)
					this.homeData = data.data
				})
			},

			// 详情跳转
			updatesvideo(row) {
				if (row.userId) {
					this.$router.push({
						path: '/userDetail',
						query: {
							userId: row.userId
						}
					})
				} else {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '未绑定用户',
						type: 'warning'
					})
					return
				}

			},
			// 查询
			select() {
				this.page = 1
				this.limit = 10
				this.homeSelect()
			},
			// 重置
			cleans() {
				this.artificerName = ''
				this.citys = ''
				this.homeSelect()
			},
			// 订单分析选择日期
			animeOrder() {
				console.log('info', this.info)
				this.homeSelect()
				// this.colonel()
			},
			// 订单分析年月日
			orderfenxi(value) {
				this.page = 1
				let vanumber = value
				this.flag = value
				this.homeSelect()

			},
		},
		mounted() {
			this.homeSelect()
		}
	}
</script>

<style scoped>
.top-jl-page__filter-input {
	width: 200px;
}

.top-jl-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.top-jl-page__link {
	color: #5cc477;
	cursor: pointer;
}

.top-jl-page__link:hover {
	color: #3fae5c;
}

.top-jl-page__avatar {
	border-radius: 50%;
	object-fit: cover;
	vertical-align: middle;
}

.top-jl-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}
</style>
