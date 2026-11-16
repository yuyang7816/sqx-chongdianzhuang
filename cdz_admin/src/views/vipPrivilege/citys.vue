<template>
	<div class="nf-page citys-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">城市配置</span>
			<h2 class="nf-page-hero__title">城市管理</h2>
			<p class="nf-page-hero__desc">维护系统可用城市列表，支持添加、修改、删除与启用状态切换。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">
				<i class="el-icon-s-operation nf-filter-card__head-icon"></i>
				<span>快捷操作</span>
			</div>
			<div class="citys-page__filter-actions">
				<el-button type="primary" icon="el-icon-refresh" @click="dataSelect">刷新</el-button>
				<el-button type="primary" icon="document" :disabled="!isAuth('citys:add')" @click="addNotice">添加</el-button>
			</div>
		</div>

		<div class="nf-table-card">
			<el-table v-loading="tableDataLoading" :data="tableData.list">
				<el-table-column fixed prop="cityId" label="编号" width="50">
				</el-table-column>
				<el-table-column prop="city" label="城市">
				</el-table-column>
				<el-table-column prop="status" label="状态">
					<template slot-scope="scope">
						<el-switch v-model="scope.row.status" @change="change(scope.row)"
							:active-value="openValue" :disabled="!isAuth('userList:delete')"
							:inactive-value="closeValue" active-color="#5cc477" inactive-color="#ff4949">
						</el-switch>
					</template>
				</el-table-column>
				<el-table-column label="操作" width="180">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('citys:update')"
							@click="amendBanner(scope.$index, scope.row)">修改
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('citys:delete')"
							@click="deletes(scope.row)">删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="citys-page__pagination">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
				</el-pagination>
			</div>
		</div>

		<el-dialog :title="title" :visible.sync="dialogFormVisible" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">城市名称：</span>
				<el-input style="width:50%;" v-model="memberName" placeholder="请输入城市名称"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">排序：</span>
				<el-radio-group v-model="status">
					<el-radio :label="1">开启</el-radio>
					<el-radio :label="2">关闭</el-radio>
				</el-radio-group>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="addNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				page: 1,
				limit: 10,
				tableDataLoading: true,
				dialogFormVisible: false,
				tableData: [],
				memberName: '',
				memberImg: '',
				memberId: '',
				status: 1,
				title: '添加',
				openValue: 1,
				closeValue: 2,
			}
		},
		methods: {
			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('city/selectCityList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'city': '',
					})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						console.log('会员列表', data)
						this.tableDataLoading = false
						let returnData = data.data;
						this.tableData = returnData;
					}

				})
			},
			//上传成功
			handleAvatarSuccess(file) {
				this.memberImg = file.data
			},
			addNotice() {
				this.memberId = ''
				this.memberName = ''
				this.status = 1
				this.title = '添加'
				this.dialogFormVisible = true
			},
			handleCurrentChange(val) {
				this.page = val
				this.dataSelect()
			},
			handleSizeChange(val) {
				this.limit = val
				this.dataSelect()
			},
			// 添加banner图
			addNoticeTo() {
				
				if (this.memberName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入城市名',
						type: 'warning'
					});
					return
				}
				this.classify = 1
				if (this.title == '添加') {
					var url = 'city/insertCity'
				} else {
					var url = 'city/updateCity'
				}
				this.$http({
					url: this.$http.adornUrl(url),
					method: 'post',
					data: this.$http.adornData({
						'cityId': this.memberId,
						'city': this.memberName,
						'status': this.status
					})
				}).then(({
					data
				}) => {
					if(data.code==0){
						this.dialogFormVisible = false
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
							type: 'warning',
							duration: 1500,
							onClose: () => {
							}
						})
					}
					
				})
			},
			// 修改
			amendBanner(index, rows, a) {
				if (a == 1) {
					this.pl = 1
				} else {
					this.pl = 0
				}
				this.dialogFormVisible = true
				this.memberId = rows.cityId
				this.status = rows.status
				this.memberName = rows.city
				this.title = '修改'
			},
			// 修改
			amendNoticeTo() {
				this.$http({
					url: this.$http.adornUrl(
						`?memberId=${this.memberId}&memberImg=${this.memberImg}&sort=${this.sort}&memberName=${this.memberName}`
						),
					method: 'post',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					this.dialogFormVisible = false
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
			//删除一级
			deletes(row) {
				let delid = row.cityId
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`city/deleteCity?cityId=${delid}`),
						method: 'post',
						data: this.$http.adornData({})
					}).then(({
						data
					}) => {
						if(data.code==0){
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
						}else{
						
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {
							}
						})
					}
						
					})
				}).catch(() => {})
			},
			// 状态
			change(row) {
				this.$http({
					url: this.$http.adornUrl('city/updateCity'),
					method: 'post',
					data: this.$http.adornData({
						'cityId': row.cityId,
						'city': row.city,
						'status': row.status
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
							type: 'warning',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
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
.citys-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.citys-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}
</style>
