<template>
	<div class="nf-page allocation-list-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">系统管理</span>
			<h2 class="nf-page-hero__title">配置列表</h2>
			<p class="nf-page-hero__desc">管理系统全局、微信、支付宝、短信等各类配置项，支持按分类查看与编辑。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">
				<i class="el-icon-s-operation nf-filter-card__head-icon"></i>
				<span>快捷操作</span>
			</div>
			<div class="allocation-list-page__filter-actions">
				<el-button type="primary" icon="el-icon-refresh" @click="dataSelect">刷新</el-button>
			</div>
		</div>

		<el-tabs v-model="activeName" class="allocation-list-page__tabs" @tab-click="handleClick">
			<el-tab-pane label="全局系统配置" name="first">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型">
					</el-table-column>
					<el-table-column prop="value" label="内容" width="500">
						<template slot-scope="scope">
							<div
								style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
								{{scope.row.value}}</div>
						</template>
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				</div>
			</el-tab-pane>
			<el-tab-pane label="微信配置" name="fifth">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型">
					</el-table-column>
					<el-table-column prop="value" label="内容" width="500">
						<template slot-scope="scope">
							<div
								style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
								{{scope.row.value}}</div>
						</template>
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				</div>
			</el-tab-pane>
			<el-tab-pane label="支付宝配置" name="sixth">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型">
					</el-table-column>
					<el-table-column prop="value" label="内容" width="500">
						<template slot-scope="scope">
							<div
								style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
								{{scope.row.value}}</div>
						</template>
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				</div>
			</el-tab-pane>
			<el-tab-pane label="短信配置" name="seventh">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型">
					</el-table-column>
					<el-table-column prop="value" label="内容" width="500">
						<template slot-scope="scope">
							<div
								style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
								{{scope.row.value}}</div>
						</template>
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				</div>
			</el-tab-pane>
			<el-tab-pane label="协议配置" name="xieyi">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型">
					</el-table-column>
					<el-table-column prop="value" label="内容" width="500">
						<template slot-scope="scope">
							<div
								style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
								{{scope.row.value}}</div>
						</template>
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				</div>
			</el-tab-pane>
			<!-- <el-tab-pane label="APP消息推送配置" name="app">
			  <el-table v-loading="tableDataLoading" :data="tableData">
			    <el-table-column fixed prop="type" label="编号" align="center" width="80">
			    </el-table-column>
			    <el-table-column prop="min" label="类型">
			    </el-table-column>
			    <el-table-column prop="value" label="内容" width="500">
			      <template slot-scope="scope">
			        <div style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">{{scope.row.value}}</div>
			      </template>
			    </el-table-column>
			    <el-table-column prop="createAt" label="创建时间">
			    </el-table-column>
			    <el-table-column label="操作" prop="id" width="120">
			      <template slot-scope="scope">
			        <el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
			          @click="amend(scope.$index, scope.row)">编辑
			        </el-button>
			      </template>
			    </el-table-column>
			  </el-table>
			</el-tab-pane> -->
			<el-tab-pane label="文件上传配置" name="wenjian">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型">
					</el-table-column>
					<el-table-column prop="value" label="内容" width="500">
						<template slot-scope="scope">
							<div
								style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
								{{scope.row.value}}</div>
						</template>
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				</div>
			</el-tab-pane>
			<!-- <el-tab-pane label="佣金配置" name="yongjin">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型">
					</el-table-column>
					<el-table-column prop="value" label="内容" width="500">
						<template slot-scope="scope">
							<div
								style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
								{{scope.row.value}}</div>
						</template>
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-tab-pane> -->
			<el-tab-pane label="服务费配置" name="fuwufei">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型">
					</el-table-column>
					<el-table-column prop="value" label="内容" width="500">
						<template slot-scope="scope">
							<div
								style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
								{{scope.row.value}}</div>
						</template>
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				</div>
			</el-tab-pane>
			<el-tab-pane label="开关配置" name="kaiguan">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型" width="250">
					</el-table-column>
					<el-table-column prop="value" label="内容">
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间" width="180">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				</div>
			</el-tab-pane>
			<el-tab-pane label="图片配置" name="image">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型" width="250">
					</el-table-column>
					<el-table-column prop="value" label="内容">
						<template slot-scope="scope">
							<div
								style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
								{{scope.row.value}}
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间" width="180">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				</div>
			</el-tab-pane>
		</el-tabs>
		<!-- 修改弹框 -->
		<el-dialog title="修改" :visible.sync="dialogFormVisible" center>
			<el-form :model="form">
				<el-form-item label="配置类型：" :label-width="formLabelWidth">
					<el-input v-model="form.min" style="width:65%;" readonly></el-input>
				</el-form-item>
				<el-form-item label="内容：" :label-width="formLabelWidth">
					<quill-editor v-if="form.conditionFrom=='xieyi' " ref="myTextEditor" v-model="form.value"
						:options="quillOption"
						style="padding-bottom: 50px;height: 300px;width: 72%;display: inline-table;margin-bottom: 60px;">
					</quill-editor>
					<div v-else-if="form.conditionFrom=='kaiguan'">
						<div>
							<el-radio-group v-model="form.value">
								<el-radio label="是">是</el-radio>
								<el-radio label="否">否</el-radio>
							</el-radio-group>
						</div>
					</div>
					<div v-else-if="form.conditionFrom=='image'"
						style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
						<el-upload class="avatar-uploader" v-model="form.value"
							:action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
							:on-success="handleAvatarSuccess">
							<img v-if="form.value" :src="form.value" class="avatar"
								style="border-radius: 6px;width: 140px;height: 140px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon"></i>
						</el-upload>
					</div>
					<el-input v-else v-model="form.value" style="width:65%;"></el-input>
					<!-- <el-input v-model="form.value" style="width:65%;"></el-input> -->
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="amendNoticeTo()">确 定</el-button>
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
	export default {
		name: 'news',
		components: {
			quillEditor
		},
		data() {
			return {
				openValue: '是',
				closeValue: '否',
				limit: 10,
				page: 0,
				min: '',
				value: '',
				id: '',
				condition: 'xitong',
				activeName: 'first',
				dialogFormVisible: false,
				dialogFormVisible1: false,
				tableDataLoading: true,
				formLabelWidth: '200px',
				form: {
					id: '',
					min: '',
					value: '',
					type: '',
				},

				values: [{
						value: 1,
						label: '是'
					},
					{
						value: 2,
						label: '否'
					}
				],
				tableData: [],
        quillOption: quillConfig,
				checkBoxData: [] //多选框选择的值
			}
		},
		methods: {
			//上传成功
			handleAvatarSuccess(file) {
				this.form.value = file.data
			},
			handleClick(tab, event) {
				if (tab._props.label == '全局系统配置') {
					this.condition = 'xitong'
					this.dataSelect()
				}
				if (tab._props.label == '首页配置') {
					this.condition = 'shouye'
					this.dataSelect()
				}
				if (tab._props.label == '微信配置') {
					this.condition = 'weixin'
					this.dataSelect()
				}
				if (tab._props.label == '支付宝配置') {
					this.condition = 'zhifubao'
					this.dataSelect()
				}
				if (tab._props.label == '短信配置') {
					this.condition = 'duanxin'
					this.dataSelect()
				}
				if (tab._props.label == '任务系统配置') {
					this.condition = 'renwu'
					this.dataSelect()
				}
				if (tab._props.label == '协议配置') {
					this.condition = 'xieyi'
					this.dataSelect()
				}
				if (tab._props.label == 'APP消息推送配置') {
					this.condition = 'push'
					this.dataSelect()
				}
				if (tab._props.label == '文件上传配置') {
					this.condition = 'oss'
					this.dataSelect()
				}
				if (tab._props.label == '佣金配置') {
					this.condition = 'yongjin'
					this.dataSelect()
				}
				if (tab._props.label == '服务费配置') {
					this.condition = 'fuwufei'
					this.dataSelect()
				}
				if (tab._props.label == '开关配置') {
					this.condition = 'kaiguan'
					this.dataSelect()
				}
				if (tab._props.label == '图片配置') {
					this.condition = 'image'
					this.dataSelect()
				}

			},
			// 修改弹框
			amend(index, rows) {
				this.dialogFormVisible = true;
				this.form.id = rows.id;
				this.form.type = rows.type
				this.form.min = rows.min;
				this.form.value = rows.value;
				this.form.max = rows.max;
				this.form.createAt = rows.createAt;
				this.form.conditionFrom = rows.conditionFrom
			},
			// 修改
			amendNoticeTo() {
				this.$http({
					url: this.$http.adornUrl('common/update'),
					method: 'post',
					data: this.$http.adornData({
						'id': this.form.id,
						'type': this.form.type,
						'value': this.form.value,
						'min': this.form.min,
						'max': this.form.max,
						'createAt': this.form.createAt,
						'conditionFrom': this.form.conditionFrom
					})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						this.dialogFormVisible = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					} else {
						this.$message.error(data.msg)
					}
				})
			},

			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl(`common/type/condition/${this.condition}`),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						this.tableDataLoading = false
						let returnData = data.data;
						this.tableData = returnData
					}
				})
			},
		},
		mounted() {
			this.dataSelect()
		}
	}
</script>

<style scoped>
.allocation-list-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.allocation-list-page__tabs {
	margin-bottom: 0;
}

.allocation-list-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.allocation-list-page__tabs .nf-table-card {
	margin-bottom: 0;
}

.allocation-list-page__tabs >>> .el-tabs__item.is-active {
	color: #5cc477;
}

.allocation-list-page__tabs >>> .el-tabs__active-bar {
	background-color: #5cc477;
}

.eit {
	height: 120px;
}
</style>