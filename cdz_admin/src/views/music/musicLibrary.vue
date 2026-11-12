<template>
	<div class="nf-page music-library-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">音乐管理</span>
			<h2 class="nf-page-hero__title">音乐列表</h2>
			<p class="nf-page-hero__desc">管理音乐库歌曲资源，支持按分类、歌曲名称与作者筛选查询，可添加、修改、删除及随机批量添加歌曲。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<el-tabs v-model="activeName" @tab-click="handleClickT" class="music-library-page__tabs">
			<el-tab-pane label="音乐列表" name="second">
				<div class="nf-filter-card">
					<div class="nf-filter-card__head">
						<i class="el-icon-search nf-filter-card__head-icon"></i>
						<span>筛选条件</span>
					</div>
					<el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
						<el-form-item label="分类名称">
							<el-select
								class="music-library-page__filter-select"
								v-model="musicTypeIdT"
								placeholder="请选择分类"
								@change="handleClick()">
								<el-option v-for="(item,index) in tableDataType" :key="index" :label="item.musicTypeName" :value="item.musicTypeId">
								</el-option>
							</el-select>
						</el-form-item>
						<el-form-item label="歌曲名称">
							<el-input
								class="music-library-page__filter-input"
								@keydown.enter.native="handleClick"
								clearable
								placeholder="请输入歌曲名称"
								v-model="musicNameT">
							</el-input>
						</el-form-item>
						<el-form-item label="歌曲作者">
							<el-input
								class="music-library-page__filter-input"
								@keydown.enter.native="handleClick"
								clearable
								placeholder="请输入歌曲作者"
								v-model="musicAuthorT">
							</el-input>
						</el-form-item>
						<el-form-item class="music-library-page__filter-actions">
							<el-button type="primary" icon="el-icon-search" @click="handleClick()">查询</el-button>
							<el-button type="primary" icon="el-icon-refresh-left" @click="clearClick()">重置</el-button>
							<el-button type="primary" icon="el-icon-plus"
								:disabled="!isAuth('musicLibrary:add')" @click="classAdd(0)">添加</el-button>
							<el-button type="warning" icon="el-icon-s-promotion"
								:disabled="!isAuth('musicLibrary:add')" @click="classAddSj(0)">随机添加</el-button>
						</el-form-item>
					</el-form>
				</div>

				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="tableData.records">
					<el-table-column prop="musicLibraryId" label="编号" width="80">
					</el-table-column>
					<!-- <el-table-column prop="tearoomName" label="门店名称" >
					</el-table-column> -->
					<el-table-column prop="musicTypeName" label="分类名称">
					</el-table-column>
					<el-table-column prop="musicName" label="歌曲名称">
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
					<el-table-column label="操作" prop="id" width="170" align="center">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('musicLibrary:update')"
								@click="classAdd(scope.row)">修改
							</el-button>
							<el-button size="mini" type="danger" :disabled="!isAuth('musicLibrary:delete')"
								@click="classdelete(scope.row)">删除
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div class="music-library-page__pagination">
					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="tableData.total">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>
		</el-tabs>

		<!-- 添加、修改活动类型 -->
		<el-dialog :title="titles" :visible.sync="dialogFormVisible9" center width="80%">
				<!-- <div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">选择门店：</span>
					<el-select v-model="tearoomId" placeholder="请选择门店" style="width:50%;">
						<el-option v-for="item in shopData" :key="index" :label="item.tearoomName"
							:value="item.tearoomId">
						</el-option>
					</el-select>
				</div> -->
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">分类名称：</span>
					<el-radio-group v-model="musicTypeId" style="display: inline-block;">
					  <el-radio v-for="(item,index) in tableDataType" :key="index" :label="item.musicTypeId">{{item.musicTypeName}}</el-radio>
					</el-radio-group>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">歌曲名称：</span>
					<el-input style="width:50%;" v-model="musicName" type="text" placeholder="请输入歌曲名称"></el-input>
				</div>
				<div style="margin-bottom: 10px;display:flex;">
					<span style="width: 200px;display: inline-block;text-align: right;">歌曲地址：</span>
					<div style="width: 50%;">
						<el-upload class="upload-demo" v-model="musicUrl"
							:action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
							:on-success="handleSuccess" :before-upload="beforeAvatarUpload"
							:on-progress="handlePreview">
							<el-button size="small" type="primary" v-loading="tableDataLoading4">点击上传</el-button>
							<el-input v-if="musicUrl!=''" v-model="musicUrl" style="width:300px;">
							</el-input>
						</el-upload>
					</div>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">歌曲作者：</span>
					<el-input style="width:50%;" v-model="musicAuthor" type="text" placeholder="请输入歌曲作者"></el-input>
				</div>
				<div style="margin-bottom: 10px;display:flex;">
					<span style="width: 200px;display: inline-block;text-align: right;">歌曲封面图：</span>
					<div
						style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
						<el-upload class="avatar-uploader" v-model="musicPicUrl" :action="$http.adornUrlUp('alioss/upload')"
							:show-file-list="false" :on-success="handleAvatarSuccess1" :on-progress="onprogress2">
							<img v-if="musicPicUrl!=''&&percentage2==100" :src="musicPicUrl" class="avatar"
								style="border-radius: 6px;width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon iconss"></i>
							<el-progress v-if="percentage2>0&&percentage2<100" type="circle" :percentage="percentage2">
							</el-progress>
						</el-upload>
					</div>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible9 = false">取 消</el-button>
					<el-button type="primary" @click="refuseto1()">确 定</el-button>
				</div>
		</el-dialog>
		<!-- 随机添加 -->
		<el-dialog title="随机添加" :visible.sync="dialogFormVisible1" center width="80%">
				<!-- <div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">选择门店：</span>
					<el-select v-model="tearoomId" placeholder="请选择门店" style="width:50%;">
						<el-option v-for="item in shopData" :key="index" :label="item.tearoomName"
							:value="item.tearoomId">
						</el-option>
					</el-select>
				</div> -->
				
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">数量：</span>
					<el-input style="width:50%;" v-model="musicAuthor" type="text" placeholder="请输入数量"></el-input>
				</div>
				
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible1 = false">取 消</el-button>
					<el-button type="primary" @click="refusetoSj()">确 定</el-button>
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
				tearoomId:'',
				musicTypeId: 1, //歌曲分类
				musicName: "", //歌曲名称
				musicUrl: "", //歌曲地址
				musicPicUrl: "", //歌曲封面图
				musicAuthor: "", //歌曲作者
				tearoomId:'',
				tableDataType:[],
				tableDataLoading4:false,
				shopData: [],
				musicTypeIdT:'',
				tearoomNameT:'',
				musicNameT:'',
				musicAuthorT:'',
				dialogFormVisible1:false,
				
			}
		},
		methods: {
			// tabs切换
			handleClickT(tab, event) {
				if (tab._props.label == '音乐列表') {
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
			clearClick(){
				this.musicTypeIdT = ''
				this.tearoomNameT = ''
				this.musicNameT = ''
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
					url: this.$http.adornUrl('musicLibrary/selectMusicLibraryList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'musicTypeId': this.musicTypeIdT,
						'musicName':this.musicNameT,
						'musicAuthor':this.musicAuthorT,
						'tearoomName':this.tearoomNameT,
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
				// if (this.tearoomId == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请选择门店',
				// 		type: 'warning'
				// 	})
				// 	return
				// }
				if (this.musicTypeId == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择歌曲分类',
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
				// if (this.musicPicUrl == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请上传歌曲封面图',
				// 		type: 'warning'
				// 	})
				// 	return
				// }
				if (this.musicUrl == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传歌曲地址',
						type: 'warning'
					})
					return
				}
				if (this.musicAuthor == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入歌曲作者',
						type: 'warning'
					})
					return
				}

				if (this.titles == '添加歌曲') {
					var urls = 'musicLibrary/insertMusicLibrary'
				} else {
					var urls = 'musicLibrary/updateMusicLibrary'
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
						'tearoomId':this.tearoomId,
						
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
				let delid = row.musicLibraryId
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`musicLibrary/deleteMusicLibrary?musicLibraryId=${delid}`),
						method: 'post',
						params: this.$http.adornData({})
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
						'page':1,
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
			// 随机添加
			classAddSj(){
				this.musicAuthor = ''
				this.tearoomId = ''
				this.dialogFormVisible1 = true
			},
			// 确认随机添加
			refusetoSj(){
				// if (this.tearoomId === '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请选择门店',
				// 		type: 'warning'
				// 	});
				// 	return
				// }
				if (this.musicAuthor === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入数量',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl('musicLibrary/insertMusicLibraryRand'),
					method: 'post',
					// data: this.$http.adornData({
					params: this.$http.adornParams({
						'num': this.musicAuthor,
						'tearoomId':this.tearoomId,
						
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisible1 = false
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
		},
		mounted() {
			this.classSelect()
			this.fenleiSelect()
			this.shopSelect()
		}
	}
</script>

<style scoped>
.music-library-page__filter-select {
	width: 150px;
}

.music-library-page__filter-input {
	width: 160px;
}

.music-library-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.music-library-page__tabs {
	margin-bottom: 0;
}

.music-library-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.music-library-page__tabs >>> .el-tabs__item.is-active {
	color: #5cc477;
}

.music-library-page__tabs >>> .el-tabs__active-bar {
	background-color: #5cc477;
}

.music-library-page__tabs .nf-filter-card,
.music-library-page__tabs .nf-table-card {
	margin-top: 12px;
	margin-bottom: 0;
}

.music-library-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
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