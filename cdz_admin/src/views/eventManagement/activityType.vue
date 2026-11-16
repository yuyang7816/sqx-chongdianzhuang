<template>
	<div class="nf-page activity-type-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">活动管理</span>
			<h2 class="nf-page-hero__title">活动类型</h2>
			<p class="nf-page-hero__desc">管理活动类型配置，支持查看、添加、修改与启用状态切换。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">
				<i class="el-icon-s-operation nf-filter-card__head-icon"></i>
				<span>快捷操作</span>
			</div>
			<div class="activity-type-page__filter-actions">
				<el-button type="primary" icon="el-icon-refresh" @click="handleClick()">刷新</el-button>
				<el-button type="primary" icon="el-icon-plus" :disabled="!isAuth('locality:add')" @click="classAdd(0)">添加</el-button>
			</div>
		</div>

		<el-tabs v-model="activeName" class="activity-type-page__tabs" @tab-click="handleClickT">
			<!-- <el-tab-pane label="猫鼠游戏" name="first">
				<div style="width: 100%;text-align: right;margin-right: 30px;">
					<el-button style="margin:10px;" size="mini" type="primary" plain icon="document"
						@click="handleClick()">刷新
					</el-button>
					<el-button style="margin:10px;" size="mini" type="primary" icon="document"
						:disabled="!isAuth('locality:add')" @click="classAdd(0)">添加
					</el-button>
				</div>
				<el-table v-loading="tableDataLoading" :data="tableData.records">
					<el-table-column prop="id" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="gameName" label="类型名称" width="150">
					</el-table-column>
					<el-table-column prop="gameImg" label="类型图片" width="80">
						<template slot-scope="scope">
							　　<img v-if="scope.row.gameImg &&scope.row.gameImg!=''" :src="scope.row.gameImg" width="40"
								height="40" />
							<span v-else>暂无图片</span>
						</template>
					</el-table-column>
					<el-table-column prop="titleImg " label="主页图" width="80">
						<template slot-scope="scope">
							　　<img v-if="scope.row.titleImg  &&scope.row.titleImg !=''" :src="scope.row.titleImg "
								width="40" height="40" />
							<span v-else>暂无图片</span>
						</template>
					</el-table-column>
					<el-table-column prop="contentImg" label="内容图" width="300">
						<template slot-scope="scope">
							<div v-if="scope.row.contentImg">
								<div v-for="(item,index) in scope.row.contentImg.split(',')" :key="index"
									style="display: inline-block; margin: 3px;">
									<el-popover placement="top-start" title="" trigger="hover">
										<img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
										<img style="width: 300px; height: auto" :src="item" alt="">
									</el-popover>
								</div>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="gameDetails" label="活动规则">
					</el-table-column>
					<el-table-column prop="status" label="状态" width="100">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.status" @change="change(scope.row.id,scope.row.status)"
								:disabled="!isAuth('locality:update')" :active-value="openValue2"
								:inactive-value="closeValue2" active-color="#13ce66" inactive-color="#ff4949">
							</el-switch>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="170" align="center">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('locality:update')"
								@click="classAdd(scope.row)">修改
							</el-button>
							<el-button size="mini" type="danger" :disabled="!isAuth('locality:delete')"
								@click="classdelete(scope.row)">删除
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="tableData.total">
					</el-pagination>
				</div>
			</el-tab-pane> -->
			<el-tab-pane label="其他活动" name="second">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="tableData.records">
					<el-table-column prop="id" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="gameName" label="类型名称" width="150">
					</el-table-column>
					<el-table-column prop="gameImg" label="类型图片" width="80">
						<template slot-scope="scope">
							　　<img v-if="scope.row.gameImg &&scope.row.gameImg!=''" :src="scope.row.gameImg" width="40"
								height="40" />
							<span v-else>暂无图片</span>
						</template>
					</el-table-column>
					<el-table-column prop="titleImg " label="主页图" width="80">
						<template slot-scope="scope">
							　　<img v-if="scope.row.titleImg  &&scope.row.titleImg !=''" :src="scope.row.titleImg "
								width="40" height="40" />
							<span v-else>暂无图片</span>
						</template>
					</el-table-column>
					<el-table-column prop="contentImg" label="内容图" width="200">
						<template slot-scope="scope">
							<div v-if="scope.row.contentImg">
								<div v-for="(item,index) in scope.row.contentImg.split(',')" :key="index"
									style="display: inline-block; margin: 3px;">
									<el-popover placement="top-start" title="" trigger="hover">
										<img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
										<img style="width: 300px; height: auto" :src="item" alt="">
									</el-popover>
								</div>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="gameDetails" label="活动规则">
					</el-table-column>
					<el-table-column prop="status" label="状态" width="100">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.status" @change="change(scope.row.id,scope.row.status)"
								:disabled="!isAuth('locality:update')" :active-value="openValue2"
								:inactive-value="closeValue2" active-color="#5cc477" inactive-color="#ff4949">
							</el-switch>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="170" align="center">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('locality:update')"
								@click="classAdd(scope.row)">修改
							</el-button>
							<el-button size="mini" type="danger" :disabled="!isAuth('locality:delete')"
								@click="classdelete(scope.row)">删除
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div class="activity-type-page__pagination">
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
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">类型名称：</span>
					<el-input style="width:50%;" v-model="gameName" type="text" placeholder="请输入类型名称"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">活动规则：</span>
					<el-input style="width:50%;" v-model="gameDetails" type="textarea" :rows="3"
						placeholder="请输入类型规则"></el-input>
				</div>
				<div style="margin-bottom: 10px;display:flex;">
					<span style="width: 200px;display: inline-block;text-align: right;">类型图片：</span>
					<div
						style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
						<el-upload class="avatar-uploader" v-model="gameImg" :action="$http.adornUrlUp('alioss/upload')"
							:show-file-list="false" :on-success="handleAvatarSuccess1" :on-progress="onprogress2">
							<!-- <el-progress type="circle" :percentage="100" status="success"></el-progress> -->
							<img v-if="gameImg!=''&&percentage2==100" :src="gameImg" class="avatar"
								style="border-radius: 6px;width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon iconss"></i>
							<el-progress v-if="percentage2>0&&percentage2<100" type="circle" :percentage="percentage2">
							</el-progress>
						</el-upload>
					</div>
				</div>
				<div style="margin-bottom: 10px;display:flex;">
					<span style="width: 200px;display: inline-block;text-align: right;">主页图：</span>
					<div
						style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
						<el-upload class="avatar-uploader" v-model="titleImg "
							:action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
							:on-success="handleAvatarSuccess3" :on-progress="onprogress3">
							<!-- <el-progress type="circle" :percentage="100" status="success"></el-progress> -->
							<img v-if="titleImg !=''&&percentage3==100" :src="titleImg " class="avatar"
								style="border-radius: 6px;width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon iconss"></i>
							<el-progress v-if="percentage3>0&&percentage3<100" type="circle" :percentage="percentage3">
							</el-progress>
						</el-upload>
					</div>
				</div>
				<div style="margin-bottom: 10px;display: flex;align-items: center;">
					<span style="width: 200px;display: inline-block;text-align: right;">展示图：</span>
					<div style="display: flex;flex-wrap: wrap;">
						<div class="imgs" v-for="(item,index) in contentImg" :key="index">
							<img width="100%" class="images" height="100%" :src="item" alt="">
							<span class="dels">
								<i class="el-icon-delete" @click="dels2(index)"></i>
							</span>
						</div>
						<div class="imgs">
							<el-upload :action="$http.adornUrlUp('alioss/upload')" list-type="picture-card"
								:show-file-list="false" :on-success="handleRemove2" v-if="contentImg.length<5">
								<i class="el-icon-plus"></i>
							</el-upload>
							<el-dialog :visible.sync="dialogVisible">
								<img width="100%" :src="dialogImageUrl" alt="">
							</el-dialog>
						</div>
					</div>
				</div>

				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible9 = false">取 消</el-button>
					<el-button type="primary" @click="refuseto1()">确 定</el-button>
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
				activityType: 2,
				dialogVisible: false,
				dialogImageUrl: [],
			}
		},
		methods: {
			// tabs切换
			handleClickT(tab, event) {
				if (tab._props.label == '猫鼠游戏') {
					this.page = 1
					this.limit = 10
					this.activityType = 1
					this.classSelect()
				}
				if (tab._props.label == '其他活动') {
					this.page = 1
					this.limit = 10
					this.activityType = 2
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
			// 封面图片上传
			handleAvatarSuccess1(file) {
				this.gameImg = file.data;
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
					url: this.$http.adornUrl('game/queryGameName'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'activityType': this.activityType,
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
			// 添加活动类型
			classAdd(row) {
				console.log('row', row)
				if (row != 0) {
					this.titles = '修改活动类型'
					this.gameName = row.gameName
					this.gameIds = row.id
					this.gameImg = row.gameImg
					if (row.titleImg) {
						this.titleImg = row.titleImg
					} else {
						this.titleImg = ''
					}
					if (row.contentImg) {
						this.contentImg = row.contentImg.split(',')
					} else {
						this.contentImg = []
					}
					if (row.gameDetails) {
						this.gameDetails = row.gameDetails
					} else {
						this.gameDetails = ''
					}
					this.gameStatus = row.status
					this.gameType = row.gameType
					this.percentage2 = 100
				} else {
					this.titles = '添加活动类型'
					this.gameName = ''
					this.gameIds = ''
					this.gameImg = ''
					this.gameStatus = 0
					this.gameType = ''
					this.gameDetails = ''
					this.titleImg = ''
					this.contentImg = []
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
			// 提交修改、添加活动类型
			refuseto1() {
				if (this.gameDetails == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入活动规则',
						type: 'warning'
					})
					return
				}
				if (this.gameName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入类型名称',
						type: 'warning'
					})
					return
				}
				if (this.gameImg == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传类型图片',
						type: 'warning'
					})
					return
				}
				if (this.titleImg == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传主页图',
						type: 'warning'
					})
					return
				}
				if (this.contentImg.length == 0) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传内容图',
						type: 'warning'
					})
					return
				}

				if (this.titles == '添加活动类型') {
					var urls = 'game/addGameName'
				} else {
					var urls = 'game/updateGameName'
				}
				this.$http({
					url: this.$http.adornUrl(urls),
					method: 'get',
					params: this.$http.adornParams({
						'gameName': this.gameName,
						'status': this.gameStatus,
						'gameImg': this.gameImg,
						'id': this.gameIds,
						'gameType': this.gameType,
						'gameDetails': this.gameDetails,
						'activityType': this.activityType,
						'titleImg': this.titleImg,
						'contentImg': this.contentImg.toString(),
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
				let delid = row.id
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`game/deleteGameName?id=${delid}`),
						method: 'get',
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
		},
		mounted() {
			this.classSelect()
		}
	}
</script>

<style scoped>
.activity-type-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.activity-type-page__tabs {
	margin-bottom: 0;
}

.activity-type-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.activity-type-page__tabs .nf-table-card {
	margin-bottom: 0;
}

.activity-type-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

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