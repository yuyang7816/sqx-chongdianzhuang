<template>
	<div class="nf-page aikeyword-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">聊天记录</span>
			<h2 class="nf-page-hero__title">AI关键词配置</h2>
			<p class="nf-page-hero__desc">管理 AI 自动回复关键词，支持按状态筛选、添加、修改与启用切换。</p>
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
						class="aikeyword-page__filter-select"
						v-model="statusT"
						placeholder="请选择状态"
						@change="handleClick()">
						<el-option v-for="item in messageStates" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="关键字">
					<el-input
						class="aikeyword-page__filter-input"
						@keydown.enter.native="handleClick"
						clearable
						placeholder="请输入关键字"
						v-model="keyWordsT">
					</el-input>
				</el-form-item>
				<el-form-item class="aikeyword-page__filter-actions">
					<el-button type="primary" icon="el-icon-refresh" @click="handleClick()">刷新</el-button>
					<el-button type="primary" icon="el-icon-refresh-left" @click="clearBtn()">重置</el-button>
					<el-button type="primary" icon="el-icon-plus" :disabled="!isAuth('locality:add')" @click="classAdd(0)">添加</el-button>
				</el-form-item>
			</el-form>
		</div>

		<el-tabs v-model="activeName" class="aikeyword-page__tabs" @tab-click="handleClickT">
			<el-tab-pane label="关键词列表" name="second">
				<div class="nf-table-card">
					<el-table v-loading="tableDataLoading" :data="tableData.list">
						<el-table-column prop="aiKeyWordsId" label="编号" width="80">
						</el-table-column>
						<el-table-column prop="keyWords" label="关键字" width="150">
						</el-table-column>
						<el-table-column prop="content" label="回复内容">
						</el-table-column>
						<el-table-column prop="status" label="状态" width="100">
							<template slot-scope="scope">
								<el-switch v-model="scope.row.status" @change="change(scope.row.aiKeyWordsId,scope.row.status)"
									:disabled="!isAuth('locality:update')" :active-value="1"
									:inactive-value="2" active-color="#5cc477" inactive-color="#ff4949">
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
					<div class="aikeyword-page__pagination">
						<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
							:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
							layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
						</el-pagination>
					</div>
				</div>
			</el-tab-pane>
		</el-tabs>

		<el-dialog :title="titles" :visible.sync="dialogFormVisible9" center width="80%">
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">关键字：</span>
				<el-input style="width:50%;" v-model="keyWords" type="text" placeholder="请输入关键字"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">回复内容：</span>
				<el-input style="width:50%;" v-model="content" type="textarea" :rows="3"
					placeholder="请输入回复内容"></el-input>
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
				activeName: 'second',
				tableData:{},
				tableDataLoading: true,
				dialogFormVisible9: false,
				titles: '添加',
				aiKeyWordsId:'',
				keyWords:'',
				content:'',
				status:1,
				statusT:'',
				keyWordsT:'',
				messageStates:[
					{
							label: '全部',
							value: ''
						},
						{
							label: '开启',
							value: 1
						},
						{
							label: '关闭',
							value: 2
						}
				],
				
			}
		},
		methods: {
			// tabs切换
			handleClickT(tab, event) {
				
				if (tab._props.label == '关键词列表') {
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
			clearBtn(){
				this.statusT = ''
				this.keyWordsT = ''
				this.page = 1
				this.classSelect()
			},
			
			// 获取活动类型数据
			classSelect() {
				this.$http({
					url: this.$http.adornUrl('aiKeyWords/selectAiKeyWordsList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'keyWords': this.keyWordsT,
						'status':this.statusT,
					})
				}).then(({
					data
				}) => {
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
			// 添加、修改
			classAdd(row) {
				console.log('row', row)
				if (row != 0) {
					this.titles = '修改'
					this.aiKeyWordsId = row.aiKeyWordsId
					this.keyWords = row.keyWords
					this.status = row.status
					this.content = row.content
				} else {
					this.titles = '添加'
					this.aiKeyWordsId = ''
					this.keyWords = ''
					this.content = ''
					this.status = 1
					
				}
				this.dialogFormVisible9 = true
			},
			// 启用与否
			change(id, status) {
				this.$http({
					url: this.$http.adornUrl('aiKeyWords/updateAiKeyWords'),
					method: 'post',
					data: this.$http.adornData({
						'aiKeyWordsId': id,
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
				if (this.keyWords == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入关键字',
						type: 'warning'
					})
					return
				}
				if (this.content == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入回复内容',
						type: 'warning'
					})
					return
				}
				

				if (this.titles == '添加') {
					var urls = 'aiKeyWords/insertAiKeyWords'
				} else {
					var urls = 'aiKeyWords/updateAiKeyWords'
				}
				this.$http({
					url: this.$http.adornUrl(urls),
					method: 'post',
					// params: this.$http.adornParams({
					data: this.$http.adornData({
						'aiKeyWordsId': this.aiKeyWordsId,
						'keyWords': this.keyWords,
						'content': this.content,
						'status': this.status,
						
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
				let delid = row.aiKeyWordsId
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`aiKeyWords/deleteAiKeyWords?aiKeyWordsId=${delid}`),
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
		},
		mounted() {
			this.classSelect()
		}
	}
</script>

<style scoped>
.aikeyword-page__filter-select {
	width: 150px;
}

.aikeyword-page__filter-input {
	width: 200px;
}

.aikeyword-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.aikeyword-page__tabs {
	margin-bottom: 0;
}

.aikeyword-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.aikeyword-page__tabs .nf-table-card {
	margin-bottom: 0;
}

.aikeyword-page__pagination {
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