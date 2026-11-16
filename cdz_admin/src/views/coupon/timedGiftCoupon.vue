<template>
	<div class="nf-page timed-gift-coupon-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">优惠券活动</span>
			<h2 class="nf-page-hero__title">优惠券活动</h2>
			<p class="nf-page-hero__desc">管理定时赠送优惠券活动，支持按活动名称筛选，可添加、编辑与删除活动，配置优惠券、活动时间及触发频率。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<el-tabs v-model="activeName" @tab-click="handleClick" class="timed-gift-coupon-page__tabs">
			<el-tab-pane label="优惠券活动列表" name="first">
				<div class="nf-filter-card">
					<div class="nf-filter-card__head">
						<i class="el-icon-search nf-filter-card__head-icon"></i>
						<span>筛选条件</span>
					</div>
					<el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
						<el-form-item label="活动名称">
							<el-input
								class="timed-gift-coupon-page__filter-input"
								@keydown.enter.native="refresh"
								clearable
								placeholder="请输入活动名称"
								v-model="activityNameT">
							</el-input>
						</el-form-item>
						<el-form-item class="timed-gift-coupon-page__filter-actions">
							<el-button type="primary" icon="el-icon-refresh" @click="refresh">刷新</el-button>
							<el-button type="primary" icon="el-icon-plus" :disabled="!isAuth('timedGiftCoupon:add')" @click="amend()">添加</el-button>
						</el-form-item>
					</el-form>
				</div>

				<div class="nf-table-card">
					<el-table v-loading="tableDataLoading" :data="tableData.records">
						<el-table-column prop="couponActivityId" label="编号" width="80">
						</el-table-column>
						<el-table-column prop="couponIds" label="优惠券" width="200">
							<template slot-scope="scope">
								<div v-if="scope.row.selfCouponIssueList&&scope.row.selfCouponIssueList.length>0">
									<div v-for="(item,index) in scope.row.selfCouponIssueList" :key="index">
										<span v-if="item!=null">{{item.couponName}}</span>
									</div>
								</div>
								<div v-else>-</div>
							</template>
						</el-table-column>
						<el-table-column prop="activityName" label="活动名称">
						</el-table-column>
						<el-table-column prop="activityTime" label="活动开始时间">
						</el-table-column>
						<el-table-column prop="endActivityTime" label="活动结束时间">
						</el-table-column>
						<el-table-column prop="privilegeType" label="触发频率">
							<template slot-scope="scope">
								<span v-if="scope.row.privilegeType==1">每天</span>
								<span v-if="scope.row.privilegeType==2">每周</span>
								<span v-if="scope.row.privilegeType==3">每月</span>
								<span v-if="scope.row.privilegeType==4">每季度</span>
								<span v-if="scope.row.privilegeType==5">每年</span>
							</template>
						</el-table-column>
						<el-table-column prop="createTime" label="创建时间">
						</el-table-column>
						<el-table-column label="操作" width="200">
							<template slot-scope="scope">
								<el-button size="mini" type="primary" :disabled="!isAuth('timedGiftCoupon:update')"
									@click="amend(scope.row)">编辑
								</el-button>
								<el-button size="mini" type="danger" :disabled="!isAuth('timedGiftCoupon:delete')"
									@click="deleteStair(scope.row)">删除
								</el-button>
							</template>
						</el-table-column>
					</el-table>
					<div class="timed-gift-coupon-page__pagination">
						<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
							:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
							layout="total,sizes, prev, pager, next" :total="tableData.total">
						</el-pagination>
					</div>
				</div>
			</el-tab-pane>
		</el-tabs>

		<!-- 修改 -->
		<el-dialog :title="titles" :visible.sync="dialogFormVisible" center>
			<el-form :model="form">
				<el-form-item label="优惠券：" :label-width="formLabelWidth">
					<el-tag :key="j" v-for="(tag,j) in couponIdList" closable :disable-transitions="false"
						@close="couponIdClose1(tag,j)">
						{{tag.couponName}}
					</el-tag>
					<el-button class="button-new-tag" size="small" @click="couponIdBtn1()">添加</el-button>
				</el-form-item>
				<el-form-item label="活动名称：" :label-width="formLabelWidth">
					<el-input v-model="form.activityName" style="width:65%;" placeholder="请输入活动名称"></el-input>
				</el-form-item>
				<el-form-item label="活动开始时间：" :label-width="formLabelWidth">
					<el-date-picker style="width:65%;" v-model="form.activityTime" align="right" type="datetime"
						format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择活动开始时间">
					</el-date-picker>
				</el-form-item>
				<el-form-item label="活动结束时间：" :label-width="formLabelWidth">
					<el-date-picker style="width:65%;" v-model="form.endActivityTime" align="right" type="datetime"
						format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择活动结束时间">
					</el-date-picker>
				</el-form-item>
				<el-form-item label="触发频率：" :label-width="formLabelWidth">
					<el-radio-group v-model="form.privilegeType">
						<el-radio :label="1">每天</el-radio>
						<el-radio :label="2">每周</el-radio>
						<el-radio :label="3">每月</el-radio>
						<el-radio :label="4">每季度</el-radio>
						<el-radio :label="5">每年</el-radio>
					</el-radio-group>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="amendNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>

		<!-- 优惠券列表 -->
		<el-dialog title="优惠券列表" :visible.sync="dialogFormVisible5" center width="70%">
			<div style="display: inline-block;margin-top: 5px;">
				<span>类型：</span>
				<el-select v-model="typeY" style="width:150px;margin-left: 10px;" @change="select1()">
					<el-option v-for="item in typeList" :key="item.value" :label="item.title" :value="item.value">
					</el-option>
				</el-select>&nbsp;&nbsp;&nbsp;
			</div>
			<div style="display: inline-block;margin-top: 5px;">
				<span>状态：</span>
				<el-select v-model="statusY" style="width:150px;margin-left: 10px;" @change="select1()">
					<el-option v-for="item in memberList" :key="item.value" :label="item.title" :value="item.value">
					</el-option>
				</el-select>&nbsp;&nbsp;&nbsp;
			</div>
			<div style="display: inline-block;margin-top: 5px;">
				<span>优惠券名称：</span>
				<el-input style="width: 150px;" @keydown.enter.native="select1" clearable placeholder="请输入优惠券名称"
					v-model="couponNameY">
				</el-input>&nbsp;&nbsp;&nbsp;&nbsp;
			</div>
			<div style="display: inline-block;margin-top: 5px;">
				<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="select1">查询
				</el-button>
				<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="cleans1">重置
				</el-button>
			</div>
			<el-button style="margin-left:15px;" :disabled="xzList.length <= 0 " size="mini" type="primary"
				icon="document" @click="closes1()">确定</el-button>
			<el-table v-loading="tableDataLoadingY" @selection-change="changeFun1" :data="tableDataYhq.content">
				<el-table-column type="selection" fixed>
				</el-table-column>
				<el-table-column prop="couponIssueId" label="编号" width="80" fixed="left">
				</el-table-column>
				<el-table-column prop="type" label="类型" fixed="left">
					<template slot-scope="scope">
						<span v-if="scope.row.type == 1">团购券</span>
						<span v-if="scope.row.type == 2">积分兑换券</span>
						<span v-if="scope.row.type == 3">新人券</span>
					</template>
				</el-table-column>
				<el-table-column prop="couponName" label="优惠券名称" width="150" fixed="left">
				</el-table-column>
				<el-table-column prop="issueNumber" label="发布数量">
				</el-table-column>
				<el-table-column prop="remainNumber" label="剩余数量">
				</el-table-column>
				<el-table-column prop="hour" label="使用时长">
				</el-table-column>
				<el-table-column prop="validDay" label="有效期限(天)">
				</el-table-column>
				<el-table-column prop="teamRule" label="使用规则" width="250">
					<template slot-scope="scope">
						<el-popover placement="top-start" title="" trigger="hover">
							<div style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;" slot="reference">
								{{scope.row.teamRule}}
							</div>
							<div style="width: 500px;height: auto;">{{scope.row.teamRule}}</div>
						</el-popover>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange2" @current-change="handleCurrentChange2"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="size1" :current-page="page1"
					layout="total,sizes, prev, pager, next,jumper" :total="tableDataYhq.totalElements">
				</el-pagination>
			</div>
		</el-dialog>
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
					couponActivityId: '',
					activityName: '',
					activityTime: '',
					endActivityTime:'',//活动结束时间
					couponIds: [],
					privilegeType:'',//触发频率 1每天 2每周 3每月 4每季度 5每年
				},
				
				formLabelWidth: '200px',
				tableDataLoading: false,
				dialogFormVisible: false,
				tableData: {},
				activeName: 'first',
				couponIds: [],
				couponIdList: [],
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
				typeList: [{
						value: '',
						title: '全部'
					},
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
				couponCardTypeId: '',
				activityNameT:'',
				
				
				
			}
		},
		methods: {
			
			handleSizeChange(val) {
				this.limit = val;
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.dataSelect()
			},
			
			
			handleClick(tab, event) {
				this.limit = 10
				this.page = 1
				if (tab._props.label == '优惠券活动列表') {
					
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
					this.form.couponActivityId = row.couponActivityId
					
					this.form.activityName = row.activityName
					this.form.activityTime = row.activityTime
					this.form.endActivityTime = row.endActivityTime
					this.form.privilegeType = row.privilegeType
					
					
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
					
				} else {
					this.titles = '添加'
					this.form.couponActivityId = ''
					this.form.activityName = ''
					this.form.activityTime = ''
					this.couponIds = []
					this.couponIdList = []
					this.form.endActivityTime = ''
					this.form.privilegeType = ''
					
				}

			},
			
			// 修改
			amendNoticeTo() {
				if (this.couponIds.length == 0 || this.couponIds == null) {
					this.$message({
						title: '提示',
						type: 'error',
						duration: 1800,
						message: '请选择优惠券',
						type: 'warning'
					});
					return
				}
				if (this.form.activityName == '') {
					this.$message({
						title: '提示',
						type: 'error',
						duration: 1800,
						message: '请输入活动名称',
						type: 'warning'
					});
					return
				}
				
				if (this.form.activityTime == '') {
					this.$message({
						title: '提示',
						type: 'error',
						duration: 1800,
						message: '请选择活动开始时间',
						type: 'warning'
					});
					return
				}
				if (this.form.endActivityTime == '') {
					this.$message({
						title: '提示',
						type: 'error',
						duration: 1800,
						message: '请选择活动结束时间',
						type: 'warning'
					});
					return
				}
				if (this.form.privilegeType == '') {
					this.$message({
						title: '提示',
						type: 'error',
						duration: 1800,
						message: '请选择触发频率',
						type: 'warning'
					});
					return
				}



				if (this.titles == '添加') {
					var url = 'couponActivity/save'
				} else {
					var url = 'couponActivity/update'
				}

				this.$http({
					url: this.$http.adornUrl(url),
					method: 'post',
					data: this.$http.adornData({
					// params: this.$http.adornParams({
						'couponActivityId': this.form.couponActivityId,
						'activityName': this.form.activityName,
						'activityTime': this.form.activityTime,
						'couponIds':this.couponIds.toString(),
						'endActivityTime':this.form.endActivityTime,
						'privilegeType':this.form.privilegeType,
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
							type: 'warning',
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
						url: this.$http.adornUrl('couponActivity/delete'),
						method: 'post',
						params: this.$http.adornParams({
							'couponActivityId': row.couponActivityId
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
			// 获取卡密分类数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('couponActivity/selectCouponActivityList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'activityName': this.activityNameT
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
			// 详情跳转
			updates(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
					}
				})
			},
			
			// 打开优惠券列表
			couponIdBtn1(rows) {
				// this.couponIds = []
				// this.couponIdList = []
				this.page1 = 1
				this.typeY = 1
				this.statusY = ''
				this.couponNameY = ''
				this.dataSelectYhq()

				this.dialogFormVisible5 = true
			},
			// 获取优惠券数据列表
			dataSelectYhq() {
				this.tableDataLoadingY = true
				this.$http({   
					url: this.$http.adornUrl('selfCouponIssue/list'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page1 - 1,
						'size': this.size1,
						'type': this.typeY,
						'tearoomName': '',
						'couponName': this.couponNameY,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoadingY = false
					let returnData = data.data
					this.tableDataYhq = returnData
				})
			},
			handleSizeChange2(val) {
				this.size1 = val;
				this.dataSelectYhq()
			},
			handleCurrentChange2(val) {
				this.page1 = val;
				this.dataSelectYhq()
			},
			// 查询
			select1() {
				this.page1 = 1
				this.dataSelectYhq()
			},
			// 重置
			cleans1() {
				this.typeY = 1
				this.statusY = ''
				this.couponNameY = ''
				this.dataSelectYhq()
			},
			// 删除优惠券
			couponIdClose1(ietm, index) {
				console.log('index', index, 'ietm', ietm, this.couponIds.length)
				if (this.couponIds.length < 2) {
					this.$message({
						title: '提示',
						type: 'error',
						duration: 1800,
						message: '最少需要选中一张优惠券',
						type: 'warning'
					});
					return
				} else {
					this.couponIds.splice(index, 1)
					this.couponIdList.splice(index, 1)
				}
			},
			// 多选优惠券
			changeFun1(val) {
				console.log('多选', val)
				this.xzList = val

			},
			closes1() {
				console.log('-----1',this.couponIdList)
				for (var i in this.xzList) {
					this.couponIds.push(this.xzList[i].couponIssueId)
					this.couponIdList.push(this.xzList[i])
				}
				console.log('-----2',this.couponIdList)
				this.dialogFormVisible5 = false
			},

		},
		mounted() {
			this.dataSelect()
		}
	};
</script>

<style scoped>
.timed-gift-coupon-page__filter-input {
	width: 200px;
}

.timed-gift-coupon-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.timed-gift-coupon-page__tabs {
	margin-bottom: 0;
}

.timed-gift-coupon-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.timed-gift-coupon-page__tabs .nf-table-card {
	margin-bottom: 0;
}

.timed-gift-coupon-page__tabs >>> .el-tabs__item.is-active {
	color: #5cc477;
}

.timed-gift-coupon-page__tabs >>> .el-tabs__active-bar {
	background-color: #5cc477;
}

.timed-gift-coupon-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

.nf-table-card .el-button + .el-button {
	margin-left: 8px;
}
</style>
