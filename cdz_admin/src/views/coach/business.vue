<template>
	<div class="nf-page business-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">教练模块</span>
			<h2 class="nf-page-hero__title">投诉中心</h2>
			<p class="nf-page-hero__desc">查看与管理用户投诉记录，支持审核通过/拒绝；维护投诉分类与扣除评分配置。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">
				<i class="el-icon-s-operation nf-filter-card__head-icon"></i>
				<span>快捷操作</span>
			</div>
			<div class="business-page__filter-actions">
				<el-button type="primary" icon="el-icon-refresh" @click="select">刷新</el-button>
			</div>
		</div>

		<el-tabs v-model="activeName" class="business-page__tabs" @tab-click="handleClick">
		<el-tab-pane label="投诉列表" name="first">
			<div class="nf-table-card">
			<el-table v-loading="tableDataLoading" :data="tableData.list">
				<el-table-column fixed prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="userName" label="投诉的用户" width="100">
					<template slot-scope="scope">
						<span class="business-page__user-link" @click="updates(scope.row.userId)">
							{{ scope.row.userName?scope.row.userName:'未绑定'}}
						</span>
					</template>
				</el-table-column>
				<el-table-column prop="byUserName" label="被投诉用户" width="100">
					<template slot-scope="scope">
						<span class="business-page__user-link" @click="updates(scope.row.byUserId)">
							{{ scope.row.byUserName?scope.row.byUserName:'未绑定'}}
						</span>
					</template>
				</el-table-column>
				<el-table-column prop="typeName" label="投诉类型" width="150">
				</el-table-column>
				<el-table-column prop="title" label="投诉标题" width="150">
				</el-table-column>
				<el-table-column prop="content" label="内容" width="150">
				</el-table-column>
				<el-table-column prop="image" label="图片" width="150">
					<template slot-scope="scope">
						<div v-if="scope.row.image == null || scope.row.image == ''">
							暂无图片
						</div>
						<div v-else>
							<div v-for="(item,index) in scope.row.image.split(',')" :key="index"
								style="display: inline-block; margin: 3px;">
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
									<img style="width: 300px; height: auto" :src="item" alt="">
								</el-popover>
							</div>
						</div>
						
					</template>
				</el-table-column>
				<el-table-column prop="auditContent" label="处理结果" align="center">
				</el-table-column>
				<el-table-column prop="createAt" label="投诉时间" width="100"></el-table-column>
				<el-table-column prop="type" label="状态" width="100">
					<template slot-scope="scope">
						<span v-if="scope.row.type=='0'">待审核</span>
						<span v-if="scope.row.type=='1'">通过</span>
						<span v-if="scope.row.type=='2'">拒绝</span>
					</template>
				</el-table-column>
				<el-table-column label="操作" width="100" align="center" fixed="right">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" v-if="scope.row.type=='0'" @click="passClicks1(scope.row)"
							:disabled="!isAuth('business:shenhe')" style="margin:5px">通过
						</el-button>
						<el-button size="mini" type="warning" v-if="scope.row.type=='0'" @click="refuseClick(scope.row)"
							:disabled="!isAuth('business:shenhe')" style="margin:5px">拒绝
						</el-button>
					</template>
				</el-table-column>
			</el-table>
				<div class="business-page__pagination">
					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
					</el-pagination>
				</div>
			</div>
			<!-- 任务拒绝弹框 -->
			<el-dialog title="拒绝" :visible.sync="dialogFormVisible" center>
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
		</el-tab-pane>
		<el-tab-pane label="投诉分类" name="second">
			<div class="nf-table-card">
				<div class="business-page__toolbar">
					<el-button :disabled="!isAuth('business:add')" type="primary"
						icon="document" @click="advertNotice">添加</el-button>
				</div>
			<el-table v-loading="tableDataLoading" :data="guangaoData.list">
				<el-table-column fixed prop="id" label="编号" width="80"></el-table-column>
				<el-table-column prop="title" label="名称">
				</el-table-column>
				<el-table-column prop="content" label="投诉扣除评分">
				</el-table-column>
				<!-- <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column> -->
				<el-table-column label="操作" width="180">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('business:update')"
							@click="advertUpdates(scope.row)">修改</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('business:delete')"
							@click="advertdeletes(scope.row)">删除</el-button>
					</template>
				</el-table-column>
			</el-table>
				<div class="business-page__pagination">
					<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="guangaoData.totalCount">
					</el-pagination>
				</div>
			</div>
			<!-- 添加素材 -->
			<el-dialog title="添加" :visible.sync="dialogFormVisible2" center>

				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">标题：</span>
					<el-input style="width:50%;" v-model="illegal" placeholder="请输入标题" type="text">
					</el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">投诉扣除评分：</span>
					<el-input style="width:50%;" v-model="forfeitMoney" placeholder="请输入投诉扣除评分" type="number">
					</el-input>
				</div>

				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible2 = false">取 消</el-button>
					<el-button type="primary" @click="advertNoticeTo()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 修改素材 -->
			<el-dialog title="修改" :visible.sync="dialogFormVisible1" center>
				<el-form :model="form">
					<el-form-item label="标题：" :label-width="formLabelWidth">
						<el-input style="width:90%;" v-model="form.illegal" placeholder="请输入标题" type="text"></el-input>
					</el-form-item>
				</el-form>
				<el-form :model="form">
					<el-form-item label="投诉扣除评分：" :label-width="formLabelWidth">
						<el-input style="width:90%;" v-model="form.forfeitMoney" placeholder="请输入投诉扣除评分" type="number">
						</el-input>
					</el-form-item>
				</el-form>

				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible1 = false">取 消</el-button>
					<el-button type="primary" @click="amendadvertTo()">确 定</el-button>
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
			}
		},
		methods: {
			// 多选
			changeFun(val) {
				this.checkBoxData = val
			},
			//处理默认选中当前日期
			// getNowTime () {
			// 	var now = new Date()
			// 	var year = now.getFullYear() //得到年份
			// 	var month = now.getMonth() //得到月份
			// 	var date = now.getDate() //得到日期
			// 	var hh = now.getHours() < 10 ? '0' + now.getHours() : now.getHours()
			// 	var mm = now.getMinutes() < 10 ? '0' + now.getMinutes() : now.getMinutes()
			// 	var ss = now.getSeconds() < 10 ? '0' + now.getSeconds() : now.getSeconds()
			// 	month = month + 1
			// 	month = month.toString().padStart(2, '0')
			// 	date = date.toString().padStart(2, '0')
			// 	var defaultDate = `${year}-${month}-${date} ${hh}:${mm}:${ss}`
			// 	return defaultDate
			// 	this.$set(this.info, 'stockDate', defaultDate)
			// },
			// tabs切换
			handleClick(tab, event) {
				if (tab._props.label == '投诉列表') {
					this.page = 1
					this.limit = 10
					this.dataSelect()
				}
				if (tab._props.label == '投诉分类') {
					this.page = 1
					this.limit = 10
					this.guangaoSelect()
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
			handleSizeChange1(val) {
				this.limit = val
				this.guangaoSelect()
			},
			handleCurrentChange1(val) {
				this.page = val
				this.guangaoSelect()
			},
			// 查询资源列表
			select() {
				this.page = 1
				this.limit = 10
				this.dataSelect()
			},
			// 重置资源列表
			cleans() {
				this.indentNumber = ''
				this.complaintState = ''
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

			// 删除banner图
			deletes(row) {
				let delid = row.ordersId
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`order/deleteOrders?ids=${delid}`),
						method: 'get',
						data: this.$http.adornData({})
					}).then(({
						data
					}) => {
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					})
				}).catch(() => {})
			},
			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl(`message/page/7/${this.page}/${this.limit}`),
					method: 'get',
					params: this.$http.adornParams({})
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
			//任务拒绝
			refuseClick(rows) {
				this.status = 2
				this.helpTakeId = rows.id
				this.content = ''
				this.dialogFormVisible = true
			},
			refuseto() {
				if (this.content == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入拒绝理由',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl('message/auditMessage'),
					method: 'post',
					params: this.$http.adornParams({
						'messageId': this.helpTakeId,
						'status': this.status,
						'auditContent': this.content,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisible = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.content = ''
								this.status = -1
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
			},
			//单条任务通过
			passClicks(row) {
				this.content = '同意'
				this.$http({
					url: this.$http.adornUrl('message/auditMessage'),
					method: 'post',
					params: this.$http.adornParams({
						'messageId': row.id,
						'status': 1,
						'auditContent': this.content,
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
								this.content = ''
								this.status = -1
								this.dataSelect()
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'error',
							duration: 1500,
							onClose: () => {
								this.content = ''
								this.status = -1
								this.dataSelect()
							}
						})
					}

				})
			},
			//单条投诉通过
			passClicks1(row) {
				this.status = 1
				this.content = '同意'
				this.$http({
					url: this.$http.adornUrl('message/auditMessage'),
					method: 'post',
					params: this.$http.adornParams({
						'messageId': row.id,
						'status': 1,
						'auditContent': this.content,
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
								this.content = ''
								this.status = -1
								this.dataSelect()
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'error',
							duration: 1500,
							onClose: () => {
								this.content = ''
								this.status = -1
								this.dataSelect()
							}
						})
					}

				})
			},
			// 添加素材弹框
			advertNotice() {
				this.dialogFormVisible2 = true
			},
			// 添加
			advertNoticeTo() {
				if (this.illegal == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入名称',
						type: 'warning'
					})
					return
				}
				if (this.forfeitMoney === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入投诉扣除评分',
						type: 'warning'
					})
					return
				}

				this.$http({
					url: this.$http.adornUrl('message/insertMessage'),
					method: 'post',
					data: this.$http.adornData({
						'title': this.illegal,
						'content': this.forfeitMoney,
						'state': 6
					})
				}).then(({
					data
				}) => {
					this.dialogFormVisible2 = false
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.illegal = ''
							this.guangaoSelect()
						}
					})
				})
			},
			// 点击修改
			advertUpdates(rows) {
				this.form.id = rows.id
				this.form.illegal = rows.title
				this.form.forfeitMoney = rows.content
				this.dialogFormVisible1 = true
			},
			// 确定修改
			amendadvertTo() {
				if (this.form.illegal == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入名称',
						type: 'warning'
					})
					return
				}
				if (this.form.forfeitMoney == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入投诉扣除评分',
						type: 'warning'
					})
					return
				}

				this.$http({
					url: this.$http.adornUrl('message/updateMessage'),
					method: 'post',
					data: this.$http.adornData({
						'id': this.form.id,
						'title': this.form.illegal,
						'content': this.form.forfeitMoney
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
								this.guangaoSelect()
								this.dialogFormVisible1 = false
							}
						})
					} else {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: data.msg,
							type: 'warning'
						})
					}

				})
			},
			// 删除广告
			advertdeletes(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('message/deleteMessageById'),
						method: 'post',
						params: this.$http.adornParams({
							'id': row.id
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
									this.guangaoSelect()
								}
							})
						} else {
							this.$notify({
								title: '提示',
								duration: 1800,
								message: data.msg,
								type: 'warning'
							})
						}

					})
				})
			},
			// 获取素材数据
			guangaoSelect() {
				let page = this.page
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl(`message/page/6/${this.page}/${this.limit}`),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.guangaoData = returnData
					if(this.guangaoData.list.length==0){
						if(this.page>1){
							this.page = this.page-1
							this.guangaoSelect()
						}
					}
				})
			},
			// /
			guangaoSelect1() {
				let page = this.page
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/illegalType/selectIllegalTypeList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.illegalList = returnData

				})
			},
		},
		mounted() {
			this.dataSelect()
			// this.guangaoSelect1()
		}
	}
</script>

<style scoped>
.business-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.business-page__tabs {
	margin-bottom: 0;
}

.business-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.business-page__tabs .nf-table-card {
	margin-bottom: 0;
}

.business-page__tabs >>> .el-tabs__item.is-active {
	color: #5cc477;
}

.business-page__tabs >>> .el-tabs__active-bar {
	background-color: #5cc477;
}

.business-page__toolbar {
	display: flex;
	justify-content: flex-end;
	margin-bottom: 12px;
}

.business-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

.business-page__user-link {
	color: #5cc477;
	cursor: pointer;
}

.customWidth {
	width: 80% !important;
}
</style>
