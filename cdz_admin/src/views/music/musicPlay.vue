<template>
	<div class="nf-page music-play-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">音乐管理</span>
			<h2 class="nf-page-hero__title">点歌列表</h2>
			<p class="nf-page-hero__desc">管理用户点歌记录，支持按状态、用户昵称、门店名称、手机号与歌曲名称筛选查询，可添加、修改与删除点歌信息。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<el-tabs v-model="activeName" @tab-click="handleClickT" class="music-play-page__tabs">
			<el-tab-pane label="点歌列表" name="second">
				<div class="nf-filter-card">
					<div class="nf-filter-card__head">
						<i class="el-icon-search nf-filter-card__head-icon"></i>
						<span>筛选条件</span>
					</div>
					<el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
						<el-form-item label="状态">
							<el-select
								class="music-play-page__filter-select"
								v-model="musicTypeIdT"
								placeholder="请选择状态"
								@change="handleClick()">
								<el-option v-for="(item,index) in tableDataType" :key="index" :label="item.musicTypeName"
									:value="item.musicTypeId">
								</el-option>
							</el-select>
						</el-form-item>
						<el-form-item label="用户昵称">
							<el-input
								class="music-play-page__filter-input"
								@keydown.enter.native="handleClick"
								clearable
								placeholder="请输入用户昵称"
								v-model="musicAuthorT">
							</el-input>
						</el-form-item>
						<el-form-item label="门店名称">
							<el-input
								class="music-play-page__filter-input"
								@keydown.enter.native="handleClick"
								clearable
								placeholder="请输入门店名称"
								v-model="tearoomNameT">
							</el-input>
						</el-form-item>
						<el-form-item label="手机号">
							<el-input
								class="music-play-page__filter-input"
								@keydown.enter.native="handleClick"
								clearable
								placeholder="请输入手机号"
								v-model="phoneT">
							</el-input>
						</el-form-item>
						<el-form-item label="歌曲名称">
							<el-input
								class="music-play-page__filter-input"
								@keydown.enter.native="handleClick"
								clearable
								placeholder="请输入歌曲名称"
								v-model="musicNameT">
							</el-input>
						</el-form-item>
						<el-form-item class="music-play-page__filter-actions">
							<el-button type="primary" icon="el-icon-search" @click="handleClick()">查询</el-button>
							<el-button type="primary" icon="el-icon-refresh-left" @click="clearClick()">重置</el-button>
							<el-button type="primary" icon="el-icon-plus"
								:disabled="!isAuth('musicPlay:add')" @click="classAdd(0)">添加</el-button>
						</el-form-item>
					</el-form>
				</div>

				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="tableData.list">
					<el-table-column prop="musicPlayId" label="编号" width="80" fixed="left">
					</el-table-column>
					<el-table-column prop="tearoomName" label="门店名称" fixed="left">
					</el-table-column>
					<el-table-column prop="userName" label="用户昵称" fixed="left">
						<template slot-scope="scope">
							<span class="music-play-page__user-link"
								@click="updates(scope.row.userId)">{{scope.row.userName ? scope.row.userName : '未绑定'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="手机号">
					</el-table-column>
					<el-table-column prop="musicName" label="歌曲名称"  width="150">
					</el-table-column>
					<el-table-column prop="musicUrl" label="歌曲地址" width="350">
						<template slot-scope="scope">
							<audio :id="'myAudio'+scope.row.musicLibraryId"
								v-if="scope.row.musicUrl!=''&&scope.row.musicUrl!=null&&scope.row.musicUrl!='null'"
								duration :src="scope.row.musicUrl" type="audio/wav" controls preload="metadata"></audio>
							<span v-else>暂无语音</span>

						</template>
					</el-table-column>
					<el-table-column prop="musicAuthor" label="歌曲作者" width="150">
					</el-table-column>
					<el-table-column prop="musicPicUrl" label="歌曲封面图" width="80">
						<template slot-scope="scope">
							　　<img v-if="scope.row.musicPicUrl &&scope.row.musicPicUrl!=''" :src="scope.row.musicPicUrl"
								width="40" height="40" />
							<span v-else>暂无图片</span>
						</template>
					</el-table-column>
					<el-table-column prop="status" label="是否已播放" width="80" fixed="right">
						<template slot-scope="scope">
							<span v-if="scope.row.status==1">是</span>
							<span v-if="scope.row.status==2">否</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="170" align="center" fixed="right">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('musicPlay:update')"
								@click="classAdd(scope.row)">修改
							</el-button>
							<el-button size="mini" type="danger" :disabled="!isAuth('musicPlay:delete')"
								@click="classdelete(scope.row)">删除
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div class="music-play-page__pagination">
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
					<span style="width: 200px;display: inline-block;text-align: right;">用户昵称：</span>
					<el-input v-model="nickName1" @focus="userselect(1)" style="width:50%;"
						class="margin15" placeholder="请选择绑定用户">
					</el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">歌曲名称：</span>
					<el-input style="width:50%;" v-model="musicName" type="text" @focus="musicselect()" placeholder="请输入歌曲名称"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">门店名称：</span>
					<el-select v-model="tearoomId" placeholder="请选择台球馆" style="width:50%;" disabled>
						<el-option v-for="(item,index) in shopData" :key="index" :label="item.tearoomName"
							:value="item.tearoomId">
						</el-option>
					</el-select>
				</div>
				<div style="margin-bottom: 10px;display:flex;">
					<span style="width: 200px;display: inline-block;text-align: right;">歌曲地址：</span>
					<div style="width: 50%;">
						<el-input v-model="musicUrl" style="width:300px;" disabled>
						</el-input>
					</div>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">歌曲作者：</span>
					<el-input style="width:50%;" v-model="musicAuthor" disabled type="text" placeholder="请输入歌曲作者"></el-input>
				</div>
				<div style="margin-bottom: 10px;display:flex;">
					<span style="width: 200px;display: inline-block;text-align: right;">歌曲封面图：</span>
					<div style=" width:148px;height:148px;border-radius: 6px;text-align: center;">
						<img v-if="musicPicUrl" :src="musicPicUrl" class="avatar" style="border-radius: 6px;width: 148px;height: 148px;" />
					</div>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible9 = false">取 消</el-button>
					<el-button type="primary" @click="refuseto1()">确 定</el-button>
				</div>
		</el-dialog>
		<!-- 用户列表 -->
		<el-dialog title="用户列表" :visible.sync="dialogFormVisible3" center width="80%">
				<div style="margin:2% 0;display: inline-block;">
					<span>手机号：</span>
					<el-input style="width: 150px;" @keydown.enter.native="userclick" clearable placeholder="请输入手机号"
						v-model="phone1"></el-input>&nbsp;&nbsp;&nbsp;&nbsp;
					<span>昵称：</span>
					<el-input style="width: 150px;" @keydown.enter.native="userclick" clearable placeholder="请输入昵称"
						v-model="nickName2"></el-input>
					<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="userclick">
						查询
					</el-button>
					<el-button style='margin-left:15px;' size="mini" type="primary" plain icon="document"
						@click="userclose">
						重置
					</el-button>
				</div>
				<el-table width="780px" v-loading="tableDataLoading4" :data="userIds.list">
					<el-table-column prop="userId" fixed label="编号" width="80">
					</el-table-column>
					<el-table-column prop="userName" label="昵称">
						<template slot-scope="scope">
							<span style="color: #f56c6c;">{{scope.row.userName ? scope.row.userName : '未设置'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="手机号">
						<template slot-scope="scope">
							<span>{{scope.row.phone ? scope.row.phone : '未绑定'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="age" label="年龄">
					</el-table-column>
					<el-table-column prop="sex" label="性别">
						<template slot-scope="scope">
							<span>{{scope.row.sex==1 ? '男' : '女'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间">
					</el-table-column>
					<el-table-column label="操作" fixed='right'>
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="confirm(scope.row)">确定
							</el-button>
							
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange2" @current-change="handleCurrentChange2"
						:page-sizes="[5, 10, 15, 20]" :page-size="size1" :current-page="page1"
						layout="total,sizes, prev, pager, next,jumper" :total="userIds.totalCount">
					</el-pagination>
				</div>
		</el-dialog>
		<!-- 音乐库列表 -->
		<el-dialog title="音乐库列表" :visible.sync="dialogFormVisible2" center width="80%">
				<div style="width: 100%;margin-right: 30px;">
					<div style="margin:5px;display: inline-block;">
						<span>分类名称：</span>
						<el-select v-model="musicTypeIdYyk" style="width:150px;margin-left: 10px;" @change="handleClickYyk()">
							<el-option  v-for="(item,index) in tableDataType" :key="index" :label="item.musicTypeName" :value="item.musicTypeId">
							</el-option>
						</el-select>&nbsp;&nbsp;&nbsp;&nbsp;
					</div>
					<div style="margin:5px;display: inline-block;">
						<span>歌曲名称：</span>
						<el-input style="width: 160px;" @keydown.enter.native="handleClickYyk" clearable placeholder="请输入歌曲名称"
							v-model="musicNameYyk"></el-input>&nbsp;&nbsp;&nbsp;&nbsp;
					</div>
					<div style="margin:5px;display: inline-block;">
						<span>歌曲作者：</span>
						<el-input style="width: 160px;" @keydown.enter.native="handleClickYyk" clearable placeholder="请输入歌曲作者"
							v-model="musicAuthorYyk"></el-input>&nbsp;&nbsp;&nbsp;&nbsp;
					</div>
					
					<el-button style="margin:10px;" size="mini" type="primary" plain icon="document"
						@click="handleClickYyk()">查询
					</el-button>
					<el-button style="margin:10px;" size="mini" type="primary" plain icon="document"
						@click="clearClickYyk()">重置
					</el-button>
				</div>
				<el-table v-loading="tableDataLoading1" :data="tableDataYyk.records">
					<el-table-column prop="musicLibraryId" label="编号" width="80" fixed='left'>
					</el-table-column>
					<el-table-column prop="tearoomName" label="门店名称" >
					</el-table-column>
					<el-table-column prop="musicTypeName" label="分类名称">
					</el-table-column>
					<el-table-column prop="musicName" label="歌曲名称" fixed='left'>
					</el-table-column>
					<el-table-column prop="musicUrl" label="歌曲地址" width="350">
						<template slot-scope="scope">
							<audio :id="'myAudio'+scope.row.musicLibraryId"
								v-if="scope.row.musicUrl!=''&&scope.row.musicUrl!=null&&scope.row.musicUrl!='null'"
								duration :src="scope.row.musicUrl" type="audio/wav" controls
								preload="metadata"></audio>
							<span v-else>暂无语音</span>
						
						</template>
					</el-table-column>
					<el-table-column prop="musicAuthor" label="歌曲作者" width="150">
					</el-table-column>
					<el-table-column prop="musicPicUrl" label="歌曲封面图" width="80">
						<template slot-scope="scope">
							　　<img v-if="scope.row.musicPicUrl &&scope.row.musicPicUrl!=''" :src="scope.row.musicPicUrl" width="40"
								height="40" />
							<span v-else>暂无图片</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="170" align="center" fixed='right'>
						<template slot-scope="scope">
							<el-button size="mini" type="primary"
								@click="YykAdd(scope.row)">选择
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChangeYyk" @current-change="handleCurrentChangeYyk"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="tableDataYyk.total">
					</el-pagination>
				</div>
		</el-dialog>
	</div>
</template>

<script>
	import {
		quillEditor
	} from 'vue-quill-editor'
	import 'quill/dist/quill.core.css'
	import 'quill/dist/quill.snow.css'
	import 'quill/dist/quill.bubble.css'
	import quillConfig from '../selfShop/quill-config.js'
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
			quillEditor
		},
		data() {
			return {
				size: 5,
				page: 1,
				limit: 10,
				openValue2: 0,
				closeValue2: 1,
				activeName: 'second',
				tableDataLoading: true,
				dialogFormVisible9: false,
				id: '',
				percentage2: 0, //进度条
				titleImg: '',
				percentage3: 0, //进度条
				contentImg: [],
				gameId: '', //游戏id
				tableData: {},
				titles: '添加活动类型',
				gameName: '',
				gameIds: '',
				gameImg: '',
				gameDetails: '',
				gameStatus: '',
				gameType: 1,
				activityType: '',
				dialogVisible: false,
				dialogImageUrl: [],

				musicLibraryId: 1,
				tearoomId: '',
				musicTypeId: 1, //歌曲分类
				musicName: "", //歌曲名称
				musicUrl: "", //歌曲地址
				musicPicUrl: "", //歌曲封面图
				musicAuthor: "", //歌曲作者
				tearoomId: '',
				musicPlayId:'',
				status:2,
				tableDataType: [],
				tableDataLoading4: false,
				shopData: [],
				musicTypeIdT: '',
				tearoomNameT: '',
				musicNameT: '',
				musicAuthorT: '',
				phoneT: '',
				
				userId: '',
				nickName: '',
				phone1: '',
				nickName1: '', //门店用户
				nickName2: '',
				nickName3: '', //团长用户
				userIds: {}, //用户列表
				size1: 10,
				page1: 1,
				dialogFormVisible3: false,
				dialogFormVisible2:false,
				tableDataLoading1:false,
				tableDataYyk:{},
				musicTypeIdYyk: '',
				tearoomNameYyk: '',
				musicNameYyk: '',
				musicAuthorYyk: '',
				
			}
		},
		methods: {
			// tabs切换
			handleClickT(tab, event) {
				if (tab._props.label == '点歌列表') {
					this.page = 1
					this.limit = 10
					this.classSelect()
				}


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
			clearClick() {
				this.musicTypeIdT = ''
				this.tearoomNameT = ''
				this.musicNameT = ''
				this.phoneT = ''
				this.musicAuthorT = ''
				this.page = 1
				this.classSelect()
				this.fenleiSelect()
				this.shopSelect()
			},
			// 封面图片上传
			handleAvatarSuccess1(file) {
				this.musicPicUrl = file.data;
			},
			onprogress2(event, file, fileList) {
				console.log('event, file, fileList', parseInt(event.percent))
				this.percentage2 = parseInt(event.percent)
			},
			handleAvatarSuccess3(file) {
				this.titleImg = file.data;
			},
			onprogress3(event, file, fileList) {
				console.log('event, file, fileList', parseInt(event.percent))
				this.percentage3 = parseInt(event.percent)
			},
			// 详情图片上传
			handleRemove2(file, ) {
				this.contentImg.push(file.data);
			},
			// 删除详情图
			dels2(index) {
				console.log(this.activityDetailsImg)
				this.contentImg.splice(index, 1);

			},

			// 获取活动类型数据
			classSelect() {
				this.$http({
					url: this.$http.adornUrl('musicPlay/selectMusicPlayList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'status': this.musicTypeIdT,
						'musicName': this.musicNameT,
						'userName': this.musicAuthorT,
						'tearoomName': this.tearoomNameT,
						'phone': this.phoneT,
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
			// 添加歌曲
			classAdd(row) {
				console.log('row', row)
				if (row != 0) {
					this.titles = '修改歌曲'
					this.musicPlayId = row.musicPlayId
					this.status = row.status
					this.musicLibraryId = row.musicLibraryId
					this.musicTypeId = row.musicTypeId
					this.musicName = row.musicName
					this.musicUrl = row.musicUrl
					this.musicAuthor = row.musicAuthor
					this.tearoomId = row.tearoomId
					this.nickName1 = row.userName
					if (row.musicPicUrl) {
						this.musicPicUrl = row.musicPicUrl
					} else {
						this.musicPicUrl = ''
					}
					this.userId = row.userId
					
					this.percentage2 = 100
				} else {
					this.titles = '添加歌曲'
					this.musicLibraryId = ''
					this.musicTypeId = ''
					this.musicName = ''
					this.musicUrl = ''
					this.musicPicUrl = ''
					this.musicAuthor = ''
					this.tearoomId = ''
					this.userId = ''
					this.nickName1 = ''
					this.musicPlayId = ''
					this.status = 2

				}
				this.dialogFormVisible9 = true
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
			// 提交修改、添加歌曲
			refuseto1() {
				if (this.userId == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择用户',
						type: 'warning'
					})
					return
				}
				if (this.musicName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入歌曲名称',
						type: 'warning'
					})
					return
				}
				if (this.musicPicUrl == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传歌曲封面图',
						type: 'warning'
					})
					return
				}

				if (this.titles == '添加歌曲') {
					var urls = 'musicPlay/insertMusicPlay'
				} else {
					var urls = 'musicPlay/updateMusicPlay'
				}
				this.$http({
					url: this.$http.adornUrl(urls),
					method: 'post',
					data: this.$http.adornData({
						'musicLibraryId': this.musicLibraryId,
						'musicTypeId': this.musicTypeId,
						'musicName': this.musicName,
						'musicUrl': this.musicUrl,
						'musicPicUrl': this.musicPicUrl,
						'musicAuthor': this.musicAuthor,
						'tearoomId': this.tearoomId,
						'musicPlayId':this.musicPlayId,
						'userId':this.userId,
						'status':this.status,

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
								this.gameName = ''
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
				let delid = row.musicPlayId
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`musicPlay/deleteMusicPlay?musicPlayId=${delid}`),
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
			fenleiSelect() {
				this.$http({
					url: this.$http.adornUrl('musicType/selectMusicTypeList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': 1,
						'limit': 50,
					})
				}).then(({
					data
				}) => {
					console.log('', data)
					if (data.code == 0) {
						let returnData = data.data
						this.tableDataType = returnData.records
					}
				})
			},
			handleSuccess(file) {
				console.log(file)
				this.musicUrl = file.data;
				console.log(file.data, this.voiceIntroduce)
				this.tableDataLoading4 = false
			},
			handlePreview(file) {
				this.tableDataLoading4 = true
			},
			// 验证只能上传音频
			beforeAvatarUpload(file) {
				var testmsg = file.name.substring(file.name.lastIndexOf('.') + 1)
				const extension = testmsg === 'mp3'
				if (!extension) {
					this.$message({
						message: "上传文件只能是mp3格式！",
						type: 'error'
					})
				}
				return extension;
			},
			// 获取门店数据
			shopSelect() {

				this.$http({
					url: this.$http.adornUrl('tearoom/selectTearoomList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': 1,
						'limit': 1000,
						'tearoomName': '',
						// 'search_goods_cate': this.typeId,
						// 'status': status,
						'city': '',
						'status': '',
					})
				}).then(({
					data
				}) => {
					let returnData = data.data.list;
					this.shopData = returnData
				})
			},
			// 获取用户列表弹框
			userselect(index, row) {
				this.indexType = index
				this.dialogFormVisible3 = true
				this.page1 = 1
				this.phone1 = ''
				this.nickName2 = ''
				this.userClass()
			},
			// 获取用户列表
			userClass() {
				this.tableDataLoading4 = true
				this.$http({
					url: this.$http.adornUrl('user/selectUserList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page1,
						'limit': this.size1,
						'userName': this.nickName2,
						'inviterCode': '',
						'startTime': '',
						'endTime': '',
						'isAuthentication': '',
						'phone': this.phone1
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
				this.page1 = 1
				this.userClass()
			},
			// 重置用户列表数据
			userclose() {
				this.phone1 = ''
				this.nickName2 = ''
				this.userClass()
			},
			// 确定用户
			confirm(row) {
				this.userId = row.userId
				this.nickName1 = row.userName
				this.dialogFormVisible3 = false
			},
			handleSizeChange2(val) {
				this.size1 = val
				this.userClass()
			},
			handleCurrentChange2(val) {
				this.page1 = val
				this.userClass()
			},
			// ----音乐库
			// 获取用户列表弹框
			musicselect() {
				this.dialogFormVisible2 = true
				this.page1 = 1
				this.musicTypeIdYyk = ''
				this.tearoomNameYyk = ''
				this.musicNameYyk = ''
				this.musicAuthorYyk = ''
				this.classSelectYyk()
			},
			// 获取活动类型数据
			classSelectYyk() {
				this.tableDataLoading1 = true
				this.$http({
					url: this.$http.adornUrl('musicLibrary/selectMusicLibraryList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page1,
						'limit': this.size1,
						'musicTypeId': this.musicTypeIdYyk,
						'musicName':this.musicNameYyk,
						'musicAuthor':this.musicAuthorYyk,
						'tearoomName':this.tearoomNameYyk,
					})
				}).then(({
					data
				}) => {
					console.log('', data)
					if (data.code == 0) {
						this.tableDataLoading1 = false
						let returnData = data.data
						this.tableDataYyk = returnData
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
			handleSizeChangeYyk(val) {
				this.size1 = val
				this.classSelectYyk()
			},
			handleCurrentChangeYyk(val) {
				this.page1 = val
				this.classSelectYyk()
			},
			handleClickYyk() {
				this.page1 = 1
				this.classSelectYyk()
				
			},
			clearClickYyk(){
				this.musicTypeIdYyk = ''
				this.tearoomNameYyk = ''
				this.musicNameYyk = ''
				this.musicAuthorYyk = ''
				this.page1 = 1
				this.classSelectYyk()
			},
			YykAdd(row){
				this.musicLibraryId = row.musicLibraryId
				this.musicTypeId = row.musicTypeId
				this.musicName = row.musicName
				this.musicUrl = row.musicUrl
				this.musicAuthor = row.musicAuthor
				this.tearoomId = row.tearoomId
				
				if (row.musicPicUrl) {
					this.musicPicUrl = row.musicPicUrl
				} else {
					this.musicPicUrl = ''
				}
				this.dialogFormVisible2 = false
			},
			// 详情跳转
			updates(userId) {
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
			this.fenleiSelect()
			this.shopSelect()
		}
	}
</script>

<style scoped>
.music-play-page__filter-select {
	width: 150px;
}

.music-play-page__filter-input {
	width: 160px;
}

.music-play-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.music-play-page__tabs {
	margin-bottom: 0;
}

.music-play-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.music-play-page__tabs >>> .el-tabs__item.is-active {
	color: #5cc477;
}

.music-play-page__tabs >>> .el-tabs__active-bar {
	background-color: #5cc477;
}

.music-play-page__tabs .nf-filter-card,
.music-play-page__tabs .nf-table-card {
	margin-top: 12px;
	margin-bottom: 0;
}

.music-play-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

.music-play-page__user-link {
	color: #5cc477;
	cursor: pointer;
}
</style>

<style>
	.customWidth {
		width: 80% !important;
	}

	.adver_main.box {
		display: block;
		max-width: 100%;
		text-align: center;
		border: 1px dotted rgba(67, 79, 103, .4);
	}

	.cards {
		padding: 0 8px;
		margin-bottom: 15px;
	}

	.adver_main.box a {
		display: flex;
		justify-content: center;
		height: 150px;
		line-height: 150px;
		text-decoration: none
	}

	.bannerManin {
		border: 1px solid #e8e8e8;
		font-size: 14px;
		padding: 0 24px;
		display: flex;
		justify-content: center;
		align-items: center;
		height: 113px;
		color: rgba(0, 0, 0, .65);
	}

	.bannerManin span {
		display: inline-block;
		margin-left: 5px;
	}

	.bannerManin img {
		width: 48px;
		height: 48px;
		border-radius: 50%;
	}

	.bannerbtn {
		display: flex;
		border-top: none !important;
		border: 1px solid #e8e8e8;
		padding: 11px;
		font-size: 14px;
		color: #5cc477;
	}

	.bannerbtn a {
		flex: 1;
		text-align: center;
		color: #5cc477 !important;
		text-decoration: none;
	}

	.imgs {
		position: relative;
		border-radius: 6px;
		width: 148px;
		height: 148px;
		margin-right: 10px;
		display: inline-block;
	}

	.dels {
		position: absolute;
		top: 0;
		left: 0;
		display: none;
	}

	.dels .el-icon-delete {
		line-height: 148px;
		padding-left: 58px;
		font-size: 25px;
		color: #fff;
	}

	.imgs:hover .dels {
		width: 100%;
		height: 100%;
		background: #000;
		display: block;
		opacity: 0.5;
	}

	.bqList {
		padding: 4px 14px;
		margin: 4px;
		border: 1px solid #efefef;
		font-size: 12px;
		color: #999;
		border-radius: 4px;
		margin-right: 15px;
	}

	.delss {
		display: none;
		position: relative;
	}

	.delss .el-icon-delete {
		position: absolute;
		top: 0;
	}

	.bqList:hover .delss {
		display: initial;
		opacity: 0.5;
	}

	.tj {
		padding: 6px !important;
		margin: 4px;
		font-size: 12px;
		border: 1px solid #ccc;
		border-radius: 4px;
	}

	.biaoqianCla {
		position: relative;
		width: 90px;
		height: 75px;
		display: inline-block;
		padding: 10px;
		box-shadow: 0px 0px 7px #999;
		margin: 5px;
	}
</style>