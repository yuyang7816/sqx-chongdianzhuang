<template>
	<div class="nf-page transfer-gift-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">积分管理</span>
			<h2 class="nf-page-hero__title">转赠记录</h2>
			<p class="nf-page-hero__desc">查看用户余额转赠与接受流水，支持按天/月/年维度筛选统计赠送与接受金额，可切换赠送记录与接受记录列表。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">
				<i class="el-icon-date nf-filter-card__head-icon"></i>
				<span>统计筛选</span>
			</div>
			<div class="transfer-gift-page__stat-filter">
				<el-select v-model="flag" class="transfer-gift-page__filter-select" @change="orderfenxi">
					<el-option v-for="item in flags" :key="item.value" :label="item.label" :value="item.value">
					</el-option>
				</el-select>
				<el-date-picker
					class="transfer-gift-page__filter-date"
					v-model="info.stockDate"
					align="right"
					type="date"
					format="yyyy-MM-dd"
					value-format="yyyy-MM-dd"
					placeholder="选择时间"
					@change="orderfenxi">
				</el-date-picker>
			</div>
		</div>

		<div class="transfer-gift-page__kpi-grid" v-loading="tableDataLoadingJe">
			<div class="transfer-gift-page__kpi-card">
				<div class="transfer-gift-page__kpi-label">赠送金额</div>
				<div class="transfer-gift-page__kpi-value">
					<span>{{ moneyStat.giveMoney ? moneyStat.giveMoney : 0 }}</span>
				</div>
			</div>
			<div class="transfer-gift-page__kpi-card">
				<div class="transfer-gift-page__kpi-label">接受金额</div>
				<div class="transfer-gift-page__kpi-value">
					<span>{{ moneyStat.takeMoney ? moneyStat.takeMoney : 0 }}</span>
				</div>
			</div>
		</div>

		<el-tabs v-model="activeName" @tab-click="handleClick" class="transfer-gift-page__tabs">
			<el-tab-pane label="赠送记录" name="first">
				<div class="nf-table-card">
					<el-table v-loading="tableDataLoading" :data="tableData.records">
						<el-table-column prop="id" label="编号" width="100"></el-table-column>
						<el-table-column prop="nickname" label="用户昵称" width="120">
							<template slot-scope="scope">
								<span class="transfer-gift-page__link" v-if="scope.row.userId"
									@click="updates(scope.row.userId)">{{ scope.row.userName ? scope.row.userName : '-' }}</span>
								<span class="transfer-gift-page__empty" v-else>-</span>
							</template>
						</el-table-column>
						<el-table-column prop="title" label="标题"></el-table-column>
						<el-table-column prop="content" label="内容"></el-table-column>
						<el-table-column prop="money" label="金额" width="100">
							<template slot-scope="scope">
								<span class="transfer-gift-page__amount--minus" v-if="scope.row.type==2">- {{ scope.row.money }}</span>
								<span class="transfer-gift-page__amount--plus" v-if="scope.row.type==1">+ {{ scope.row.money }}</span>
							</template>
						</el-table-column>
						<el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
					</el-table>
					<div class="transfer-gift-page__pagination">
						<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
							:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
							layout="total,sizes, prev, pager, next" :total="tableData.total">
						</el-pagination>
					</div>
				</div>
			</el-tab-pane>

			<el-tab-pane label="接受记录" name="putong">
				<div class="nf-table-card">
					<el-table v-loading="tableDataLoadingKm" :data="tableData.records" @selection-change="changeFunP">
						<el-table-column prop="id" label="编号" width="100"></el-table-column>
						<el-table-column prop="nickname" label="用户昵称" width="120">
							<template slot-scope="scope">
								<span class="transfer-gift-page__link" v-if="scope.row.userId"
									@click="updates(scope.row.userId)">{{ scope.row.userName ? scope.row.userName : '-' }}</span>
								<span class="transfer-gift-page__empty" v-else>-</span>
							</template>
						</el-table-column>
						<el-table-column prop="title" label="标题"></el-table-column>
						<el-table-column prop="content" label="内容"></el-table-column>
						<el-table-column prop="money" label="金额" width="100">
							<template slot-scope="scope">
								<span class="transfer-gift-page__amount--minus" v-if="scope.row.type==2">- {{ scope.row.money }}</span>
								<span class="transfer-gift-page__amount--plus" v-if="scope.row.type==1">+ {{ scope.row.money }}</span>
							</template>
						</el-table-column>
						<el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
					</el-table>
					<div class="transfer-gift-page__pagination">
						<el-pagination @size-change="handleSizeChangeList" @current-change="handleCurrentChangeList"
							:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
							layout="total,sizes, prev, pager, next" :total="tableData.total">
						</el-pagination>
					</div>
				</div>
			</el-tab-pane>
		</el-tabs>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				limit: 10,
				page: 1,
				size1: 10,
				page1: 1,
				form: {
					typeId: '',
					money: '',
					remarks: '',
					validDay: '',
					sdkClassify: 1,
					couponIds: [],
				},
				formLabelWidth: '200px',
				tableDataLoading: false,
				tableDataLoadingKm: false,
				dialogFormVisible: false,
				tableData: {},
				tableDataKm: {},
				activeName: 'first',
				classify: 10,
				couponIssueNameT: '',
				statusT: 0,
				couponIds: [],
				couponIdList: [],
				dialogFormVisible4: false,
				dialogFormVisible5: false,
				tableDataLoadingY: false,
				tableDataYhq: {},
				typeY: 1,
				statusY: '',
				couponNameY: '',
				xzList: [],
				memberList: [{
						value: '',
						title: '全部'
					},
					{
						value: 1,
						title: '开启'
					},
					{
						value: 2,
						title: '关闭'
					},
				],
				typeList: [
					// {
					// 	value: '',
					// 	title: '全部'
					// },
					{
						value: 1,
						title: '团购券'
					},
					{
						value: 2,
						title: '积分兑换券'
					},
					{
						value: 3,
						title: '新人券'
					},
				],

				titles: '添加',
				tableDataList: [],
				dialogFormVisible3: false,
				num: 0,
				couponCardTypeId: '',
				status: '',
				statesnum: [{
						label: '全部',
						value: ''
					},
					{
						label: '待使用',
						value: 0
					},
					{
						label: '已使用',
						value: 1
					},
					{
						label: '已过期',
						value: 2
					}
				],
				dialogFormVisibleKm: false,
				checkBoxData: [], //多选框选择的值
				userName: '',
				sdkContent: '',
				startTime: '',
				endTime: '',
				info2: {
					stockDate2: this.getNowTime2(), //日期
				},
				dialogFormVisibleQd: false,
				dataList: [],
				dataListLoading: false,
				sysUserId: '',
				sysUserName: '',
				sysUserNameT: '',
				
				moneyStat:{},
				tableDataLoadingJe:false,
				flag: 1,
				info: {
					stockDate: this.getNowTime(), //日期
				},
				flags: [{
					value: 1,
					label: '按天查询'
				}, {
					value: 2,
					label: '按月查询'
				}, {
					value: 3,
					label: '按年查询'
				}],
			}
		},
		methods: {
			//处理默认选中当前日期
			getNowTime() {
				var now = new Date()
				var year = now.getFullYear() //得到年份
				var month = now.getMonth() //得到月份
				var date = now.getDate() //得到日期
				month = month + 1
				month = month.toString().padStart(2, '0')
				date = date.toString().padStart(2, '0')
				var defaultDate = `${year}-${month}-${date}`
				return defaultDate
				this.$set(this.info, 'stockDate', defaultDate)
			},
			//处理默认选中当前日期
			getNowTime2() {
				var now = new Date()
				var year = now.getFullYear() //得到年份
				var month = now.getMonth() - now.getMonth() //得到月份
				var date = now.getDate() - now.getDate() + 1 //得到日期
				month = month + 1
				month = month.toString().padStart(2, '0')
				date = date.toString().padStart(2, '0')
				var defaultDate = `${year}-${month}-${date}`
				return defaultDate
				this.$set(this.info, 'stockDate', defaultDate)
			},
			// 多选
			changeFunP(val) {
				this.checkBoxData = val;
			},
			handleSizeChange(val) {
				this.limit = val;
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.dataSelect()
			},
			handleSizeChange(val) {
				this.size = val;
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.dataSelect()
			},
			handleSizeChangeList(val) {
				this.limit = val;
				this.dataSelectKm()
			},
			handleCurrentChangeList(val) {
				this.page = val;
				this.dataSelectKm()
			},

			handleSizeChangeY(val) {
				this.size1 = val;
				this.dataSelectYhq()
			},
			handleCurrentChangeY(val) {
				this.page1 = val;
				this.dataSelectYhq()
			},
			handleClick(tab, event) {
				this.limit = 10
				this.page = 1
				this.couponCardTypeId = ''
				if (tab._props.label == '赠送记录') {
					this.classify = 10
					this.dataSelect()
				}
				if (tab._props.label == '接受记录') {
					this.classify = 20
					this.dataSelect()
				}

			},
			// 刷新
			refresh() {
				this.page = 1
				this.dataSelect()
			},
			// 添加、修改
			amend(row) {
				this.dialogFormVisible = true
				
				if (row) {
					this.titles = '修改'
					this.form.typeId = row.typeId
					this.form.money = row.money
					this.form.remarks = row.remarks
					this.form.validDay = row.validDay
					if(row.couponIds){
						this.couponIds = row.couponIds.split(',')
					}else{
						this.couponIds = []
					}
					if(row.selfCouponIssueList){
						this.couponIdList = row.selfCouponIssueList
					}else{
						this.couponIdList = []
					}
					this.form.sdkClassify = row.sdkClassify
					
				} else {
					this.titles = '添加'
					this.form.typeId = ''
					this.form.money = ''
					this.form.remarks = ''
					this.form.validDay = ''
					this.couponIds = []
					this.couponIdList = []
					this.form.sdkClassify = 1
				}

			},
			handleAvatarSuccess2(file, fileList) {
				this.form.couponIssueImage = file.data
			},
			// 修改
			amendNoticeTo() {
				if (this.form.remarks === '') {
					this.$notify({
						title: '提示',
						type: 'error',
						duration: 1800,
						message: '请输入名称',
						type: 'warning'
					});
					return
				}
				
				
				if(this.form.sdkClassify==1){
					if (this.form.money === '' || this.form.money == null) {
						this.$notify({
							title: '提示',
							type: 'error',
							duration: 1800,
							message: '请输入金额',
							type: 'warning'
						});
						return
					}
					if (this.form.validDay === ''||this.form.validDay ==null) {
						this.$notify({
							title: '提示',
							type: 'error',
							duration: 1800,
							message: '请输入有效天数',
							type: 'warning'
						});
						return
					}
				}else{
					if(this.couponIdList.length==0){
						this.$notify({
							title: '提示',
							type: 'error',
							duration: 1800,
							message: '请选择优惠券',
							type: 'warning'
						});
						return
					}
				}
				

				if (this.titles == '添加') {
					var url = 'couponCard/insertCouponCardType'
				} else {
					var url = 'couponCard/updateCouponCardType'
				}

				this.$http({
					url: this.$http.adornUrl('admin/sdkType/saveSdkType'),
					method: 'post',
					// data: this.$http.adornData({
					params: this.$http.adornParams({
						'typeId': this.form.typeId,
						'money': this.form.money,
						'remarks': this.form.remarks,
						'validDay': this.form.validDay,
						'couponIds':this.couponIds.toString(),
						'sdkClassify':this.form.sdkClassify,
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
								this.dataSelect()
							}
						})
						this.dialogFormVisible = false
					} else {
						this.$message({
							message: data.msg,
							type: 'success',
							duration: 1500,
							onClose: () => {}
						})
					}

				})
			},
			//删除
			deleteStair(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/sdkType/deleteSdkType'),
						method: 'get',
						params: this.$http.adornParams({
							'typeId': row.typeId
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
								type: 'error',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
						}

					})
				}).catch(() => {})
			},
			refreshList() {
				this.page = 1
				this.dataSelectKm()
			},
			// 重置
			cleans() {
				this.endTime = ''
				this.startTime = ''
				this.couponIssueNameT = ''
				this.sdkContent = ''
				this.status = ''
				this.userName = ''
				this.sysUserNameT = ''
				this.page = 1
				this.dataSelectKm()
			},
			// 获取卡密分类数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('Details/queryUserMoneyDetails'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'classify': this.classify,
						'time': this.info.stockDate,
						'flag': this.flag,
						
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData
					if(this.page>1){
						if (data.data.records.length == 0) {
							this.page = this.page - 1
							this.dataSelect()
						}
					}
					
				})
			},

			
			// 统计金额
			dataSelectJe() {
				this.tableDataLoadingJe = true
				this.$http({
					url: this.$http.adornUrl('Details/selectGiveTakeMoneyCount'),
					method: 'get',
					params: this.$http.adornParams({
						'time': this.info.stockDate,
						'flag': this.flag,
						
					})
				}).then(({
					data
				}) => {
					this.tableDataLoadingJe = false
					let returnData = data.data;
					this.moneyStat = returnData
			
				})
			},
			orderfenxi(){
				this.dataSelectJe()
				this.dataSelect()
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
			this.dataSelectJe()
			this.dataSelect()
		}
	};
