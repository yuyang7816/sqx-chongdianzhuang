<template>
	<div class="nf-page comment-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">评价中心</span>
			<h2 class="nf-page-hero__title">评价中心</h2>
			<p class="nf-page-hero__desc">查看与管理用户评论，支持按用户昵称、手机号、评论内容、台球馆名称筛选，可查询、重置、刷新与删除。</p>
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
						class="comment-page__filter-input"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入用户昵称"
						v-model="userName">
					</el-input>
				</el-form-item>
				<el-form-item label="手机号">
					<el-input
						class="comment-page__filter-input"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入手机号"
						v-model="phone">
					</el-input>
				</el-form-item>
				<el-form-item label="评论内容/关键字">
					<el-input
						class="comment-page__filter-input comment-page__filter-input--wide"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入评论内容/关键字"
						v-model="content">
					</el-input>
				</el-form-item>
				<el-form-item label="台球馆名称">
					<el-input
						class="comment-page__filter-input comment-page__filter-input--wide"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入台球馆名称"
						v-model="tearoomName">
					</el-input>
				</el-form-item>
				<el-form-item class="comment-page__filter-actions">
					<el-button type="primary" icon="el-icon-search" @click="select">查询</el-button>
					<el-button type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
					<el-button type="primary" icon="el-icon-refresh" @click="refresh">刷新</el-button>
				</el-form-item>
			</el-form>
		</div>

		<el-tabs v-model="activeName" @tab-click="handleClick" class="comment-page__tabs">
			<el-tab-pane label="评论列表" name="first">
				<div class="nf-table-card">
					<el-table v-loading="tableDataLoading" :data="tableData.list">
						<el-table-column prop="commentId" label="编号" width="80">
						</el-table-column>
						<el-table-column prop="userName" label="用户昵称" width="150">
							<template slot-scope="scope">
								<span
									class="comment-page__link"
									@click="updates(scope.row)">{{scope.row.userName ? scope.row.userName : '未绑定'}}</span>
							</template>
						</el-table-column>
						<el-table-column prop="avatar" label="头像">
							<template slot-scope="scope">
								<img :src="scope.row.avatar" alt="" width="60" height="60">
							</template>
						</el-table-column>
						<el-table-column prop="tearoomName" label="台球馆名称">
						</el-table-column>
						<el-table-column prop="content" label="评论内容">
						</el-table-column>
						<!-- <el-table-column label="评论图片" width="150">
					<template slot-scope="scope">
						<div v-if="scope.row.img == null || scope.row.img == ''">
							暂无图片
						</div>
						<div v-else-if="scope.row.img.includes(',')" style="display:flex;flex-wrap: wrap;">
							<div v-for="item in scope.row.img.split(',')" style="margin: 2px;">
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
									<img style="width: 200px; height: 200px" :src="item" alt="">
								</el-popover>
							</div>
						</div>
						<div v-else>
							<el-popover placement="top-start" title="" trigger="hover">
								<img style="width: 50px; height: 50px" :src="scope.row.img" alt="" slot="reference">
								<img style="width: 200px; height: 200px" :src="scope.row.img" alt="">
							</el-popover>
						</div>
					</template>
				</el-table-column> -->
						<el-table-column prop="score" label="评分">
							<template slot-scope="scope">
								<span>{{scope.row.score?scope.row.score:'暂无评分'}}</span>
							</template>
						</el-table-column>
						<el-table-column prop="createTime" label="创建时间" width="180">
						</el-table-column>
						<el-table-column fixed="right" label="操作" width="150">
							<template slot-scope="scope">
								<el-button size="mini" type="danger" :disabled="!isAuth('comment:delete')"
									@click="deletes(scope.row)">删除
								</el-button>
							</template>
						</el-table-column>
					</el-table>
					<div class="comment-page__pagination">
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
				tearoomName:'',
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
			handleSizeChangeDt(val) {
				this.size = val;
				this.dataSelectDt()
			},
			handleCurrentChangeDt(val) {
				this.page = val;
				this.dataSelectDt()
			},
			// tabs切换
			handleClick(tab, event) {
				this.phone = ''
				this.page = 1
				this.userName = ''
				if (tab._props.label == '评论列表') {
					this.dataSelect()
				}
				if (tab._props.label == '动态评论') {
					this.dataSelectDt()
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
				this.tearoomName = ''
				this.page = 1
				this.dataSelect()
			},
			// 刷新
			refreshDt() {
				this.dataSelectDt()
			},
			//搜索
			selectDt() {
				this.page = 1
				this.dataSelectDt()
			},
			// 重置
			cleansDt() {
				this.userName = ''
				this.phone = ''
				this.content = ''
				this.page = 1
				this.dataSelectDt()
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
			// 删除评论
			deletes(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('comment/deleteComment'),
						method: 'post',
						params: this.$http.adornParams({
							'id':row.commentId
						})
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
			// 获取评论列表数据
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('comment/selectCommentList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.size,
						'userName': this.userName,
						'content': this.content,
						'phone': this.phone,
						'tearoomName':this.tearoomName,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData
				})
			},
			// 获取动态评论
			dataSelectDt() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('comment/selectCommentList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.size,
						'userName': this.userName,
						'content': this.content,
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
			updates(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
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
			// 删除动态评论
			pldeletes(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`gambit/deleteCommentById`),
						method: 'post',
						params: this.$http.adornParams({
							'commentId': row.commentId
						})
					}).then(({
						data
					}) => {
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelectDt()
							}
						})
					})
				})
			},
		},
		activated() {
			this.dataSelect()
		}
	};
</script>

<style scoped>
.comment-page__filter-input {
	width: 160px;
}

.comment-page__filter-input--wide {
	width: 200px;
}

.comment-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.comment-page__tabs {
	margin-bottom: 0;
}

.comment-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.comment-page__tabs .nf-table-card {
	margin-bottom: 0;
}

.comment-page__tabs >>> .el-tabs__item.is-active {
	color: #5cc477;
}

.comment-page__tabs >>> .el-tabs__active-bar {
	background-color: #5cc477;
}

.comment-page__link {
	color: #5cc477;
	cursor: pointer;
}

.comment-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}
</style>
