<template>
	<div class="nf-page member-details-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">会员管理</span>
			<h2 class="nf-page-hero__title">会员配置</h2>
			<p class="nf-page-hero__desc">管理会员套餐类型、名称与现价，支持添加、编辑与删除会员配置项。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">
				<i class="el-icon-setting nf-filter-card__head-icon"></i>
				<span>快捷操作</span>
			</div>
			<el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
				<el-form-item class="member-details-page__filter-actions">
					<el-button type="primary" icon="el-icon-plus"
						:disabled="!isAuth('memberDetails:add')" @click="addNotice">添加会员
					</el-button>
				</el-form-item>
			</el-form>
		</div>

		<el-tabs value="first" class="member-details-page__tabs">
			<el-tab-pane label="会员列表" name="first">
				<div class="nf-table-card">
					<el-table v-loading="tableDataLoading" :data="configData.list">
						<el-table-column prop="id" label="编号" width="80">
						</el-table-column>
						<el-table-column prop="vipName" label="会员名称">
						</el-table-column>
						<el-table-column prop="money" label="现价">
						</el-table-column>
						<!-- <el-table-column prop="oldMoney" label="原价">
						</el-table-column> -->
						<!-- <el-table-column prop="award" label="邀请赏金" width="150">
						</el-table-column> -->
						<el-table-column prop="vipNameType" label="会员等级" width="150">
							<template slot-scope="scope">
								<span class="member-details-page__tag" v-if="scope.row.vipNameType == 0">月</span>
								<span class="member-details-page__tag" v-if="scope.row.vipNameType == 1">季</span>
								<span class="member-details-page__tag" v-if="scope.row.vipNameType == 2">年</span>
							</template>
						</el-table-column>
						<!-- <el-table-column prop="type" label="赠送优惠券">
							<template slot-scope="scope">
								<div v-if="scope.row.couponList">
									<div v-for="(item,index) in scope.row.couponList" :key="index" >
										<el-tag style="margin: 3px;" v-if="item">{{item.couponName}}</el-tag>
									</div>
									
								</div>
							</template>
						</el-table-column> -->
						<el-table-column label="操作" fixed='right' width="200">
							<template slot-scope="scope">
								<el-button size="mini" type="primary" :disabled="!isAuth('memberDetails:update')"
									@click="amendone(scope.$index, scope.row)">编辑
								</el-button>
								<el-button size="mini" type="danger" :disabled="!isAuth('memberDetails:delete')"
									@click="deletes(scope.row)">删除
								</el-button>
							</template>
						</el-table-column>
					</el-table>
					<div class="member-details-page__pagination">
						<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
							:page-sizes="[5, 10, 15, 20]" :page-size="size" :current-page="page"
							layout="total,sizes, prev, pager, next,jumper" :total="configData.totalCount">
						</el-pagination>
					</div>
				</div>
			</el-tab-pane>
		</el-tabs>

		<el-dialog :title="titles" :visible.sync="dialogFormVisible" center>
			<div style="margin-bottom: 20px;">
				<span style="width: 200px;display: inline-block;text-align: right;">会员类型：</span>
				<el-radio-group v-model="vipNameType">
					<el-radio :label="0">月会员</el-radio>
					<el-radio :label="1">季会员</el-radio>
					<el-radio :label="2">年会员</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 20px;">
				<span style="width: 200px;display: inline-block;text-align: right;">会员名称：</span>
				<el-input style="width:50%;" v-model="vipName" placeholder="请输入会员名称"></el-input>
			</div>
			
			<!-- <div style="margin-bottom: 20px;">
				<span style="width: 200px;display: inline-block;text-align: right;">原价：</span>
				<el-input style="width:50%;" v-model="oldMoney" type="number" placeholder="请输入原价" min="0"
					onkeyup="value=value.replace(/^\D*(\d*(?:\.\d{0,2})?).*$/g, '$1')"></el-input>
			</div> -->
			<div style="margin-bottom: 20px;">
				<span style="width: 200px;display: inline-block;text-align: right;">现价：</span>
				<el-input style="width:50%;" v-model="money" type="number" placeholder="请输入现价" min="0"
					onkeyup="value=value.replace(/^\D*(\d*(?:\.\d{0,2})?).*$/g, '$1')"></el-input>
			</div>
			<!-- <div style="margin-bottom: 20px;">
				<span style="width: 200px;display: inline-block;text-align: right;">邀请赏金：</span>
				<el-input style="width:50%;" v-model="award" type="number" placeholder="请输入邀请赏金" min="0"
					onkeyup="value=value.replace(/^\D*(\d*(?:\.\d{0,2})?).*$/g, '$1')"></el-input>
			</div>
			<div style="margin: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">选择优惠券：</span>
				<div :key="j" v-for="(tag,j) in couponIdList" style="display: inline-block;">
					<el-tag v-if="tag" closable :disable-transitions="false"
						@close="couponIdClose(tag,j)">
						{{tag.couponName}}
					</el-tag>
				</div>
				
				<el-button class="button-new-tag" size="small" @click="couponIdBtn(couponIds)">添加</el-button>
			</div> -->


			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="releasNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
		
		<!-- 优惠券列表 -->
		<el-dialog title="优惠券列表" :visible.sync="dialogFormVisible4" center>
			<el-button style="margin-left:15px;" :disabled="couponIds1.length <= 0 " size="mini" type="primary"
				icon="document" @click="closes2()">确定</el-button>
			<el-table @selection-change="changeFun" v-loading="tableDataLoading" :data="tableDataYhq.records">
				<el-table-column type="selection" fixed>
				</el-table-column>
				<el-table-column prop="couponName" label="优惠券名称" width="150">
				</el-table-column>
				<el-table-column prop="type" label="优惠券类型" width="100">
					<template slot-scope="scope">
						<span v-if="scope.row.type==1">通用券</span>
						<span class="member-details-page__tag" v-if="scope.row.type==2">商品券</span>
					</template>
				</el-table-column>
				<el-table-column prop="money" label="优惠券抵扣金额" width="100">
				</el-table-column>
				<el-table-column prop="buyMoney" label="优惠券购买金额" width="100">
				</el-table-column>
		
				<el-table-column prop="minMoney" label="优惠券最低消费" width="150">
				</el-table-column>
				<el-table-column prop="maxReceive" label="最多领取/购买数量" width="150">
				</el-table-column>
		
				<el-table-column prop="failureTime" label="优惠券有效期限" width="150">
				</el-table-column>
				<el-table-column prop="sort" label="排序">
				</el-table-column>
				<el-table-column label="状态">
					<template slot-scope="scope">
						<span class="member-details-page__tag" v-if="scope.row.status == 1">开启</span>
						<span v-if="scope.row.status == 2">关闭</span>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="150">
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChangeY" @current-change="handleCurrentChangeY"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="size1" :current-page="page1"
					layout="total,sizes, prev, pager, next" :total="tableDataYhq.total">
				</el-pagination>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				size: 10,
				page: 1,
				size1: 10,
				page1: 1,
				totalnum: 0,
				totalnum2: 0,
				ids: '',
				content: '',
				activeName: 'first',
				tableDataLoading: false,
				dialogFormVisible: false,
				dialogFormVisible1: false,
				dialogFormVisible2: false,
				dialogFormVisible3: false,
				memberGrade: '',
				vipName: '',
				money: '',
				oldMoney:'',
				award: '',
				vipNameType: 0,
				formLabelWidth: '200px',
				checkBoxData: [], //多选框选择的值
				tableData: [],
				configData: {},
				form: {
					id: '',
					vipName: '',
					money: 0.00,
					award: 0.00,
					vipNameType: 0,
				},
				form2: {
					picture: '',
				},
				// 赠送优惠券
				tableDataLoading1: false,
				dialogFormVisible0: false,
				dialogFormVisible5: false,
				dialogFormVisible4: false,
				couponIds: [],
				couponIdList: [],
				titles:'',
				couponIds1: [],
				couponIdList1: [],
				id:'',
				tableDataYhq:{},
			}
		},
		methods: {
			changeInput() {
				var pattern = /^[1-9][1-9]*$/ // 正整数的正则表达式
				// 不符合正整数时
				if (!pattern.test(this.memberGrade)) {
					// input 框绑定的内容为空
					this.memberGrade = ''
				}
			},
			deletes(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('vipDetails/deleteVipDetails'),
						method: 'post',
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
			},
			handleAvatarSuccess1(file) {
				this.memberIcon = file.data;
			},
			handleAvatarSuccess2(file) {
				this.memberImg = file.data;
			},
			handleAvatarSuccess3(file) {
				this.form.memberIcon = file.data;
			},
			handleAvatarSuccess4(file) {
				this.form.memberImg = file.data;
			},
			handleSizeChange(val) {
				this.size = val
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val
				this.dataSelect()
			},
			handleSizeChangeY(val) {
				this.size1 = val;
				this.dataSelectY()
			},
			handleCurrentChangeY(val) {
				this.page1 = val;
				this.dataSelectY()
			},
			// 图片预览
			lookMember(rows) {
				this.dialogFormVisible2 = true
				this.form2.picture = rows.picture
			},
			selectTrigger() {
				this.page = 1
				this.size = 5
				this.dataSelect()
			},
			addNotice() {
				this.titles = '添加'
				this.id = ''
				this.vipName = ''
				this.money = ''
				this.oldMoney = ''
				this.award = ''
				this.vipNameType = 0
				this.couponIds = []
				this.couponIdList = []
				this.dialogFormVisible = true
			},
			//添加会员配置
			releasNoticeTo() {

				if (this.vipName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入会员名称',
						type: 'warning'
					});
					return
				}
				
				// if (this.oldMoney === '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请输入原价',
				// 		type: 'warning'
				// 	});
				// 	return
				// }
				if (this.money === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入现价',
						type: 'warning'
					});
					return
				}
				// if (this.award == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请输入邀请赏金',
				// 		type: 'warning'
				// 	});
				// 	return
				// }
				// if (this.couponIds.length == 0) {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请选择优惠券',
				// 		type: 'warning'
				// 	});
				// 	return
				// }
				if(this.titles == '添加'){
					var url = 'vipDetails/insertVipDetails'
				}else{
					var url = 'vipDetails/updateVipDetails'
				}
				this.$http({
					url: this.$http.adornUrl(url),
					method: 'post',
					data: this.$http.adornData({
						'id': this.id,
						'vipName': this.vipName,
						'money': this.money,
						'oldMoney':this.oldMoney,
						'award': this.award,
						'vipNameType': this.vipNameType,
						'couponIds': this.couponIds.toString(),
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
							
							this.dataSelect()
						}
					})
				})
			},
			// 修改配置弹框
			amendone(index, rows) {
				this.titles = '修改'
				this.dialogFormVisible = true
				this.id = rows.id
				this.vipName = rows.vipName
				this.money = rows.money
				this.oldMoney = rows.oldMoney
				this.award = rows.award
				this.vipNameType = rows.vipNameType
				this.couponIds = rows.couponIds.split(',')
				var rowsValue=JSON.parse(JSON.stringify(rows));
				this.couponIdList = rowsValue.couponList

			},
			// 修改配置
			amendoneNoticeTo() {

				if (this.form.vipName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入会员名称',
						type: 'warning'
					});
					return
				}
				if (this.form.money == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入会员价格',
						type: 'warning'
					});
					return
				}

				if (this.form.award == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入邀请赏金',
						type: 'warning'
					});
					return
				}

				this.$http({
					url: this.$http.adornUrl('vipDetails/updateVipDetails'),
					method: 'post',
					data: this.$http.adornData({
						'id': this.form.id,
						'vipName': this.form.vipName,
						'money': this.form.money,
						'award': this.form.award,
						'vipNameType': this.form.vipNameType,
					})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						this.dialogFormVisible1 = false
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
				let page = this.page
				this.$http({
					url: this.$http.adornUrl('vipDetails/selectVipDetailsList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': page,
						'limit': this.size,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.configData = returnData
					this.totalnum2 = returnData.totalElements
				})
			},
			// 多选券
			changeFun(val) {
				console.log('多选', val)
				this.couponIds1 = []
				this.couponIdList1 = []
				for (var i in val) {
					this.couponIds1.push(val[i].couponId)
					this.couponIdList1.push(val[i])
				}
				
			},
			// 打开优惠券列表
			couponIdBtn(rows) {
				this.page1 = 1
				this.dataSelectY()
				this.dialogFormVisible4 = true
			},
			// 批量开启
			closes2(id) {
				this.couponIds = [...this.couponIds,...this.couponIds1]
				this.couponIdList = [...this.couponIdList,...this.couponIdList1]
				this.dialogFormVisible4 = false
			},
			// 删除优惠券
			couponIdClose(ietm,index) {
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
			//获取优惠券数据
			dataSelectY() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/coupon/getCouponPageList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page1,
						'limit': this.size1
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableDataYhq = returnData
				})
			},
		},
		mounted() {
			this.dataSelect()
		}
	}
</script>

<style scoped>
.member-details-page__filter-actions {
	margin-bottom: 0;
}

.member-details-page__tabs {
	margin-bottom: 0;
}

.member-details-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.member-details-page__tabs .nf-table-card {
	margin-bottom: 0;
}

.member-details-page__tabs >>> .el-tabs__item.is-active {
	color: #5cc477;
}

.member-details-page__tabs >>> .el-tabs__active-bar {
	background-color: #5cc477;
}

.member-details-page__tag {
	color: #5cc477;
	cursor: pointer;
}

.member-details-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

.nf-table-card .el-button + .el-button {
	margin-left: 8px;
}
</style>
