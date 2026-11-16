<template>
	<div class="nf-page message-board-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">话题论坛</span>
			<h2 class="nf-page-hero__title">留言板管理</h2>
			<p class="nf-page-hero__desc">查看与管理用户留言内容，支持按状态、昵称、联系电话、内容及时间范围筛选，可添加、修改与删除留言。</p>
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
						class="message-board-page__filter-select"
						v-model="statusT"
						placeholder="请选择状态"
						@change="handleClick()">
						<el-option v-for="item in statuss" :key="item.value" :label="item.title" :value="item.id">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="昵称">
					<el-input
						class="message-board-page__filter-input"
						@keydown.enter.native="handleClick"
						clearable
						placeholder="请输入发布人昵称"
						v-model="userNameT">
					</el-input>
				</el-form-item>
				<el-form-item label="联系电话">
					<el-input
						class="message-board-page__filter-input"
						@keydown.enter.native="handleClick"
						clearable
						placeholder="请输入联系电话"
						v-model="phoneT">
					</el-input>
				</el-form-item>
				<el-form-item label="内容">
					<el-input
						class="message-board-page__filter-input"
						@keydown.enter.native="handleClick"
						clearable
						placeholder="请输入内容"
						v-model="contentT">
					</el-input>
				</el-form-item>
				<el-form-item label="开始时间">
					<el-date-picker
						class="message-board-page__filter-date"
						v-model="startTime"
						align="right"
						type="datetime"
						format="yyyy-MM-dd"
						value-format="yyyy-MM-dd"
						placeholder="选择开始时间">
					</el-date-picker>
				</el-form-item>
				<el-form-item label="截止时间">
					<el-date-picker
						class="message-board-page__filter-date"
						v-model="endTime"
						align="right"
						type="datetime"
						format="yyyy-MM-dd"
						value-format="yyyy-MM-dd"
						placeholder="选择截止时间">
					</el-date-picker>
				</el-form-item>
				<el-form-item class="message-board-page__filter-actions">
					<el-button type="primary" icon="el-icon-search" @click="handleClick()">查询</el-button>
					<el-button type="primary" icon="el-icon-refresh-left" @click="chongzhi()">重置</el-button>
				</el-form-item>
			</el-form>
		</div>

		<el-tabs v-model="activeName" @tab-click="handleClickT" class="message-board-page__tabs">
			<el-tab-pane label="留言板列表" name="first">
				<div class="nf-table-card">
					<div class="message-board-page__toolbar">
						<el-button
							type="primary"
							icon="el-icon-plus"
							:disabled="!isAuth('messageBoard:add')"
							@click="classAdd(0)">添加
						</el-button>
					</div>
				<el-table v-loading="tableDataLoading" :data="tableData.list">
					<el-table-column prop="leaveMessageId" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="nickName" label="用户昵称" width="150">
						<template slot-scope="scope">
							<span class="message-board-page__link"
								@click="updatesvideo(scope.row.userId)">{{scope.row.userName?scope.row.userName:'未绑定'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="avatar" label="头像" width="80">
						<template slot-scope="scope">
							　　<img v-if="scope.row.avatar &&scope.row.avatar!=''" :src="scope.row.avatar" width="40"
								height="40" />
							<span v-else>暂无图片</span>
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="手机号" width="150">
					</el-table-column>
					<el-table-column prop="content" label="内容">
						<template slot-scope="scope">
							<el-popover placement="top-start" title="" trigger="hover">
								<div style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;"
									slot="reference">{{scope.row.content}}</div>
								<div style="width: 300px;">{{scope.row.content}}</div>
							</el-popover>
						</template>
					</el-table-column>

					<!-- <el-table-column prop="clubProvince" label="省">
					</el-table-column> -->
					<!-- 	<el-table-column prop="city" label="市">
					</el-table-column> -->
					<!-- <el-table-column prop="clubDistrict" label="区">
					</el-table-column>
					<el-table-column prop="clubAddress" label="详细地址">
					</el-table-column> -->
					<el-table-column prop="isDelete" label="状态" width="100">
						<template slot-scope="scope">
							<span style="color:#ff4949" v-if="scope.row.isDelete==1">用户端已删除</span>
							<span style="color:#13ce66" v-else>正常</span>
							<!-- <el-switch v-model="scope.row.status" @change="change(scope.row.id,scope.row.status)"
								:disabled="!isAuth('messageBoard:update')" :active-value="openValue2"
								:inactive-value="closeValue2" active-color="#13ce66" inactive-color="#ff4949">
							</el-switch> -->
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="170" align="center">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('messageBoard:update')"
								style="margin: 3px;" @click="classAdd(scope.row)">修改
							</el-button>
							<el-button size="mini" type="warning" plain :disabled="!isAuth('messageBoard:delete')"
								style="margin: 3px;" @click="classdelete(scope.row)">删除用户端显示
							</el-button>
							<el-button size="mini" type="danger" :disabled="!isAuth('messageBoard:delete')"
								style="margin: 3px;" @click="deleteBtn(scope.row)">删除数据
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div class="message-board-page__pagination">
					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>
		</el-tabs>

			<!-- 添加、修改活动类型 -->
			<el-dialog :title="titles" :visible.sync="dialogFormVisible9" center width="80%">
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">绑定用户：</span>
					<el-input v-model="nickName1" @focus="userselect" style="width:50%;" placeholder="请选择绑定用户">
					</el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">内容：</span>
					<el-input style="width:50%;" v-model="content" type="textarea" :rows="2"
						placeholder="请输入内容"></el-input>
				</div>
				<!-- <div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">省市区：</span>
					<el-cascader style="width:50%" size="large" :options="options" ref="cascaderAddr" v-model="storeAddress"
						:placeholder="storeAddre" @change="handleChange55" :value="storeAddre">
					</el-cascader>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">详细地址：</span>
					<el-input v-model="clubAddress"type="text" style="width:50%;"
						placeholder="请输入详细地址">
					</el-input>
				</div>
				<div style="margin-bottom: 10px;display:flex;">
					<span style="width: 200px;display: inline-block;text-align: right;">俱乐部图片：</span>
					<div
						style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
						<el-upload class="avatar-uploader" v-model="clubLogo"
							:action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
							:on-success="handleAvatarSuccess1" :on-progress="onprogress2">
							<img v-if="clubLogo!=''&&percentage2==100" :src="clubLogo" class="avatar"
								style="border-radius: 6px;width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon iconss"></i>
							<el-progress v-if="percentage2>0&&percentage2<100" type="circle" :percentage="percentage2">
							</el-progress>
						</el-upload>
					</div>
				</div> -->
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible9 = false">取 消</el-button>
					<el-button type="primary" @click="refuseto1()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 用户列表 -->
			<el-dialog title="用户列表" :visible.sync="dialogFormVisible3" center width="70%">
				<div style="margin:5px;display: inline-block;">

					<div style="position: relative;display: inline-block;margin:5px;">
						<span>用户类型：</span>
						<el-select v-model="sysUser" style="width:150px;margin-left: 10px;" @change="userclick()">
							<el-option v-for="item in sysUsers" :key="item.value" :label="item.title" :value="item.id">
							</el-option>
						</el-select>&nbsp;&nbsp;&nbsp;
					</div>
					<div style="position: relative;display: inline-block;margin:5px;">
						<span>手机号：</span>
						<el-input style="width: 150px;" @keydown.enter.native="userclick" clearable placeholder="请输入手机号"
							v-model="phone1"></el-input>&nbsp;&nbsp;&nbsp;&nbsp;
					</div>
					<div style="position: relative;display: inline-block;margin:5px;">
						<span>昵称：</span>
						<el-input style="width: 150px;" @keydown.enter.native="userclick" clearable placeholder="请输入昵称"
							v-model="nickName2"></el-input>
					</div>
					<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="userclick">
						查询
					</el-button>
					<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="userclose">
						重置
					</el-button>
				</div>
				<el-table width="780px" v-loading="tableDataLoading4" :data="userIds.list">
					<el-table-column prop="userId" width="80" label="编号">
					</el-table-column>
					<el-table-column prop="nickName" label="昵称">
						<template slot-scope="scope">
							<span style="color: #f56c6c;">{{scope.row.userName ? scope.row.userName : '未设置'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="手机号">
						<template slot-scope="scope">
							<span>{{scope.row.phone ? scope.row.phone : '未绑定'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间">
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
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="size1" :current-page="page1"
						layout="total,sizes, prev, pager, next,jumper" :total="userIds.totalCount">
					</el-pagination>
				</div>
			</el-dialog>
	</div>
</template>

<script>
	// import {
	// 	quillEditor
	// } from 'vue-quill-editor'
	// import 'quill/dist/quill.core.css'
	// import 'quill/dist/quill.snow.css'
	// import 'quill/dist/quill.bubble.css'
	// import quillConfig from './quill-config.js'
	import axios from 'axios';
	import {
		jsonp
	} from 'vue-jsonp'
	import {
		provinceAndCityData,
		regionData,
		provinceAndCityDataPlus,
		regionDataPlus,
		CodeToText,
		TextToCode
	} from 'element-china-area-data'
	var cityOptions = []
	var geocoder, map, markersArray = [];
	export default {
		components: {
			// quillEditor
		},
		data() {
			return {
				size: 5,
				page: 1,
				limit: 10,
				openValue2: 0,
				closeValue2: 1,
				activeName: 'first',
				tableDataLoading: true,
				dialogFormVisible9: false,
				id: '',
				percentage2: 0, //进度条
				gameId: '', //游戏id
				tableData: {},
				titles: '添加活动类型',
				content: '',
				leaveMessageId: '',
				clubLogo: '',
				gameDetails: '',
				clubProvince: '',
				clubCity: '',
				clubDistrict: '',
				clubAddress: '',

				isDelete: 2,
				gameType: 1,
				activityType: '',
				activeName: 'first',

				options: regionData,
				storeAddress: [],
				storeAddre: '请选择城市',

				nickName: '',
				nickName1: '',
				nickName2: '',
				userIds: {}, //用户列表
				userIdss: '', //用户id
				phone1: '',
				tableDataLoading4: false,
				dialogFormVisible3: false,
				size1: 10,
				page1: 1,
				userNameT: '',
				phoneT: '',
				contentT: '',
				startTime: '',
				endTime: '',
				statusT: '',
				statuss: [{
						id: '',
						title: '全部'
					},
					{
						id: 0,
						title: '正常'
					},
					{
						id: 1,
						title: '用户端已删除'
					},

				],
				sysUser: '',
				sysUsers: [{
						id: '',
						title: '全部用户'
					},
					{
						id: 1,
						title: '虚拟用户'
					},
				],
			}
		},
		methods: {
			// tabs切换
			handleClickT(tab, event) {
				if (tab._props.label == '留言板列表') {
					this.page = 1
					this.limit = 10
					this.activityType = ''
					this.classSelect()
				}
				// if (tab._props.label == '其他活动') {
				// 	this.page = 1
				// 	this.limit = 10
				// 	this.activityType = 2
				// 	this.classSelect()
				// }


			},
			handleSizeChange2(val) {
				this.size1 = val
				this.userClass()
			},
			handleCurrentChange2(val) {
				this.page1 = val
				this.userClass()
			},
			handleSizeChange(val) {
				this.limit = val
				this.classSelect()
			},
			handleCurrentChange(val) {
				this.page = val
				this.classSelect()
			},
			handleClick() {
				this.page = 1
				this.classSelect()
			},
			chongzhi() {
				this.page = 1
				this.userNameT = ''
				this.phoneT = ''
				this.contentT = ''
				this.startTime = ''
				this.endTime = ''
				this.statusT = ''
				this.classSelect()
			},
			// 封面图片上传
			handleAvatarSuccess1(file) {
				this.clubLogo = file.data;
			},
			onprogress2(event, file, fileList) {
				console.log('event, file, fileList', parseInt(event.percent))
				this.percentage2 = parseInt(event.percent)
			},
			// 获取活动类型数据
			classSelect() {
				this.$http({
					url: this.$http.adornUrl('leaveMessage/selectLeaveMessageList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userName': this.userNameT,
						'phone': this.phoneT,
						'content': this.contentT,
						'startTime': this.startTime,
						'endTime': this.endTime,
						'isDelete': this.statusT,
					})
				}).then(({
					data
				}) => {
					console.log('', data)
					if (data.code == 0) {
						this.tableDataLoading = false
						let returnData = data.data
						this.tableData = returnData
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
			// 添加
			classAdd(row) {
				console.log('row', row)
				if (row != 0) {
					this.titles = '修改'
					this.content = row.content
					this.leaveMessageId = row.leaveMessageId
					this.userIdss = row.userId
					this.nickName1 = row.userName
					this.isDelete = row.isDelete
				} else {
					this.titles = '添加'
					this.content = ''
					this.leaveMessageId = ''
					this.userIdss = ''
					this.nickName1 = ''
					this.isDelete = 2
				}
				this.dialogFormVisible9 = true
			},
			// 获取省市区
			handleChange55(value) {
				value = this.$refs['cascaderAddr'].currentLabels
				this.clubProvince = value[0]
				this.clubCity = value[1]
				this.clubDistrict = value[2]
				if (this.clubCity == '市辖区') {
					this.clubCity = this.clubProvince
				}
				console.log(this.$refs['cascaderAddr'].currentLabels)
			},
			// 启用与否
			change(id, status) {
				this.$http({
					url: this.$http.adornUrl(`game/enableGameName`),
					method: 'get',
					params: this.$http.adornParams({
						'id': id,
						'status': status
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
								this.classSelect()
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {
								this.classSelect()
							}
						})
					}
				})
			},
			// 提交修改、添加活动类型
			refuseto1() {
				if (this.userIdss == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择用户',
						type: 'warning'
					})
					return
				}
				if (this.content == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入内容',
						type: 'warning'
					})
					return
				}

				if (this.titles == '添加') {
					var urls = 'leaveMessage/insertLeaveMessage'
				} else {
					var urls = 'leaveMessage/updateLeaveMessage'
				}
				this.$http({
					url: this.$http.adornUrl(urls),
					method: 'post',
					// params: this.$http.adornParams({
					data: this.$http.adornData({
						'content': this.content,
						'isDelete': this.isDelete,
						'leaveMessageId': this.leaveMessageId,
						'userId': this.userIdss


					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisible9 = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.classSelect()
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
			// 删除活动类型
			classdelete(row) {
				this.$confirm(`此操作为假删除，删除数据后用户端不展示?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('leaveMessage/deleteLeaveMessageById'),
						method: 'post',
						params: this.$http.adornParams({
							'leaveMessageId': row.leaveMessageId
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
									this.classSelect()
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
			deleteBtn(row) {
				this.$confirm(`此操作为删除数据，确定继续操作?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('leaveMessage/removeLeaveMessageById'),
						method: 'post',
						params: this.$http.adornParams({
							'leaveMessageId': row.leaveMessageId
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
									this.classSelect()
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
			// 获取用户列表弹框
			userselect() {

				this.phone1 = ''
				this.nickName2 = ''
				this.sysUser = ''
				this.page = 1
				this.userClass()
				this.dialogFormVisible3 = true
			},
			// 获取用户列表
			userClass() {
				this.tableDataLoading4 = true
				this.$http({
					// url: this.$http.adornUrl('userCertification/queryUserCertification'),
					// method: 'get',
					// params: this.$http.adornParams({
					// 	'page': this.page1,
					// 	'limit': this.size1,
					// 	'name': this.nickName2,
					// 	'phone': this.phone1
					// })

					url: this.$http.adornUrl('user/selectUserList'),
					method: 'get',
					params: this.$http.adornParams({
						// data: this.$http.adornData({
						'page': this.page1,
						'limit': this.size1,
						'member': -1,
						'userName': this.nickName2,
						'inviterCode': '',
						'startTime': '',
						'endTime': '',
						'isAuthentication': -1,
						'phone': this.phone1,
						'sysUser': this.sysUser,
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
				this.tableDataLoading4 = true
				this.userClass()
			},
			// 重置用户列表数据
			userclose() {
				this.phone1 = ''
				this.nickName2 = ''
				this.sysUser = ''
				this.page = 1
				this.tableDataLoading4 = true

				this.userClass()
			},
			// 确定用户
			confirm(row) {
				this.userIdss = row.userId
				this.nickName1 = row.userName
				// if (this.nickName == '' || this.nickName == null) {
				// 	this.nickName = row.phone
				// }
				this.dialogFormVisible3 = false
			},
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
		},
		mounted() {
			this.classSelect()
		}
	}
</script>

<style scoped>
.message-board-page__filter-select {
	width: 150px;
}

.message-board-page__filter-input {
	width: 200px;
}

.message-board-page__filter-date {
	width: 160px;
}

.message-board-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.message-board-page__tabs {
	margin-bottom: 0;
}

.message-board-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.message-board-page__tabs .nf-table-card {
	margin-bottom: 0;
}

.message-board-page__tabs >>> .el-tabs__item.is-active {
	color: #5cc477;
}

.message-board-page__tabs >>> .el-tabs__active-bar {
	background-color: #5cc477;
}

.message-board-page__toolbar {
	display: flex;
	flex-wrap: wrap;
	gap: 12px;
	margin-bottom: 12px;
}

.message-board-page__link {
	color: #5cc477;
	cursor: pointer;
}

.message-board-page__link:hover {
	color: #3fae5c;
}

.message-board-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

.message-board-page .nf-table-card .el-button + .el-button {
	margin-left: 0;
	margin-top: 5px;
}
</style>