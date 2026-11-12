<template>
	<div class="nf-page divide-into-jl-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">教练模块</span>
			<h2 class="nf-page-hero__title">教练分成规则</h2>
			<p class="nf-page-hero__desc">管理教练等级分成配置，支持按等级设置最低业绩、加钟比例与佣金比例。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">
				<i class="el-icon-s-operation nf-filter-card__head-icon"></i>
				<span>快捷操作</span>
			</div>
			<div class="divide-into-jl-page__filter-actions">
				<el-button type="primary" icon="el-icon-refresh" @click="Notice">刷新</el-button>
			</div>
		</div>

		<div class="nf-table-card">
			<div class="divide-into-jl-page__toolbar">
				<el-button
					:disabled="!isAuth('divideIntoJl:add')"
					type="primary"
					icon="document"
					@click="addNotice">添加</el-button>
			</div>
			<el-table v-loading="tableDataLoading" :data="tableData.list">
				<el-table-column fixed prop="artificerGradeId" label="编号" width="50">
				</el-table-column>
				<el-table-column prop="gradeIndex" label="等级">
				</el-table-column>
				<el-table-column prop="minPerformance" label="最低业绩">
				</el-table-column>
				<el-table-column prop="addBellRate" label="加钟比例">
				</el-table-column>
				<el-table-column prop="rate" label="佣金比例">
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间">
				</el-table-column>
				<el-table-column label="操作" width="180">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('divideIntoJl:update')"
							@click="amendBanner(scope.row)">修改
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('divideIntoJl:delete')"
							@click="deletes(scope.row)">删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="divide-into-jl-page__pagination">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
				</el-pagination>
			</div>
		</div>

		<el-dialog :title="title" :visible.sync="dialogFormVisible" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">等级：</span>
				<el-input style="width:50%;" v-model="gradeIndex" type="number" placeholder="请输入等级"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">最低业绩：</span>
				<el-input style="width:50%;" v-model="minPerformance" type="number" min="0" placeholder="请输入最低业绩"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">加钟比例：</span>
				<el-input style="width:50%;" v-model="addBellRate" type="number" min="0" placeholder="请输入加钟比例"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">佣金比例：</span>
				<el-input style="width:50%;" v-model="rate" type="number" min="0" placeholder="请输入佣金比例"></el-input>
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
				page1: 1,
				size1: 10,
				tableDataLoading: true,
				dialogFormVisible: false,
				tableData: [],
				tableDataY: {},
				gradeIndex: '',
				minPerformance:'',
				addBellRate:'',
				rate:'',
				artificerGradeId: '',
				giveNum: '',
				couponId: '',
				couponName: '',
				sort: '',
				payClassifyId: '',
				title: '添加',
				dialogFormVisible4: false,
				cityT:'',
			}
		},
		methods: {
			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('artificerGrade/selectArtificerGradeList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
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
			Notice() {
				this.page = 1
				this.dataSelect()
			},
			addNotice() {
				this.gradeIndex = ''
				this.artificerGradeId = ''
				this.minPerformance = ''
				this.addBellRate = ''
				this.rate = ''
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
				if (this.gradeIndex === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入等级',
						type: 'warning'
					});
					return
				}
				if (this.minPerformance === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入最低业绩',
						type: 'warning'
					});
					return
				}
				if (this.addBellRate === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入加钟比例',
						type: 'warning'
					});
					return
				}
				if (this.rate === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入佣金比例',
						type: 'warning'
					});
					return
				}
				if (this.title == '添加') {
					var url = 'artificerGrade/insertArtificerGrade'
				} else {
					var url = 'artificerGrade/updateArtificerGrade'
				}
				this.$http({
					url: this.$http.adornUrl(url),
					method: 'post',
					data: this.$http.adornData({
						'gradeIndex': this.gradeIndex,
						'artificerGradeId': this.artificerGradeId,
						'minPerformance': this.minPerformance,
						'addBellRate': this.addBellRate,
						'rate': this.rate,
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
			amendBanner(rows) {
				this.gradeIndex = rows.gradeIndex
				this.artificerGradeId = rows.artificerGradeId
				this.minPerformance = rows.minPerformance
				this.addBellRate = rows.addBellRate
				this.rate = rows.rate
				this.title = '修改'
				this.dialogFormVisible = true
			},
			//删除一级
			deletes(row) {
				let delid = row.artificerGradeId
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('artificerGrade/deleteArtificerGradeById'),
						method: 'post',
						params: this.$http.adornParams({
							'artificerGradeId':delid
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
			},
		},
		mounted() {
			this.dataSelect()
		}
	}
</script>

<style scoped>
.divide-into-jl-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.divide-into-jl-page__toolbar {
	display: flex;
	justify-content: flex-end;
	margin-bottom: 12px;
}

.divide-into-jl-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}
</style>
