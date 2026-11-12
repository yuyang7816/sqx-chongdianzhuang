<template>
	<div class="nf-page face-recognition-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">消息管理</span>
			<h2 class="nf-page-hero__title">人脸识别记录</h2>
			<p class="nf-page-hero__desc">查看人脸识别开台记录，支持按状态、用户昵称、手机号筛选查询，可查看用户 AI 分析详情。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">
				<i class="el-icon-search nf-filter-card__head-icon"></i>
				<span>筛选条件</span>
			</div>
			<el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
				<el-form-item label="状态">
					<el-select
						class="face-recognition-page__filter-select"
						v-model="statusT"
						placeholder="请选择状态"
						@change="select()">
						<el-option v-for="item in statesnum" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="用户昵称">
					<el-input
						class="face-recognition-page__filter-input"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入用户昵称"
						v-model="userNameT">
					</el-input>
				</el-form-item>
				<el-form-item label="手机号">
					<el-input
						class="face-recognition-page__filter-input"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入手机号"
						v-model="phoneT">
					</el-input>
				</el-form-item>
				<el-form-item class="face-recognition-page__filter-actions">
					<el-button type="primary" icon="el-icon-search" @click="select">查询</el-button>
					<el-button type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
				</el-form-item>
			</el-form>
		</div>

		<el-tabs v-model="activeName" @tab-click="handleClick" class="face-recognition-page__tabs">
			<el-tab-pane label="记录列表" name="first">
				<div class="nf-table-card">
					<el-table v-loading="tableDataLoading" :data="tableData.list">
						<el-table-column prop="id" label="编号" width="80">
						</el-table-column>
						<el-table-column prop="userName" label="用户昵称" width="150">
							<template slot-scope="scope">
								<span class="face-recognition-page__link"
									@click="updataDetails(scope.row,scope.row.userEntity)">
									{{ scope.row.userName ? scope.row.userName : '未绑定' }}
								</span>
							</template>
						</el-table-column>
						<el-table-column prop="title" label="请求标识">
						</el-table-column>
						<el-table-column prop="auditContent" label="开台信息">
						</el-table-column>
						<el-table-column prop="createAt" label="创建时间" width="160">
						</el-table-column>
						<el-table-column prop="status" label="开台是否成功">
							<template slot-scope="scope">
								<span class="face-recognition-page__status--success" v-if="scope.row.status == 1">开台成功</span>
								<span class="face-recognition-page__status--fail" v-if="scope.row.status == 2">开台失败</span>
							</template>
						</el-table-column>
						<el-table-column fixed="right" label="详细信息" width="120">
							<template slot-scope="scope">
								<el-button size="mini" type="primary" @click="qitauser(scope.row.content)">用户分析</el-button>
							</template>
						</el-table-column>
					</el-table>
					<div class="face-recognition-page__pagination">
						<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
							:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
							layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
						</el-pagination>
					</div>
				</div>
			</el-tab-pane>
		</el-tabs>

		<el-dialog title="审核处理" :visible.sync="dialogFormVisible" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">审核结果：</span>
				<el-radio-group v-model="radio">
					<el-radio :label="0">通过</el-radio>
					<el-radio :label="1">驳回</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 10px;" v-if="radio===1">
				<span
					style="width: 200px;display: inline-block;text-align: right;position: relative;top: -70px;">驳回理由：</span>
				<el-input style="width:50%;" v-model="content" type="textarea" :rows="4" placeholder="请输入驳回理由">
				</el-input>
			</div>
			<div style="margin-bottom: 10px;" v-if="radio===0">
				<span style="width: 200px;display: inline-block;text-align: right;">处理结果：</span>
				<el-radio-group v-model="clStatus">
					<el-radio :label="0">封号</el-radio>
					<el-radio :label="1">下架</el-radio>
				</el-radio-group>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="refuseto()">确 定</el-button>
			</div>
		</el-dialog>
		<el-dialog title="AI分析详情" :visible.sync="dialogFormVisibleFx" center width="80%">
			<div class="detailtabel">
				<div class="_th1">AI分析</div>
				<div class="table_main">
					<table>
						<tbody>
							<tr>
								<th>分析的年龄</th>
								<td>
									<div v-if="xiangqingData.attribute">
										{{xiangqingData.attribute.age?xiangqingData.attribute.age:0}}
									</div>
								</td>
								<th>是否戴帽子</th>
								<td>
									<div v-if="xiangqingData.attribute">
										<span
											v-if="xiangqingData.attribute.cap&&xiangqingData.attribute.cap.value==1">带帽</span>
										<span v-else>没戴</span>
									</div>
								</td>
								<th>心情</th>
								<td class="border-rt"> <!-- 1 生气  2开心 3悲伤 4平静 5惊讶 -->
									<div v-if="xiangqingData.attribute">
										<span
											v-if="xiangqingData.attribute.expression&&xiangqingData.attribute.expression.value==1">生气</span>
										<span
											v-else-if="xiangqingData.attribute.expression&&xiangqingData.attribute.expression.value==2">开心</span>
										<span
											v-else-if="xiangqingData.attribute.expression&&xiangqingData.attribute.expression.value==3">悲伤</span>
										<span
											v-else-if="xiangqingData.attribute.expression&&xiangqingData.attribute.expression.value==4">平静</span>
										<span
											v-else-if="xiangqingData.attribute.expression&&xiangqingData.attribute.expression.value==5">惊讶</span>
										<span v-else>-</span>
									</div>

								</td>
							</tr>
							<tr>

								<th>性别</th>
								<td style="color: rgb(245, 108, 108)">
									<div v-if="xiangqingData.attribute">
										<span
											v-if="xiangqingData.attribute.gender&&xiangqingData.attribute.gender.value==1">男</span>
										<span v-else>女</span>
									</div>

								</td>
								<th>是否带眼镜</th>
								<td>
									<div v-if="xiangqingData.attribute">
										<span
											v-if="xiangqingData.attribute.glasses&&xiangqingData.attribute.glasses.value==1">带了</span>
										<span v-else>没带</span>
									</div>

								</td>

								<th>是否是活体</th>
								<td class="border-rt">
									<div v-if="xiangqingData.attribute">
										<span
											v-if="xiangqingData.attribute.liveness&&xiangqingData.attribute.liveness.value==1">是</span>
										<span v-else>不是</span>
									</div>

								</td>
							</tr>
							<tr>
								<th class="border-bt">是否带口罩</th>
								<td class="border-bt">
									<div v-if="xiangqingData.attribute">
										<span
											v-if="xiangqingData.attribute.mask&&xiangqingData.attribute.mask.value==1">带了</span>
										<span v-else>没带</span>
									</div>

								</td>
								<th class="border-bt">是否有胡子</th>
								<td class="border-bt">
									<div v-if="xiangqingData.attribute">
										<span
											v-if="xiangqingData.attribute.mustache&&xiangqingData.attribute.mustache.value==1">有</span>
										<span v-else>没有</span>
									</div>

								</td>
								<th class="border-bt"></th>
								<td class="border-bt border-rt">
								</td>
							</tr>
						</tbody>
					</table>
				</div>
				<div class="_th1">用户档案信息</div>
				<div class="table_main">
					
					<table>
						<tbody>
							<tr>
								<th>编号</th>
								<td>
									<div v-if="xiangqingData.registrationInfo">
										{{xiangqingData.registrationInfo.personId ? xiangqingData.registrationInfo.personId : '-'}}
									</div>
				
								</td>
								<th>姓名</th>
								<td>
									<div v-if="xiangqingData.registrationInfo">
										{{xiangqingData.registrationInfo.name ? xiangqingData.registrationInfo.name : '-'}}
									</div>
				
								</td>
								<th>手机号</th>
								<td class="border-rt">
									<div v-if="xiangqingData.registrationInfo">
										{{xiangqingData.registrationInfo.phone ? xiangqingData.registrationInfo.phone : '-'}}
									</div>
				
								</td>
							</tr>
							<tr>
								<th>年龄</th>
								<td>
									<div v-if="xiangqingData.registrationInfo">
										{{xiangqingData.registrationInfo.age ? xiangqingData.registrationInfo.age : '-'}}
									</div>
				
								</td>
								<th>性别</th>
								<td>
									<div v-if="xiangqingData.registrationInfo">
										{{xiangqingData.registrationInfo.gender ? xiangqingData.registrationInfo.gender : '-'}}
									</div>
				
								</td>
								<th>身份证</th>
								<td class="border-rt">
									<div v-if="xiangqingData.registrationInfo">
										{{xiangqingData.registrationInfo.certificateNumber ? xiangqingData.registrationInfo.certificateNumber : '-'}}
									</div>
				
								</td>
							</tr>
							<tr>
								<th class="border-bt">邮箱</th>
								<td class="border-bt">
									<div v-if="xiangqingData.registrationInfo">
										{{xiangqingData.registrationInfo.email ? xiangqingData.registrationInfo.email : '-'}}
									</div>
				
								</td>
								<th class="border-bt">规则</th>
								<td class="border-bt">
									<div v-if="xiangqingData.registrationInfo">
										{{xiangqingData.registrationInfo.ruleId ? xiangqingData.registrationInfo.ruleId : '-'}}
									</div>
				
								</td>
								<th class="border-bt">规则名称</th>
								<td class="border-bt border-rt">
									<div v-if="xiangqingData.registrationInfo">
										{{xiangqingData.registrationInfo.ruleName ? xiangqingData.registrationInfo.ruleName : '-'}}
									</div>
				
								</td>
							</tr>
				
						</tbody>
					</table>
				</div>
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
				state: 410,
				content: '',

				activeName: 'first',
				tableDataLoading: false,
				dialogFormVisible: false,
				tableData: [],
				radio: '',
				clStatus: '',
				messageId: '',
				platform: '',
				platform1: '',
				status: '',
				userNameT:'',
				phoneT:'',
				statusT: '',
				statesnum: [{
						value: '',
						label: '全部'
					},
					{
						value: 1,
						label: '开台成功'
					}, {
						value: 2,
						label: '开台失败'
					},
				],
				xiangqingData: {},
				dialogFormVisibleFx: false,

			}
		},
		methods: {
			// 详情跳转
			updataDetails(row, userEntity) {
				if (userEntity != null) {
					this.$router.push({
						path: '/userDetail',
						query: {
							userId: row.userId
						}
					});
				} else {
					this.$message({
						message: '当前用户已注销',
						type: 'warning',
						duration: 1500,
						onClose: () => {}
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

			handleClick(tab, event) {
				if (tab._props.label == '记录列表') {
					this.page = 1
					this.limit = 10
					this.state = 410
					this.dataSelect()
				}
			},

			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl(`message/selectMessageByType`),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'state': this.state,
						'platform': this.platform1,
						'status': this.statusT,
						'userName': this.userNameT,
						'phone': this.phoneT,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					for (var i in data.data.list) {
						var a = data.data.list[i].content.toString()
						var b = JSON.parse(a)
						data.data.list[i].content = b
						console.log('key', b)
					}
					let returnData = data.data;
					this.tableData = returnData

				})
			},
			// 注销
			deleteuser(row) {
				let delid = row.userId
				this.$confirm(`确定要注销该用户?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`user/deleteUserByUserId/${delid}`),
						method: 'post',
						params: this.$http.adornParams({
							'messageId': row.id
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
				})
			},

			//审核弹框
			refuseClick(rows) {
				this.radio = ''
				this.clStatus = ''
				this.content = ''
				this.messageId = rows.id
				this.platform = rows.platform
				this.dialogFormVisible = true
			},
			//提交审核
			refuseto() {
				if (this.radio === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择审核结果',
						type: 'warning'
					});
					return
				}
				if (this.radio === 1) {
					if (this.content == '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入拒绝理由',
							type: 'warning'
						});
						return
					}
				}
				if (this.radio === 0) {
					if (this.clStatus === '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请选择处理结果',
							type: 'warning'
						});
						return
					}
				}
				var status = ''
				if (this.radio === 1) {
					status = 1
				} else {
					if (this.clStatus === 0) {
						status = 2
					}
					if (this.clStatus === 1) {
						status = 3
					}
				}

				this.$http({
					url: this.$http.adornUrl('message/auditMessage'),
					method: 'post',
					params: this.$http.adornParams({
						'messageId': this.messageId,
						'status': status,
						'auditContent': this.content
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
			select() {
				this.page = 1
				this.dataSelect()
			},
			cleans(){
				this.statusT = ''
				this.userNameT = ''
				this.phoneT = ''
				this.page = 1
				this.dataSelect()
			},
			dataSelect1() {
				if (this.$route.query.status != undefined) {
					this.statusT = Number(this.$route.query.status)
				} else {
					this.statusT = ''
				}
				this.dataSelect()
			},
			qitauser(row) {
				this.xiangqingData = row
				this.dialogFormVisibleFx = true
			}
		},
		mounted() {
			if (this.$route.query.status != undefined) {
				this.statusT = Number(this.$route.query.status)
			} else {
				this.statusT = ''
			}
			this.dataSelect()
		},
		watch: {
			'$route': 'dataSelect1'
		}
	};
</script>


<style scoped>
.face-recognition-page__filter-select {
	width: 150px;
}

.face-recognition-page__filter-input {
	width: 160px;
}

.face-recognition-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.face-recognition-page__tabs {
	margin-bottom: 0;
}

.face-recognition-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.face-recognition-page__tabs .nf-table-card {
	margin-bottom: 0;
}

.face-recognition-page__tabs >>> .el-tabs__item.is-active {
	color: #5cc477;
}

.face-recognition-page__tabs >>> .el-tabs__active-bar {
	background-color: #5cc477;
}

.face-recognition-page__link {
	color: #5cc477;
	cursor: pointer;
}

.face-recognition-page__link:hover {
	color: #3fae5c;
}

.face-recognition-page__status--success {
	color: #5cc477;
}

.face-recognition-page__status--fail {
	color: #F56C6C;
}

.face-recognition-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

.detailtabel h2 {
	margin-top: 0;
}

.detailtabel .table_main table {
	width: 100%;
}

.detailtabel .table_main {
	border: 1px solid #e8e8e8;
}

.detailtabel table tr {
	border-bottom: 1px solid #e8e8e8;
}

.detailtabel table tr th {
	background-color: #fafafa;
	padding: 16px 24px;
	border-right: 1px solid #e8e8e8;
	border-bottom: 1px solid #e8e8e8;
}

.detailtabel table tr td {
	padding: 16px 24px;
	border-right: 1px solid #e8e8e8;
	border-bottom: 1px solid #e8e8e8;
}

.detailtabel table th {
	color: rgba(0, 0, 0, .85);
	font-weight: 400;
	font-size: 14px;
	line-height: 1.5;
}

.border-rt {
	border-right: none !important;
}

.border-bt {
	border-bottom: none !important;
}

.table_main tr td.border-rt {
	width: 20%;
}

._th1 {
	width: 100%;
	font-size: 16px;
	font-weight: 600;
	color: #999;
	margin: 10px;
}
</style>
