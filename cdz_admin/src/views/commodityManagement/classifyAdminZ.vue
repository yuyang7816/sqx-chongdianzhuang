<template>
	<div class="nf-page classify-admin-z-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">门店餐饮</span>
			<h2 class="nf-page-hero__title">商品分类</h2>
			<p class="nf-page-hero__desc">管理商品分类，支持添加、修改与删除分类信息。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">
				<i class="el-icon-s-operation nf-filter-card__head-icon"></i>
				<span>快捷操作</span>
			</div>
			<div class="classify-admin-z-page__filter-actions">
				<el-button type="primary" icon="el-icon-refresh" @click="Notice">刷新</el-button>
			</div>
		</div>

		<div class="nf-table-card">
			<div class="classify-admin-z-page__toolbar">
				<el-button :disabled="!isAuth('classifyAdminZ:add')" type="primary" icon="document" @click="addNotice">添加分类</el-button>
			</div>
			<el-table v-loading="tableDataLoading" :data="typeDatas" row-key="id">
				<el-table-column label="编号" prop="classifyId">
				</el-table-column>
				<el-table-column label="名称" prop="classifyName">
				</el-table-column>
				<el-table-column label="操作" width="180">
					<template slot-scope="scope">
						<el-button :disabled="!isAuth('classifyAdminZ:update')" size="mini" type="primary"
							@click="updates(scope.$index, scope.row)">修改
						</el-button>
						<el-button :disabled="!isAuth('classifyAdminZ:delete')" size="mini" type="danger"
							@click="deletes(scope.row)">
							删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
		</div>

		<!-- 添加分类 -->
		<el-dialog :visible.sync="dialogFormVisible" center :title="titles">
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">分类名称：</span>
				<el-input v-model="classifyName" placeholder="请输入分类名称" style="width:50%;"></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="releasNoticeTo()">确 定</el-button>
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
				classifyId: '',
				classifyName: '',
				tableDataLoading: false,
				dialogFormVisible: false,
				typeDatas: [],
				titles: '添加',
			}
		},
		methods: {

			// 添加分类弹框
			addNotice() {
				this.dialogFormVisible = true
				this.titles = '添加'
				this.classifyId = ''
				this.classifyName = ''
				this.classSelect()
			},
			// 添加分类
			releasNoticeTo() {
				if (this.classifyName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入分类名称',
						type: 'warning'
					})
					return
				}
				if (this.titles == '添加') {
					var url = 'admin/takeoutGoods/insertClassify'
				}
				if (this.titles == '修改') {
					var url = 'admin/takeoutGoods/updateClassify'
				}
				this.$http({
					url: this.$http.adornUrl(url),
					method: 'post',
					data: this.$http.adornData({
						'classifyId': this.classifyId,
						'classifyName': this.classifyName
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
			// 修改弹框
			updates(index, rows) {
				this.classifyId = rows.classifyId
				this.classifyName = rows.classifyName
				this.titles = '修改'
				this.dialogFormVisible = true

			},

			// 删除分类
			deletes(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/takeoutGoods/deleteClassify'),
						method: 'get',
						params: this.$http.adornParams({
							'classifyId': row.classifyId
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
								type: 'warning',
								duration: 1500,
								onClose: () => {}
							})
						}

					})
				}).catch(() => {})

			},
			// 获取分类数据
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/takeoutGoods/selectAllClassify'),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
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
	}
</script>

<style scoped>
.classify-admin-z-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.classify-admin-z-page__toolbar {
	display: flex;
	justify-content: flex-end;
	margin-bottom: 12px;
}

.nf-table-card .el-button + .el-button {
	margin-left: 0 !important;
	margin-top: 5px !important;
}
</style>
