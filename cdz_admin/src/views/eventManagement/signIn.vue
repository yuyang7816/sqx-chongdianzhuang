<template>
	<div class="nf-page sign-in-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">活动管理</span>
			<h2 class="nf-page-hero__title">签到列表</h2>
			<p class="nf-page-hero__desc">查看用户活动签到记录，支持按昵称、手机号、活动名称筛选查询。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">
				<i class="el-icon-search nf-filter-card__head-icon"></i>
				<span>筛选条件</span>
			</div>
			<el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
				<el-form-item label="用户昵称">
					<el-input
						class="sign-in-page__filter-input"
						@keydown.enter.native="select"
						placeholder="请输入用户昵称"
						v-model="minMoney">
					</el-input>
				</el-form-item>
				<el-form-item label="手机号">
					<el-input
						class="sign-in-page__filter-input"
						@keydown.enter.native="select"
						placeholder="请输入手机号"
						v-model="maxMoney">
					</el-input>
				</el-form-item>
				<el-form-item label="活动名称">
					<el-input
						class="sign-in-page__filter-input"
						@keydown.enter.native="select"
						placeholder="请输入活动名称"
						v-model="couponName">
					</el-input>
				</el-form-item>
				<el-form-item class="sign-in-page__filter-actions">
					<el-button type="primary" icon="el-icon-refresh" @click="select">刷新</el-button>
					<el-button type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
				</el-form-item>
			</el-form>
		</div>

		<div class="nf-table-card">
			<el-table v-loading="tableDataLoading" :data="tableData.records">
				<el-table-column prop="id" label="编号" width="80">
				</el-table-column>

				<el-table-column prop="userName" label="签到用户">
					<template slot-scope="scope">
						<span
							v-if="scope.row.userName"
							class="sign-in-page__user-link"
							@click="updatesvideo(scope.row.userId)">
							{{ scope.row.userName ? scope.row.userName : '未绑定' }}
						</span>
					</template>
				</el-table-column>
				<el-table-column prop="phone" label="手机号">
				</el-table-column>
				<el-table-column prop="title" label="活动名称">
				</el-table-column>

				<el-table-column prop="createAt" label="签到时间">
				</el-table-column>
			</el-table>
			<div class="sign-in-page__pagination">
				<el-pagination
					@size-change="handleSizeChange"
					@current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]"
					:page-size="limit"
					:current-page="page"
					layout="total,sizes, prev, pager, next,jumper"
					:total="tableData.total">
				</el-pagination>
			</div>
		</div>

		<el-dialog title="任务拒绝" :visible.sync="dialogFormVisible" center>
			<div style="margin-bottom: 10px;">
				<span
					style="width: 200px;display: inline-block;text-align: right;position: relative;top: -70px;">拒绝理由：</span>
				<el-input style="width:50%;" v-model="content" type="textarea" :rows="4" placeholder="请输入拒绝理由">
				</el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="refuseto()">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				limit: 10,
				page: 1,
				content: '',
				status: '',
				statesnum: [{
						value: '',
						label: '全部'
					},
					{
						value: 0,
						label: '待审核'
					}, {
						value: 1,
						label: '审核成功'
					}, {
						value: 2,
						label: '拒绝'
					}
				],
				userName: '',
				activeName: 'first',
				tableDataLoading: false,
				dialogFormVisible: false,
				tableData: {},
				checkBoxData: [], //多选框选择的值
				couponName: '',
				homeData1: [{
						id: 0,
						name: '全部'
					},
					{
						id: 1,
						name: '发起中'
					},
					{
						id: 2,
						name: '匹配成功'
					},
					{
						id: 3,
						name: '取消约玩'
					}
				],
				status: 0,
				maxMoney: '',
				minMoney: '',
				times: '',
				sex: 0, //1男 2女 不限0
				sexList: [{
						value: 0,
						label: '不限'
					},
					{
						value: 1,
						label: '男'
					}, {
						value: 2,
						label: '女'
					}
				],

			}
		},
		methods: {
			// 详情跳转
			updatesvideo(userId) {
				if (userId) {
					this.$router.push({
						path: '/userDetail',
						query: {
							userId: userId
						}
					})
				}

			},

			handleSizeChange(val) {
				this.limit = val;
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.dataSelect()
			},



			// 查询
			select() {
				this.page = 1
				this.limit = 10
				this.dataSelect()
			},
			// 重置
			cleans() {
				this.page = 1
				this.couponName = ''
				this.userName = ''
				this.maxMoney = ''
				this.minMoney = ''
				this.dataSelect()
			},
			// 获取数据列表
			dataSelect() {
				// if(this.times){}
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl(`message/selectActivitySignList`),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'state': 110,
						'platform': '',
						'activityName':this.couponName,
						'userName':this.minMoney,
						'phone':this.maxMoney
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData
				})
			},

			// 筛选信息
			animeDat2(state) {
				this.dataSelect()
			},

			cancelOrder(row, status) {
				let content = '确定取消此订单'

				this.$confirm(content, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('activityApply/cancelActivityApply'),
						method: 'post',
						params: this.$http.adornParams({
							'activityApplyId': row.activityApplyId
						})
					}).then(({
						data
					}) => {
						if(data.code==0){
							this.$message({
								message: '操作成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
						}else{
							this.$message({
								message: data.msg,
								type: 'warning',
								duration: 1500,
								onClose: () => {
								}
							})
						}

					})
				})
			},
			// 删除
			deleteOrder(row){
				this.$confirm('确定删除此订单', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('activityApply/deleteActivityApply'),
						method: 'post',
						params: this.$http.adornParams({
							'activityApplyId': row.activityApplyId
						})
					}).then(({
						data
					}) => {
						if(data.code==0){
							this.$message({
								message: '操作成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
						}else{
							this.$message({
								message: data.msg,
								type: 'warning',
								duration: 1500,
								onClose: () => {
								}
							})
						}

					})
				})
			}
		},
		mounted() {
			this.dataSelect()
		}
	};
</script>

<style scoped>
.sign-in-page__filter-input {
	width: 200px;
}

.sign-in-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.sign-in-page__user-link {
	color: #5cc477;
	cursor: pointer;
}

.sign-in-page__user-link:hover {
	color: #3fae5c;
}

.sign-in-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}
</style>
