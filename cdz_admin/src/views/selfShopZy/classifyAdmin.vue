<template>
	<div class="nf-page classify-admin-zy-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">自营商城</span>
			<h2 class="nf-page-hero__title">商品分类</h2>
			<p class="nf-page-hero__desc">管理商品分类树形结构，支持添加一级/二级分类、修改分类信息与删除分类。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">
				<i class="el-icon-s-operation nf-filter-card__head-icon"></i>
				<span>快捷操作</span>
			</div>
			<div class="classify-admin-zy-page__filter-actions">
				<el-button type="primary" icon="el-icon-refresh" @click="Notice">刷新</el-button>
			</div>
		</div>

		<div class="nf-table-card">
			<div class="classify-admin-zy-page__toolbar">
				<el-button :disabled="!isAuth('classifyAdminZy:add')" type="primary" icon="document" @click="addNotice">添加分类</el-button>
			</div>
			<el-table v-loading="tableDataLoading" :data="typeDatas" row-key="id">
				<el-table-column prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="img" label="图片">
					<template slot-scope="scope">
						<img :src="scope.row.img" alt="" width="40" height="40">
					</template>
				</el-table-column>
				<el-table-column prop="name" label="名称">
				</el-table-column>
				<el-table-column prop="sort" label="排序号">
				</el-table-column>
				<el-table-column prop="createAt" label="创建时间">
				</el-table-column>
				<el-table-column label="操作" width="180">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('classifyAdminZy:update')"
							@click="updates(scope.$index, scope.row)">修改
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('classifyAdminZy:delete')"
							@click="deletes(scope.row)">删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
		</div>
		<!-- 添加分类 -->
		<el-dialog title="添加分类" :visible.sync="dialogFormVisible" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">选择分类：</span>
				<el-select v-model="parentIdnum" placeholder="请选择分类" style="width:50%;">
					<el-option v-for="item in classnum" :key="item.value" :label="item.label" :value="item.value">
					</el-option>
				</el-select>
			</div>
			<div style="margin-bottom: 10px;" v-if="parentIdnum==1">
				<span style="width: 200px;display: inline-block;text-align: right;">上级分类：</span>
				<el-cascader style="width:50%;" v-model="value" :options="classDatas" :show-all-levels="false"
					:change-on-select="true" :props="{ expandTrigger: 'hover' }" @change="handleChange">
				</el-cascader>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">分类名称：</span>
				<el-input style="width:50%;" v-model="name" placeholder="请输入分类名称"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">排序号：</span>
				<el-input style="width:50%;" v-model="sort" placeholder="请输入排序号" min="0" type="number"
					onkeyup="value=value.replace(/^(0+)|[^\d]+/g,'')"></el-input>
			</div>
			<div style="margin-bottom: 10px;display:flex;">
				<span style="width: 200px;display: inline-block;text-align: right;">分类图标：</span>
				<div
					style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
					<el-upload class="avatar-uploader" v-model="img" :action="$http.adornUrlUp('alioss/upload')"
						:show-file-list="false" :on-success="handleAvatarSuccess">
						<img v-if="img" :src="img" class="avatar"
							style="border-radius: 6px;width: 148px;height: 148px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon"></i>
					</el-upload>
				</div>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="releasNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 修改分类 -->
		<el-dialog title="修改分类" :visible.sync="dialogFormVisible1" center>
			<el-form :model="form">
				<el-form-item label="分类名称：" :label-width="formLabelWidth">
					<el-input v-model="form.name" style="width:65%;" placeholder="请输入分类名称"></el-input>
				</el-form-item>
				<el-form-item label="排序号：" :label-width="formLabelWidth">
					<el-input v-model="form.sort" style="width:65%;" placeholder="请输入排序号" min="0" type="number"
						onkeyup="value=value.replace(/^(0+)|[^\d]+/g,'')"></el-input>
				</el-form-item>
				<el-form-item label="图标：" :label-width="formLabelWidth">
					<div
						style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
						<el-upload class="avatar-uploader" v-model="form.img"
							:action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
							:on-success="handleAvatarSuccess2">
							<img v-if="form.img" :src="form.img" class="avatar"
								style="border-radius: 6px;width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon"></i>
						</el-upload>
					</div>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible1 = false">取 消</el-button>
				<el-button type="primary" @click="amendNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				size: 5,
				page: 1,
				name: '',
				sort: '',
				img: '',
				classn: '',
				value: [],
				parentId: 0,
				parentIdnum: '',
				form: {
					id: '',
					name: '',
					img: '',
					parentId: '',
					sort: ''
				},
				classnum: [{
						value: '0',
						label: '一级分类'
					},
					{
						value: '1',
						label: '二级分类'
					}
				],
				formLabelWidth: '200px',
				tableDataLoading: false,
				dialogFormVisible1: false,
				dialogFormVisible: false,
				typeDatas: [],
				classDatas: []
			}
		},
		methods: {
			// 图标上传
			handleAvatarSuccess(file) {
				this.img = file.data;
			},
			// 图标上传
			handleAvatarSuccess2(file) {
				this.form.img = file.data;
			},
			handleChange(value) {
				this.value.forEach(element => {
					this.parentId = element;
				});
			},
			// 添加分类弹框
			addNotice() {
				this.dialogFormVisible = true
				this.classSelect()
			},
			// 添加分类
			releasNoticeTo() {
				if (this.name == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入分类名称',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl('goodsType/save'),
					method: 'post',
					data: this.$http.adornData({
						'parentId': this.parentId,
						'name': this.name,
						'img': this.img,
						'sort': this.sort
					})
				}).then(({
					data
				}) => {
					this.dialogFormVisible = false
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.name = ''
							this.img = ''
							this.parentId = 0
							this.parentIdnum = ''
							this.dataSelect()
						}
					})
				})
			},
			// 修改弹框
			updates(index, rows) {
				this.dialogFormVisible1 = true;
				this.form.id = rows.id;
				this.form.name = rows.name;
				this.form.parentId = rows.parentId;
				this.form.sort = rows.sort;
				this.form.createAt = rows.createAt;
				this.form.img = rows.img
			},
			// 修改商品类别
			amendNoticeTo() {
				this.$http({
					url: this.$http.adornUrl('goodsType/save'),
					method: 'post',
					data: this.$http.adornData({
						'id': this.form.id,
						'name': this.form.name,
						'img': this.form.img,
						'parentId': this.form.parentId,
						'createAt': this.form.createAt,
						'sort': this.form.sort

					})
				}).then(({
					data
				}) => {
					this.dialogFormVisible1 = false
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.dataSelect()
						}
					})
				})
			},
			// 删除分类
			deletes(row) {
				if (row.children == null || row.children.length == 0) {
					this.$confirm(`确定删除此条信息?`, '提示', {
						confirmButtonText: '确定',
						cancelButtonText: '取消',
						type: 'warning'
					}).then(() => {
						this.$http({
							url: this.$http.adornUrl('goodsType/delete'),
							method: 'get',
							params: this.$http.adornParams({
								'id': row.id
							})
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
				} else {
					this.$message({
						message: '请先删除二级分类',
						type: 'error',
						duration: 1500,
					})
					return
				}
			},
			// 获取分类数据
			classSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('goodsType/result1'),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.classDatas = returnData
				})
			},
			// 获取分类数据
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('goodsType/list'),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.typeDatas = returnData
				})
			},
			Notice(){
				this.dataSelect()
			},
		},
		mounted() {
			this.dataSelect()
		}
	};
</script>

<style scoped>
.classify-admin-zy-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.classify-admin-zy-page__toolbar {
	display: flex;
	justify-content: flex-end;
	margin-bottom: 12px;
}

.nf-table-card .el-button + .el-button {
	margin-left: 0 !important;
	margin-top: 5px !important;
}
</style>
