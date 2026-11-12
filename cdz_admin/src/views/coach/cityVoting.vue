<template>
	<div class="nf-page city-voting-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">教练模块</span>
			<h2 class="nf-page-hero__title">城市投票</h2>
			<p class="nf-page-hero__desc">查看各城市投票统计，支持按城市名称筛选查询，可查看城市投票明细列表。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">
				<i class="el-icon-search nf-filter-card__head-icon"></i>
				<span>筛选条件</span>
			</div>
			<el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
				<el-form-item label="城市">
					<el-input
						class="city-voting-page__filter-input"
						@keydown.enter.native="phoneSelect"
						clearable
						placeholder="请输入城市"
						v-model="artificerName">
					</el-input>
				</el-form-item>
				<el-form-item class="city-voting-page__filter-actions">
					<el-button type="primary" icon="el-icon-search" @click="phoneSelect">查询</el-button>
					<el-button type="primary" icon="el-icon-refresh-left" @click="cleans2">重置</el-button>
				</el-form-item>
			</el-form>
		</div>

		<el-tabs value="first" class="city-voting-page__tabs">
			<el-tab-pane label="城市投票列表" name="first">
				<div class="nf-table-card">
					<el-table v-loading="tableDataLoading" :data="choicenData.list">
						<el-table-column prop="artificerId" label="编号" width="80">
							<template slot-scope="scope">
								<span>{{ scope.$index + 1 }}</span>
							</template>
						</el-table-column>
						<el-table-column prop="content" label="城市">
						</el-table-column>
						<el-table-column prop="counts" label="数量">
						</el-table-column>
						<el-table-column label="操作" prop="id" width="120">
							<template slot-scope="scope">
								<el-button size="mini" type="primary" @click="xinyongBtn(scope.row)">投票列表
								</el-button>
							</template>
						</el-table-column>
					</el-table>
					<div class="city-voting-page__pagination">
						<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
							:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
							layout="total,sizes, prev, pager, next,jumper" :total="choicenData.totalCount">
						</el-pagination>
					</div>
				</div>
			</el-tab-pane>
		</el-tabs>

		<el-dialog title="投票列表" :visible.sync="dialogFormVisibleXyf" center>
			<el-table v-loading="tableDataLoadingXyf" :data="tableDataXyf.list">
				<el-table-column prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="userName" label="用户昵称">
					<template slot-scope="scope">
						<span
							class="city-voting-page__user-link"
							@click="updatesvideo(scope.row)">
							{{ scope.row.userName ? scope.row.userName : '未绑定' }}
						</span>
					</template>
				</el-table-column>
				<el-table-column prop="content" label="投票城市">
				</el-table-column>
				<el-table-column prop="createAt" label="投票时间">
				</el-table-column>
			</el-table>
			<div class="city-voting-page__dialog-pagination">
				<el-pagination @size-change="handleSizeChangeXyf" @current-change="handleCurrentChangeXyf"
					:page-sizes="[10, 20, 30, 40]" :page-size="size1" :current-page="page1"
					layout="total,sizes, prev, pager, next,jumper" :total="tableDataXyf.totalCount">
				</el-pagination>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				page: 1,
				size1: 10,
				page1: 1,
				limit: 10,
				tableDataLoading: true,
				choicenData: [],
				artificerName: '',
				artificerId:'',
				dialogFormVisibleXyf: false,
				tableDataLoadingXyf: false,
				tableDataXyf: {},

			}
		},
		methods: {


			handleSizeChange1(val) {
				this.limit = val
				this.InformationSelect()
			},
			handleCurrentChange1(val) {
				this.page = val
				this.InformationSelect()
			},



			handleSizeChangeXyf(val) {
				this.size1 = val
				this.xinyongSelect(this.artificerId)
			},
			handleCurrentChangeXyf(val) {
				this.page1 = val
				this.xinyongSelect(this.artificerId)
			},
			


			// 查询
			phoneSelect() {
				this.page = 1
				this.InformationSelect()
			},
			// 重置
			cleans2() {
				this.artificerName = ''
				this.page = 1
				this.InformationSelect()
			},

			// 信息数据
			InformationSelect() {
				this.$http({
					url: this.$http.adornUrl('message/selectCityList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'content': this.artificerName,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.choicenData = returnData


				})
			},

			// 详情跳转
			updatesvideo(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
					}
				})
			},


			// 信用分明细弹框
			xinyongBtn(row) {
				this.artificerId = row.content
				this.xinyongSelect(this.artificerId)
				this.dialogFormVisibleXyf = true

			},
			// 信用分明细数据
			xinyongSelect(artificerId) {
				this.tableDataLoadingXyf = true
				this.$http({
					url: this.$http.adornUrl('message/selectCityDetailsList'),
					method: 'get',
					params: this.$http.adornParams({
						'content': artificerId,
						'page': this.page1,
						'limit': this.size1
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.tableDataLoadingXyf = false
						let returnData = data.data
						this.tableDataXyf = returnData


					} else {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: data.msg,
							type: 'warning'
						});
					}
				})
			},
		},
		mounted() {
			this.InformationSelect()
		}
	}
</script>

<style scoped>
.city-voting-page__filter-input {
	width: 200px;
}

.city-voting-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.city-voting-page__tabs {
	margin-bottom: 0;
}

.city-voting-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.city-voting-page__tabs .nf-table-card {
	margin-bottom: 0;
}

.city-voting-page__tabs >>> .el-tabs__item.is-active {
	color: #5cc477;
}

.city-voting-page__tabs >>> .el-tabs__active-bar {
	background-color: #5cc477;
}

.city-voting-page__user-link {
	color: #5cc477;
	cursor: pointer;
}

.city-voting-page__user-link:hover {
	color: #3fae5c;
}

.city-voting-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

.city-voting-page__dialog-pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}
</style>
