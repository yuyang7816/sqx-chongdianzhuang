<template>
	<div class="nf-page chat-record-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">聊天记录</span>
			<h2 class="nf-page-hero__title">聊天记录</h2>
			<p class="nf-page-hero__desc">查看与管理用户 AI 聊天记录，支持按用户昵称、问题、答案筛选，可查询、重置、添加、修改与删除。</p>
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
						class="chat-record-page__filter-input"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入用户昵称"
						v-model="nickName">
					</el-input>
				</el-form-item>
				<el-form-item label="问题">
					<el-input
						class="chat-record-page__filter-input"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入问题"
						v-model="prompt">
					</el-input>
				</el-form-item>
				<el-form-item label="答案">
					<el-input
						class="chat-record-page__filter-input"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入答案"
						v-model="answer">
					</el-input>
				</el-form-item>
				<el-form-item class="chat-record-page__filter-actions">
					<el-button type="primary" icon="el-icon-search" @click="select">查询</el-button>
					<el-button type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
					<el-button type="primary" icon="el-icon-plus" :disabled="!isAuth('chatRecord:add')" @click="addBtn()">添加</el-button>
				</el-form-item>
			</el-form>
		</div>

		<el-tabs value="first" class="chat-record-page__tabs">
			<el-tab-pane label="聊天记录列表" name="first">
				<div class="nf-table-card">
					<el-table v-loading="tableDataLoading" :data="tableData.records">
						<el-table-column fixed prop="chatId" label="id" width="80">
						</el-table-column>
						<el-table-column prop="userName" label="用户昵称" width="150">
							<template slot-scope="scope">
								<span
									class="chat-record-page__link"
									v-if="scope.row.userEntity"
									@click="updates(scope.row.userId)">
									{{scope.row.userEntity.userName ? scope.row.userEntity.userName : '未绑定'}}
								</span>
							</template>
						</el-table-column>
						<el-table-column prop="picture" label="用户头像" width="80">
							<template slot-scope="scope">
								<div v-if="scope.row.userEntity && scope.row.userEntity.avatar">
									<el-popover placement="top-start" title="" trigger="hover">
										<img style="width: 50px; height: 50px" :src="scope.row.userEntity.avatar" alt=""
											slot="reference">
										<img style="width: 200px; height: 200px" :src="scope.row.userEntity.avatar" alt="">
									</el-popover>
								</div>
								<div v-else>
									暂无图片
								</div>
							</template>
						</el-table-column>
						<el-table-column prop="prompt" label="问题">
							<template slot-scope="scope">
								<div>
									<el-popover placement="top-start" title="" trigger="hover">
										<div class="chat-record-page__text-clamp" slot="reference">{{scope.row.prompt}}</div>
										<div style="width: 200px;height: auto;">{{scope.row.prompt}}</div>
									</el-popover>
									<span @click="copys(scope.row.prompt)" class="chat-record-page__copy-btn">
										<icon-svg name="bianji" class="el-icon-document-copy"></icon-svg>
									</span>
								</div>
							</template>
						</el-table-column>
						<el-table-column prop="answer" label="答案">
							<template slot-scope="scope">
								<div>
									<div v-if="scope.row.messageType==1">
										<div>
											<el-popover placement="top-start" title="" trigger="hover">
												<div class="chat-record-page__text-clamp" slot="reference">{{scope.row.answer}}
												</div>
												<div style="width: 400px;height: auto;word-break: break-all;max-height: 500px;overflow: auto">
													{{scope.row.answer}}
												</div>
											</el-popover>
										</div>
										<span @click="copys(scope.row.answer)" class="chat-record-page__copy-btn">
											<icon-svg name="bianji" class="el-icon-document-copy"></icon-svg>
										</span>
									</div>
								</div>
							</template>
						</el-table-column>
						<el-table-column prop="createTime" label="创建时间" width="160">
						</el-table-column>
						<el-table-column label="操作" width="170">
							<template slot-scope="scope">
								<el-button size="mini" type="primary" :disabled="!isAuth('chatRecord:update')" @click="addBtn(scope.row)">修改
								</el-button>
								<el-button size="mini" type="danger" :disabled="!isAuth('chatRecord:delete')"
									@click="deleteStair(scope.row)">删除
								</el-button>
							</template>
						</el-table-column>
					</el-table>
					<div class="chat-record-page__pagination">
						<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
							:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
							layout="total,sizes, prev, pager, next" :total="tableData.total">
						</el-pagination>
					</div>
				</div>
			</el-tab-pane>
		</el-tabs>

		<!-- 添加、修改弹框 -->
		<el-dialog :title="titles" :visible.sync="dialogFormVisibleOpen" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">用户昵称：</span>
				<el-input style="width: 50%;" v-model="nickNames"  @focus="userselect" type="text" placeholder="请选择用户"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">问题：</span>
				<el-input style="width: 50%;" v-model="fromDatas.prompt" type="textarea" :rows="3" placeholder="请输入问题"></el-input>
			</div>
			<div style="margin-bottom: 10px;" v-if="fromDatas.messageType==1">
				<span style="width: 200px;display: inline-block;text-align: right;">答案：</span>
				<el-input style="width: 50%;" v-model="fromDatas.answer" type="textarea" :rows="4" placeholder="请输入答案"></el-input>
			</div>
			<div style="margin-bottom: 10px;" v-if="fromDatas.messageType==2">
				<span style="width: 200px;display: inline-block;text-align: right;">答案：</span>
				<div style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;display: inline-block;">
					<el-upload class="avatar-uploader" v-model="fromDatas.answer"
						action="https://admin.cloudshequ.online/sqx_fast/alioss/upload" :show-file-list="false"
						:on-success="handleAvatarSuccess1">
						<img v-if="fromDatas.answer" :src="fromDatas.answer" class="avatar" style="width: 148px;height: 148px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon" style="font-size: 28px;color: #8c939d"></i>
					</el-upload>
				</div>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisibleOpen = false">取 消</el-button>
				<el-button type="primary" @click="phoneNoticeToOpen()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 用户列表 -->
		<el-dialog title="用户列表" :visible.sync="dialogFormVisible3" center width="80%">
			<div style="margin:2% 0;display: inline-block;">
				<div style="margin:5px;display: inline-block;">
					<span>手机号:</span>
				<el-input style="width: 150px;" @keydown.enter.native="userclick" clearable placeholder="请输入手机号"
					v-model="phoneU"></el-input>&nbsp;&nbsp;&nbsp;&nbsp;
				</div>
				<div style="margin:5px;display: inline-block;">
					<span>昵称:</span>
				<el-input style="width: 150px;" @keydown.enter.native="userclick" clearable placeholder="请输入昵称"
					v-model="nickNameU"></el-input>
				</div>
				<div style="margin:5px;display: inline-block;">
					<span>邀请码:</span>
					<el-input style="width: 150px;" @keydown.enter.native="userclick" placeholder="请输入邀请码"
						v-model="invitationCodeU">
					</el-input>
				</div>
				<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="userclick">
					查询
				</el-button>
				<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="userclose">
					重置
				</el-button>
			</div>
			<el-table  v-loading="tableDataLoading4" :data="userIds.list">
				<el-table-column prop="userId" width="80" label="编号">
				</el-table-column>
				<el-table-column prop="userName" width="220" label="昵称">
					<template slot-scope="scope">
						<span style="color: #f56c6c;">{{scope.row.userName ? scope.row.userName : '未设置'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="phone" width="220" label="手机号">
					<template slot-scope="scope">
						<span>{{scope.row.phone ? scope.row.phone : '未绑定'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="invitationCode"  label="邀请码">
				</el-table-column>
				<el-table-column prop="createTime" width="220" label="创建时间">
				</el-table-column>
				<el-table-column label="操作" fixed='right' width="120">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" @click="confirm(scope.row)">确定
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange2" @current-change="handleCurrentChange2"
					:page-sizes="[5, 10, 15, 20]" :page-size="limitU" :current-page="pageU"
					layout="total,sizes, prev, pager, next,jumper" :total="userIds.totalCount">
				</el-pagination>
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
				userId: '',
				nickName: '',
				tableDataLoading: false,
				tableData: {},
				answer: '',
				prompt: '',
				openValue:1,
				closeValue:2,
				good:'',
				classifyStates: [{
						label: '全部',
						value: ''
					},
					{
						label: '是',
						value: 1
					},
					{
						label: '否',
						value: 2
					}
				],
				fromDatas:{
					chatId:'',
					userId:'',
					prompt:'',
					answer:'',
					messageType:1,
					good:'',
				},
				nickNames:'',
				titles:'',
				dialogFormVisibleOpen:false,
				nickNameU: '',
				
				dialogFormVisible3:false,
				tableDataLoading4:false,
				nickNameU: '',
				userIds: {}, //用户列表
				pageU:1,
				limitU:10,
				phoneU:'',
				invitationCodeU:'',
				phone:'',
				messageTypeT:'',
				messageStates:[
					{
							label: '全部',
							value: ''
						},
						{
							label: '文本',
							value: 1
						},
						{
							label: '图片',
							value: 2
						}
				],
				
			}
		},
		methods: {
			// 复制
			copys(number) {
				 const textarea = document.createElement('textarea');
				      textarea.value = number;
				      document.body.appendChild(textarea);
				      textarea.select();
				      try {
				        document.execCommand('copy'); // 注意：这是一个废弃的方法，但仍可用于兼容性处理。
				        this.$message({
				        	message: '复制成功',
				        	type: 'success'
				        });
				      } catch (err) {
				        console.error('复制失败', err);
				        this.$message.error('复制失败，请手动复制');
				      } finally {
				        document.body.removeChild(textarea); // 清理DOM中的临时元素。
				      }
				
				// return
				// this.$copyText(number).then(()=> {
				// 	this.$message({
				// 		message: '复制成功',
				// 		type: 'success'
				// 	});
				// }, ()=> {
				// 	this.$message.error('复制失败');
				// })
			},
			$copytext(text) {
			       navigator.clipboard.writeText(text).then(() => {
			        console.log('文本已复制');
			      }, (err) => {
			        console.error('无法复制文本: ', err);
			      });
			    },
			handleSizeChange(val) {
				this.limit = val;
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.dataSelect()
			},
			handleSizeChange2(val) {
				this.limitU = val
				this.userClass()
			},
			handleCurrentChange2(val) {
				this.pageU = val
				this.userClass()
			},
			// 用户详情跳转
			updates(userid) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: userid
					}
				})
			},
			// 查询
			select() {
				this.page = 1
				this.dataSelect()
			},
			// 重置
			cleans() {
				this.page = 1
				this.nickName = ''
				this.answer = ''
				this.prompt = ''
				this.good = ''
				// this.messageTypeT = ''
				this.dataSelect()
			},
			// 获取聊天会话列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/question/getChatList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'nickName': this.nickName,
						'prompt': this.prompt,
						'answer': this.answer,
						'good':this.good,
						'messageType':this.messageTypeT,
						'type':1,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData
					if (this.page > 1) {
					if (data.data.records.length == 0) {
						this.page = this.page-1
							this.dataSelect()
						}
					}

				})
			},
			//删除
			deleteStair(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/question/deleteChat'),
						method: 'post',
						params: this.$http.adornParams({
							'chatId': row.chatId
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
								type: 'error',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
						}

					})
				}).catch(() => {})
			},
			// 是否热搜
			change(val, row) {
				this.$http({
					url: this.$http.adornUrl('admin/question/updateChat'),
					method: 'post',
					data: this.$http.adornData({
						'chatId':row.chatId,
						'good':val
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
							type: 'error',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					}
					
				})
			},
			// 添加/修改 弹框
			addBtn(row){
				if(row){
					this.titles = '修改'
					this.fromDatas.chatId = row.chatId
					this.fromDatas.userId = row.userId
					this.fromDatas.prompt = row.prompt
					this.fromDatas.answer = row.answer
					this.fromDatas.messageType = row.messageType
					this.fromDatas.good = row.good
					this.nickNames = row.userEntity.userName
				}else{
					this.titles = '添加'
					this.fromDatas.chatId = ''
					this.fromDatas.userId = ''
					this.fromDatas.prompt = ''
					this.fromDatas.answer = ''
					this.fromDatas.messageType = 1
					this.fromDatas.good = ''
					this.nickNames = ''
				}
				this.dialogFormVisibleOpen = true
			},
			// 确认添加/修改
			phoneNoticeToOpen(){
				if (this.fromDatas.userId === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择用户',
						type: 'warning'
					})
					return
				}
				if (this.fromDatas.prompt == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入问题',
						type: 'warning'
					})
					return
				}
				
				if (this.fromDatas.messageType == 1) {
					if (this.fromDatas.answer == '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入答案',
							type: 'warning'
						})
						return
					}
					
				}
				
				if(this.titles == '添加'){
					var url = 'admin/question/saveChat'
				}else{
					var url = 'admin/question/updateChat'
				}
				this.$http({
					url: this.$http.adornUrl(url),
					method: 'post',
					data: this.$http.adornData(this.fromDatas)
				}).then(({
					data
				}) => {
					if(data.code==0){
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dialogFormVisibleOpen = false
								this.dataSelect()
							}
						})
					}else{
						this.$message({
							message: data.msg,
							type: 'error',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					}
					
				})
			},
			handleAvatarSuccess1(file, fileList) {
				this.fromDatas.answer = file.data
			},
			// 获取用户列表弹框
			userselect() {
				this.dialogFormVisible3 = true
				
				this.userClass()
			},
			// 获取用户列表
			userClass() {
				this.tableDataLoading4 = true
				this.$http({
					url: this.$http.adornUrl('user/selectUserList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.pageU,
						'limit': this.limitU,
						'userType': '',
						'phone': this.phoneU,
						'userName': this.nickNameU,
						'invitationCode': this.invitationCodeU,
						'platform': '',
					})
					
					
				}).then(({
					data
				}) => {
					this.tableDataLoading4 = false
					let returnData = data.data
					this.userIds = returnData
				})
			},
			// 查询用户列表
			userclick() {
				this.page = 1
				this.userClass()
			},
			// 重置用户列表数据
			userclose() {
				this.phoneU = ''
				this.nickNameU = ''
				this.invitationCodeU = ''
				this.userClass()
			},
			// 确定用户
			confirm(row) {
				this.fromDatas.userId = row.userId
				this.nickNames = row.userName
				this.dialogFormVisible3 = false
			},
		},
		mounted() {
			this.dataSelect()
		}
	};
</script>

<style scoped>
.chat-record-page__filter-input {
	width: 160px;
}

.chat-record-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.chat-record-page__tabs {
	margin-bottom: 0;
}

.chat-record-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.chat-record-page__tabs .nf-table-card {
	margin-bottom: 0;
}

.chat-record-page__tabs >>> .el-tabs__item.is-active {
	color: #5cc477;
}

.chat-record-page__tabs >>> .el-tabs__active-bar {
	background-color: #5cc477;
}

.chat-record-page__link {
	color: #5cc477;
	cursor: pointer;
}

.chat-record-page__copy-btn {
	color: #5cc477;
	cursor: pointer;
	margin: 5px;
	font-size: 18px;
}

.chat-record-page__text-clamp {
	display: -webkit-box;
	-webkit-box-orient: vertical;
	-webkit-line-clamp: 3;
	overflow: hidden;
	max-height: 80px;
}

.chat-record-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

.nf-table-card .el-button + .el-button {
	margin-left: 0 !important;
	margin-top: 5px !important;
}
</style>
