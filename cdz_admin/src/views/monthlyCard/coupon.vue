<template>
	<div class="nf-page coupon-km-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">卡密管理</span>
			<h2 class="nf-page-hero__title">卡密管理</h2>
			<p class="nf-page-hero__desc">管理卡密分类与卡密列表，支持添加分类、生成卡密、按券包名称/状态筛选，可查询、重置与编辑删除。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">
				<i class="el-icon-search nf-filter-card__head-icon"></i>
				<span>{{ activeName === 'first' ? '快捷操作' : '筛选条件' }}</span>
			</div>
			<div v-if="activeName === 'first'" class="nf-filter-card__body coupon-km-page__filter-actions-only">
				<el-button type="primary" icon="el-icon-refresh" @click="refresh">刷新</el-button>
				<el-button type="primary" icon="el-icon-plus" :disabled="!isAuth('coupon:add')" @click="amend()">添加</el-button>
			</div>
			<el-form v-else :inline="true" class="nf-filter-card__body" @submit.native.prevent>
				<el-form-item label="券包名称">
					<el-input
						class="coupon-km-page__filter-input"
						@keydown.enter.native="refreshList"
						clearable
						placeholder="请输入券包名称"
						v-model="couponIssueNameT">
					</el-input>
				</el-form-item>
				<el-form-item label="状态">
					<el-select
						class="coupon-km-page__filter-select"
						v-model="status"
						placeholder="请选择状态"
						@change="refreshList(status)">
						<el-option v-for="item in statesnum" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item class="coupon-km-page__filter-actions">
					<el-button type="primary" icon="el-icon-search" @click="refreshList">查询</el-button>
					<el-button type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
					<el-button type="primary" icon="el-icon-plus" :disabled="!isAuth('couponissue:add')" @click="amend()">添加</el-button>
				</el-form-item>
			</el-form>
		</div>

		<el-tabs v-model="activeName" @tab-click="handleClick" class="coupon-km-page__tabs">
			<el-tab-pane label="卡密分类" name="first">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="tableData.list">
					<el-table-column prop="couponCardTypeId" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="couponCardName" label="分类名称">
					</el-table-column>
					<el-table-column label="状态">
						<template slot-scope="scope">
							<span class="coupon-km-page__status-open" v-if="scope.row.status == 1">开启</span>
							<span v-if="scope.row.status == 2">关闭</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间">
					</el-table-column>
					<el-table-column label="操作" width="200">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="listBtn(scope.row)" style="margin: 3px;">优惠券列表
							</el-button>
							<el-button size="mini" type="primary" :disabled="!isAuth('coupon:update')"
								@click="amend(scope.row)" style="margin: 3px;">编辑
							</el-button>
							<el-button :disabled="!isAuth('coupon:shengcheng')" size="mini" type="primary"
								icon="document" @click="shengCoupon(scope.row)" style="margin: 3px;">生成卡密</el-button>

							<el-button size="mini" type="danger" :disabled="!isAuth('coupon:delete')"
								@click="deleteStair(scope.row)" style="margin: 3px;">删除
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div class="coupon-km-page__pagination">
					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="tableData.totalCount">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>

			<el-tab-pane label="卡密列表" name="putong">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="tableData.list">
					<el-table-column prop="couponCardId" fixed="left" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="couponCardNo" label="卡密">
					</el-table-column>
					<el-table-column label="领取用户">
						<template slot-scope="scope">
							<span class="coupon-km-page__link" @click="updates(scope.row)"
								v-if="scope.row.user">{{scope.row.user.userName}}</span>
						</template>
					</el-table-column>
					<el-table-column label="状态">
						<template slot-scope="scope">
							<span class="coupon-km-page__status-open" v-if="scope.row.status == 1">待使用</span>
							<span v-if="scope.row.status == 2">已领取</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="150">
					</el-table-column>
				</el-table>
				<div class="coupon-km-page__pagination">
					<el-pagination @size-change="handleSizeChangeList" @current-change="handleCurrentChangeList"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="tableData.totalCount">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>
			<!-- 修改 -->
			<el-dialog :title="titles" :visible.sync="dialogFormVisible" center>
				<el-form :model="form">
					<el-form-item label="分类名称：" :label-width="formLabelWidth">
						<el-input v-model="form.couponCardName" style="width:65%;" placeholder="请输入分类名称"></el-input>
					</el-form-item>

					<el-form-item label="选择优惠券：" :label-width="formLabelWidth">
						<el-tag :key="tag" v-for="(tag,index) in couponIdList" closable :disable-transitions="false"
							@close="couponIdClose(tag,index)">
							{{tag}}
						</el-tag>
						<el-button class="button-new-tag" size="small" @click="couponIdBtn(couponIds)">添加</el-button>
					</el-form-item>
					<el-form-item label="状态：" :label-width="formLabelWidth">
						<el-radio-group v-model="form.status">
							<el-radio :label="1">开启</el-radio>
							<el-radio :label="2">关闭</el-radio>
						</el-radio-group>
					</el-form-item>
				</el-form>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible = false">取 消</el-button>
					<el-button type="primary" @click="amendNoticeTo()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 优惠券列表 -->
			<el-dialog title="优惠券列表" :visible.sync="dialogFormVisible4" center width="70%">

				<el-table v-loading="tableDataLoadingY" :data="tableDataYhq.list">
					<el-table-column prop="couponId" label="编号" width="80" fixed="left">
					</el-table-column>
					<el-table-column prop="couponName" label="优惠券名称" fixed="left">
					</el-table-column>
					<el-table-column prop="couponPicture" label="优惠券图片">
						<template slot-scope="scope">
							<img v-if="scope.row.couponPicture" :src="scope.row.couponPicture" alt="" width="40"
								height="40">
						</template>
					</el-table-column>
					<el-table-column prop="money" label="优惠券面值" width="100">
					</el-table-column>
					<el-table-column prop="minMoney" label="优惠券最低消费" width="150">
					</el-table-column>
					<el-table-column prop="endDate" label="优惠券有效期限(天)" width="150">
					</el-table-column>
					<el-table-column label="操作" width="150" fixed="right">
						<template slot-scope="scope">
							<el-button style="margin-left:15px;" size="mini" type="primary" icon="document"
								@click="changeFun(scope.row)">确定</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChangeY" @current-change="handleCurrentChangeY"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="size1" :current-page="page1"
						layout="total,sizes, prev, pager, next" :total="tableDataYhq.totalCount">
					</el-pagination>
				</div>
			</el-dialog>
			<!-- 优惠券列表 -->
			<el-dialog title="优惠券列表" :visible.sync="dialogFormVisible5" center width="70%">
				<el-table v-loading="tableDataLoadingY" :data="tableDataList">
					<el-table-column prop="couponId" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="couponName" label="优惠券名称" >
					</el-table-column>
					<el-table-column prop="couponPicture" label="优惠券图片">
						<template slot-scope="scope">
							<img v-if="scope.row.couponPicture" :src="scope.row.couponPicture" alt="" width="40"
								height="40">
						</template>
					</el-table-column>
					<el-table-column prop="money" label="优惠券面值">
					</el-table-column>
					<el-table-column prop="minMoney" label="优惠券最低消费">
					</el-table-column>
					<el-table-column prop="endDate" label="优惠券有效期限(天)">
					</el-table-column>
				</el-table>
			</el-dialog>
			<!-- 生成卡密 -->
			<el-dialog title="生成卡密" :visible.sync="dialogFormVisible3" center width="70%">
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">生成数量：</span>
					<el-input style="width:50%;" v-model="num" type="number" :min="0" placeholder="请输入生成数量">
					</el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible3 = false">取 消</el-button>
					<el-button type="primary" @click="couponNoticeTo()">确 定</el-button>
				</div>
			</el-dialog>
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
					couponIds: '',
					couponCardTypeId: '',
					couponCardName: '',
					status: '',
				},
				formLabelWidth: '200px',
				tableDataLoading: false,
				dialogFormVisible: false,
				tableData: {},
				activeName: 'first',
				classify: 1,
				couponIssueNameT: '',
				statusT: 0,
				couponIds: [],
				couponIdList: [],
				dialogFormVisible4: false,
				dialogFormVisible5: false,
				tableDataLoadingY: false,
				tableDataYhq: {},
				size1: 10,
				page1: 1,
				titles: '添加',
				tableDataList: [],
				dialogFormVisible3: false,
				num: 0,
				couponCardTypeId: '',
				status: 0,
				statesnum: [{
						label: '全部',
						value: 0
					},
					{
						label: '待使用',
						value: 1
					},
					{
						label: '已领取',
						value: 2
					}
				],

			}
		},
		methods: {
			// 多选
			changeFun(val) {
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
				if (tab._props.label == '卡密分类') {
					this.classify = 1
					this.dataSelect()
				}
				if (tab._props.label == '卡密列表') {
					this.dataSelectKm()
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
				this.couponIds = []
				this.couponIdList = []
				if (row) {
					this.titles = '修改'
					this.form.couponIds = row.couponIds
					this.form.couponCardTypeId = row.couponCardTypeId
					this.form.couponCardName = row.couponCardName
					this.form.status = row.status
					if (row.couponList.length > 0) {
						for (var i in row.couponList) {
							this.couponIds.push(row.couponList[i].couponId)
							this.couponIdList.push(row.couponList[i].couponName)
						}
					}
					
				} else {
					this.titles = '添加'
					this.form.couponId = ''
					this.form.couponCardTypeId = ''
					this.form.couponCardName = ''
					this.form.status = ''
					this.couponIdList = []
					this.couponIds = []
				}

			},
			handleAvatarSuccess2(file, fileList) {
				this.form.couponIssueImage = file.data
			},
			// 修改
			amendNoticeTo() {
				if (this.form.couponCardName ==='' ) {
					this.$message({
						title: '提示',
						type: 'error',
						duration: 1800,
						message: '请输入分类名称',
						type: 'warning'
					});
					return
				}
				if (this.couponIds.length ==0 ) {
					this.$message({
						title: '提示',
						type: 'error',
						duration: 1800,
						message: '最少需要选中一张优惠券',
						type: 'warning'
					});
					return
				}
				if (this.form.status ==='' ) {
					this.$message({
						title: '提示',
						type: 'error',
						duration: 1800,
						message: '请选择状态',
						type: 'warning'
					});
					return
				}
				
				if (this.titles == '添加') {
					var url = 'couponCard/insertCouponCardType'
				} else {
					var url = 'couponCard/updateCouponCardType'
				}

				this.$http({
					url: this.$http.adornUrl(url),
					method: 'post',
					data: this.$http.adornData({
						'couponIds': this.couponIds.toString(),
						'couponCardTypeId': this.form.couponCardTypeId,
						'couponCardName': this.form.couponCardName,
						'status': this.form.status,
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
						url: this.$http.adornUrl('couponCard/deleteCouponCardTypeById'),
						method: 'post',
						params: this.$http.adornParams({
							'couponCardTypeId': row.couponCardTypeId
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
				this.content = ''
				this.status = 0
				this.page = 1
				this.dataSelectKm()
			},
			// 获取卡密分类数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('couponCard/selectCouponCardTypeList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'couponTypeName': this.content,
						'status': 0
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData
					if (data.data.list.length == 0) {
						this.page = this.page - 1
						this.dataSelect()
					}
				})
			},
			// 打开优惠券列表
			couponIdBtn(rows) {
				this.dataSelectYhq()
				this.dialogFormVisible4 = true
			},
			//获取优惠券数据
			dataSelectYhq() {
				this.tableDataLoadingY = true
				this.$http({
					url: this.$http.adornUrl('admin/coupon/seleteAllCoupon'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page1,
						'limit': this.size1
					})
				}).then(({
					data
				}) => {
					this.tableDataLoadingY = false
					let returnData = data.data;
					this.tableDataYhq = returnData
				})
			},
			// 删除优惠券
			couponIdClose(ietm, index) {
				console.log('index', index, 'ietm', ietm, this.couponIdList.length)
				if (this.couponIdList.length < 2) {
					this.$message({
						title: '提示',
						type: 'error',
						duration: 1800,
						message: '最少需要选中一张优惠券',
						type: 'warning'
					});
					return
				} else {
					this.couponIdList.splice(index, 1)
					this.couponIds.splice(index, 1)
				}
			},
			// 批量开启
			closes2(id) {
				this.dialogFormVisible4 = false
			},
			// 多选券
			changeFun(row) {
				this.couponIds.push(row.couponId)
				this.couponIdList.push(row.couponName)
				this.dialogFormVisible4 = false
			},
			// 优惠券列表
			listBtn(row) {
				this.tableDataList = row.couponList
				this.dialogFormVisible5 = true
			},
			// 生成卡密弹框
			shengCoupon(row) {
				this.couponCardTypeId = row.couponCardTypeId
				this.dialogFormVisible3 = true
			},
			// 确认生成
			couponNoticeTo() {
				if (this.num == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入生成数量',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl('couponCard/insertCouponCard'),
					method: 'post',
					params: this.$http.adornParams({
						'couponCardTypeId': this.couponCardTypeId,
						'num': this.num
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
								this.dialogFormVisible3 = false
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
			},
			// 卡密列表
			dataSelectKm() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('couponCard/selectCouponCardList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'couponCardNo': this.content,
						'status': this.status,
						'couponCardTypeId': '',

					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData

				})
			},
			// 详情跳转
			updates(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
					}
				})
			},
		},
		mounted() {
			this.dataSelect()
		}
	};
</script>

<style scoped>
.coupon-km-page__filter-input {
	width: 200px;
}

.coupon-km-page__filter-select {
	width: 150px;
}

.coupon-km-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.coupon-km-page__filter-actions-only .el-button + .el-button {
	margin-left: 12px;
}

.coupon-km-page__tabs {
	margin-bottom: 0;
}

.coupon-km-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.coupon-km-page__tabs .nf-table-card {
	margin-bottom: 0;
}

.coupon-km-page__tabs >>> .el-tabs__item.is-active {
	color: #5cc477;
}

.coupon-km-page__tabs >>> .el-tabs__active-bar {
	background-color: #5cc477;
}

.coupon-km-page__link {
	color: #5cc477;
	cursor: pointer;
}

.coupon-km-page__status-open {
	color: #5cc477;
}

.coupon-km-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}
</style>