</script>

<style scoped>
.transfer-gift-page__filter-select {
	width: 150px;
}

.transfer-gift-page__filter-date {
	width: 200px;
}

.transfer-gift-page__stat-filter {
	display: flex;
	flex-wrap: wrap;
	align-items: center;
	gap: 12px;
}

.transfer-gift-page__kpi-grid {
	display: grid;
	grid-template-columns: repeat(2, 1fr);
	gap: 12px;
	margin-top: 12px;
	background: #fff;
	border: 1px solid #e8eaed;
	border-radius: 8px;
	padding: 16px;
	box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
}

.transfer-gift-page__kpi-card {
	padding: 16px;
	background: #f7f8fa;
	border: 1px solid #e8eaed;
	border-radius: 6px;
}

.transfer-gift-page__kpi-label {
	font-size: 13px;
	color: #909399;
	margin-bottom: 8px;
}

.transfer-gift-page__kpi-value {
	font-size: 14px;
	color: #303133;
}

.transfer-gift-page__kpi-value span {
	font-size: 22px;
	font-weight: 600;
	color: #5cc477;
	margin-right: 4px;
}

.transfer-gift-page__tabs {
	margin-top: 12px;
	margin-bottom: 0;
}

.transfer-gift-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.transfer-gift-page__tabs >>> .el-tabs__item.is-active {
	color: #5cc477;
}

.transfer-gift-page__tabs >>> .el-tabs__active-bar {
	background-color: #5cc477;
}

.transfer-gift-page__tabs .nf-table-card {
	margin-bottom: 0;
}

.transfer-gift-page__link {
	color: #5cc477;
	cursor: pointer;
}

.transfer-gift-page__link:hover {
	color: #3fae5c;
}

.transfer-gift-page__empty {
	color: #f56c6c;
}

.transfer-gift-page__amount--minus {
	color: #f56c6c;
}

.transfer-gift-page__amount--plus {
	color: #006600;
}

.transfer-gift-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

@media (max-width: 768px) {
	.transfer-gift-page__kpi-grid {
		grid-template-columns: 1fr;
	}
}
</style>
