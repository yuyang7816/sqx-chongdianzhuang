<template>
	<div class="nf-page credit-score-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">教练信用分</span>
			<h2 class="nf-page-hero__title">教练信用分明细</h2>
			<p class="nf-page-hero__desc">查看教练信用分明细记录，支持按用户昵称、手机号筛选，可查询、重置与刷新。</p>
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
						class="credit-score-page__filter-input"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入用户昵称"
						v-model="userName">
					</el-input>
				</el-form-item>
				<el-form-item label="手机号">
					<el-input
						class="credit-score-page__filter-input"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入手机号"
						v-model="phone">
					</el-input>
				</el-form-item>
				<el-form-item class="credit-score-page__filter-actions">
					<el-button type="primary" icon="el-icon-search" @click="select">查询</el-button>
					<el-button type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
					<el-button type="primary" icon="el-icon-refresh" @click="refresh">刷新</el-button>
				</el-form-item>
			</el-form>
		</div>

		<el-tabs v-model="activeName" @tab-click="handleClick" class="credit-score-page__tabs">
			<el-tab-pane label="信用分明细" name="first">
				<div class="nf-table-card">
					<el-table v-loading="tableDataLoading" :data="tableData.list">
						<el-table-column prop="userId" label="编号" width="80">
						</el-table-column>
						<el-table-column prop="artificerName" label="教练昵称" width="150">
							<template slot-scope="scope">
								<span
									class="credit-score-page__link"
									@click="updates(scope.row.userId)">{{scope.row.artificerName ? scope.row.artificerName : '未绑定'}}</span>
							</template>
						</el-table-column>
						<el-table-column prop="artificerImg" label="头像">
							<template slot-scope="scope">
								<img :src="scope.row.artificerImg" alt="" width="60" height="60">
							</template>
						</el-table-column>
						<el-table-column prop="phone" label="手机号">
						</el-table-column>
						<el-table-column prop="title" label="标题">
						</el-table-column>
						<el-table-column prop="content" label="内容">
						</el-table-column>
						<!-- <el-table-column prop="score" label="信用分">
						<template slot-scope="scope">
							<span>{{scope.row.score?scope.row.score:'暂无'}}</span>
						</template>
					</el-table-column> -->
						<el-table-column prop="createTime" label="创建时间" width="180">
						</el-table-column>
					</el-table>
					<div class="credit-score-page__pagination">
						<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
							:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
							layout="total,sizes, prev, pager, next" :total="tableData.totalCount">
						</el-pagination>
					</div>
				</div>
			</el-tab-pane>
		</el-tabs>

		<!-- 回复 -->
		<el-dialog title="回复" :visible.sync="dialogFormVisible" center>
			<el-form :model="form">
				<el-form-item label="回复内容：" :label-width="formLabelWidth">
					<el-input v-model="form.reply" type="textarea" :rows="4" style="width:65%;"
						placeholder="请输入回复内容">
					</el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="replyNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				size: 10,
				page: 1,
				goodsId: '',
				form: {
					commentId: '',
					reply: ''
				},
				scoreType: 0,
				scoreTypes: [{
						value: 0,
						label: '全部'
					},
					{
						value: 1,
						label: '好评'
					},
					{
						value: 2,
						label: '中评'
					},
					{
						value: 3,
						label: '差评'
					}
				],
				formLabelWidth: '200px',
				tableDataLoading: false,
				dialogFormVisible: false,
				tableData: [],
				content: '',
				title: '',
				userName: '',
				phone: '',
				activeName: 'first',

			}
		},
		methods: {
			// 返回上一级
			prev() {
				this.$router.back()
			},
			handleSizeChange(val) {
				this.size = val;
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.dataSelect()
			},
			
			// tabs切换
			handleClick(tab, event) {
				this.phone = ''
				this.page = 1
				this.userName = ''
				if (tab._props.label == '评价列表') {
					this.dataSelect()
				}
				
			},
			// 刷新
			refresh() {
				this.dataSelect()
			},
			//搜索
			select() {
				this.page = 1
				this.dataSelect()
			},
			// 重置
			cleans() {
				this.title = ''
				this.userName = ''
				this.phone = ''
				this.content = ''
				this.page = 1
				this.dataSelect()
			},
			
			// 回复
			replyClick(row) {
				this.dialogFormVisible = true
				this.form.commentId = row.commentId
				this.form.reply = row.reply
			},
			replyNoticeTo() {
				this.$http({
					url: this.$http.adornUrl('/selfGoodsComment/reply'),
					method: 'get',
					params: this.$http.adornParams({
						'commentId': this.form.commentId,
						'reply': this.form.reply,
					})
				}).then(({
					data
				}) => {
					this.$message({
						message: '回复成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.dataSelect()
						}
					})
					this.dialogFormVisible = false
				})
			},
			// 删除评价
			deletes(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`takingComment/deleteOrderTakingComment/${row.id}`),
						method: 'post',
						params: this.$http.adornParams({})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '删除成功',
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
			// 获取评价列表数据
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('message/selectArtificerScoreList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.size,
						'artificerName': this.userName,
						// 'content': this.content,
						'phone': this.phone
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData
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
			// 商品跳转
			toGoods(title) {
				this.$router.push({
					path: '/shopAdmin1',
					query: {
						title: title
					}
				})
			},
			
		},
		activated() {
			this.dataSelect()
		}
	};
</script>

<style scoped>
.credit-score-page__filter-input {
	width: 160px;
}

.credit-score-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.credit-score-page__tabs {
	margin-bottom: 0;
}

.credit-score-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.credit-score-page__tabs .nf-table-card {
	margin-bottom: 0;
}

.credit-score-page__tabs >>> .el-tabs__item.is-active {
	color: #5cc477;
}

.credit-score-page__tabs >>> .el-tabs__active-bar {
	background-color: #5cc477;
}

.credit-score-page__link {
	color: #5cc477;
	cursor: pointer;
}

.credit-score-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}
</style>
