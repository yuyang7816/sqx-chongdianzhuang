<template>
	<div class="nf-page master-position-jl-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">教练模块</span>
			<h2 class="nf-page-hero__title">教练位置记录</h2>
			<p class="nf-page-hero__desc">查看教练实时位置记录，支持按教练名称、电话筛选查询。</p>
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
						class="master-position-jl-page__filter-input"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入教练名称"
						v-model="artificerName">
					</el-input>
				</el-form-item>
				<el-form-item label="电话">
					<el-input
						class="master-position-jl-page__filter-input"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入电话"
						v-model="phone">
					</el-input>
				</el-form-item>
				<el-form-item class="master-position-jl-page__filter-actions">
					<el-button type="primary" icon="el-icon-search" @click="select">查询</el-button>
					<el-button type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
				</el-form-item>
			</el-form>
		</div>

		<el-tabs v-model="activeName" class="master-position-jl-page__tabs" @tab-click="handleClick">
			<el-tab-pane label="位置列表" name="first">
				<div class="nf-table-card">
					<el-table v-loading="tableDataLoading" :data="tableData.list">
						<el-table-column fixed prop="id" label="编号" width="80">
							<template slot-scope="scope">
								<span>{{ scope.$index + 1 }}</span>
							</template>
						</el-table-column>
						<el-table-column prop="artificerName" label="教练名称" width="100">
							<template slot-scope="scope">
								<span
									class="master-position-jl-page__user-link"
									@click="updates(scope.row.userId)">
									{{ scope.row.artificerName ? scope.row.artificerName : '未绑定' }}
								</span>
							</template>
						</el-table-column>
						<el-table-column prop="artificerImg" label="头像">
							<template slot-scope="scope">
								<img v-if="scope.row.artificerImg == null" src="~@/assets/img/avatar.png" alt="" width="40" height="40">
								<img v-else :src="scope.row.artificerImg" alt="" width="40" height="40">
							</template>
						</el-table-column>
						<el-table-column prop="phone" label="电话" width="160">
						</el-table-column>
						<el-table-column prop="address" label="地址" width="200"></el-table-column>
						<el-table-column prop="longitude" label="经度" width="160">
						</el-table-column>
						<el-table-column prop="latitude" label="纬度" width="160">
						</el-table-column>
						<el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
					</el-table>
					<div class="master-position-jl-page__pagination">
						<el-pagination
							@size-change="handleSizeChange"
							@current-change="handleCurrentChange"
							:page-sizes="[10, 20, 30, 40]"
							:page-size="limit"
							:current-page="page"
							layout="total,sizes, prev, pager, next,jumper"
							:total="tableData.totalCount">
						</el-pagination>
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
				page: 1,
				limit: 10,
				classify: 1,
				indentNumber: '',
				imageUrl: '',
				url: '',
				complaintState: '',
				activeName: 'first',
				tableDataLoading: true,
				dialogFormVisible: false,
				dialogFormVisible2: false,
				tableData: [],
				checkBoxData: [], //多选框选择的值
				statesnum: [{
						label: '全部',
						value: ''
					},
					{
						label: '可申诉',
						value: 1
					},
					{
						label: '申诉中',
						value: 2
					},
					{
						label: '申诉未通过',
						value: 3
					},
					{
						label: '申诉通过',
						value: 4
					},
					{
						label: '待审核',
						value: 5
					},
					{
						label: '投诉未通过',
						value: 6
					},
				],
				content: '',
				status: 1,
				helpTakeId: '',
				indentNumbers: '',
				complaintId: '',
				message: '',
				title: '',
				type: 3,
				form: {
					id: '',
					illegal: '',
					forfeitMoney: '',
				},
				formLabelWidth: '200px',
				dialogFormVisible1: false,
				guangaoData: {},
				illegal: '',
				illegalId: '',
				forfeitMoney: '',
				illegalList: [],
				phone:'',
				artificerName:'',
				
			}
		},
		methods: {
			// 多选
			changeFun(val) {
				this.checkBoxData = val
			},
			// tabs切换
			handleClick(tab, event) {
				if (tab._props.label == '位置列表') {
					this.page = 1
					this.limit = 10
					this.dataSelect()
				}
				
			},
			handleSizeChange(val) {
				this.limit = val
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val
				this.dataSelect()
			},
			
			// 查询资源列表
			select() {
				this.page = 1
				this.limit = 10
				
				this.dataSelect()
			},
			// 重置资源列表
			cleans() {
				this.phone = ''
				this.artificerName = ''
				this.illegalId = ''
				this.page = 1
				this.dataSelect()
			},
			// select选择事件
			animeDat(state) {
				this.page = 1
				this.complaintState = state
				console.log(state)
				this.dataSelect()
			},
			animeDat1(state) {
				this.page = 1
				this.illegalId = state
				this.dataSelect()
			},

			
			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('message/selectArtificerLocation'),
					method: 'get',
					params: this.$http.adornParams({
						'page':this.page,
						'limit':this.limit,
						'artificerName':this.artificerName,
						'phone':this.phone,
					})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						this.tableDataLoading = false
						let returnData = data.data
						this.tableData = returnData
					}
				})
			},
			// 详情跳转
			updates(userId) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: userId
					}
				})
			},
			// 处理
			classdelete(row) {
				this.$confirm(`确定处理该投诉?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('message/auditMessage'),
						method: 'post',
						params: this.$http.adornParams({
							'ids': row.id,
							'status': 1,
							'auditContent': '',
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
									this.dataSelect()
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
		},
		mounted() {
			this.dataSelect()
		}
	}
</script>

<style scoped>
.master-position-jl-page__filter-input {
	width: 200px;
}

.master-position-jl-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.master-position-jl-page__tabs {
	margin-bottom: 0;
}

.master-position-jl-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.master-position-jl-page__tabs >>> .el-tabs__item.is-active {
	color: #5cc477;
}

.master-position-jl-page__tabs >>> .el-tabs__active-bar {
	background-color: #5cc477;
}

.master-position-jl-page__tabs .nf-table-card {
	margin-top: 12px;
	margin-bottom: 0;
}

.master-position-jl-page__user-link {
	color: #5cc477;
	cursor: pointer;
}

.master-position-jl-page__user-link:hover {
	color: #3fae5c;
}

.master-position-jl-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}
</style>
