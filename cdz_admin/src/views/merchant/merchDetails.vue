<template>
	<div class="nf-page merch-details-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">商家管理</span>
			<h2 class="nf-page-hero__title">审核详情</h2>
			<p class="nf-page-hero__desc">查看商家入驻申请的店铺、主体与经营者信息，支持返回列表与审核处理。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<div class="nf-table-card merch-details-page__section">
			<div class="merch-details-page__section-head">
				<i class="el-icon-shop merch-details-page__section-icon"></i>
				<span>店铺信息</span>
			</div>
			<div class="merch-details-page__grid">
				<div class="merch-details-page__row">
					<span class="merch-details-page__label">店铺名称</span>
					<span class="merch-details-page__value">{{ tableData.store_name }}</span>
				</div>
				<div class="merch-details-page__row">
					<span class="merch-details-page__label">店铺负责人</span>
					<span class="merch-details-page__value">{{ tableData.name }}</span>
				</div>
				<div class="merch-details-page__row">
					<span class="merch-details-page__label">负责人电话</span>
					<span class="merch-details-page__value">{{ tableData.phone }}</span>
				</div>
				<div class="merch-details-page__row merch-details-page__row--full">
					<span class="merch-details-page__label">店铺地址</span>
					<span class="merch-details-page__value">{{ tableData.store_address }}</span>
				</div>
			</div>
		</div>

		<div class="nf-table-card merch-details-page__section" v-if="tableData.store_type > 1">
			<div class="merch-details-page__section-head">
				<i class="el-icon-document merch-details-page__section-icon"></i>
				<span>主体信息</span>
			</div>
			<div class="merch-details-page__grid">
				<div class="merch-details-page__row">
					<span class="merch-details-page__label">营业执照编号</span>
					<span class="merch-details-page__value">{{ tableData.id_card }}</span>
				</div>
				<div class="merch-details-page__row">
					<span class="merch-details-page__label">公司注册地址省</span>
					<span class="merch-details-page__value">{{ tableData.store_address }}</span>
				</div>
				<div class="merch-details-page__row merch-details-page__row--full">
					<span class="merch-details-page__label">营业执照照片</span>
					<span class="merch-details-page__value merch-details-page__images">
						<img :src="tableData.company_license_img" alt="" width="148" height="148">
					</span>
				</div>
			</div>
		</div>

		<div class="nf-table-card merch-details-page__section">
			<div class="merch-details-page__section-head">
				<i class="el-icon-user merch-details-page__section-icon"></i>
				<span>经营者信息</span>
			</div>
			<div class="merch-details-page__grid">
				<div class="merch-details-page__row">
					<span class="merch-details-page__label">经营者姓名</span>
					<span class="merch-details-page__value">{{ tableData.name }}</span>
				</div>
				<div class="merch-details-page__row">
					<span class="merch-details-page__label">身份证号码</span>
					<span class="merch-details-page__value">{{ tableData.id_card }}</span>
				</div>
				<div class="merch-details-page__row merch-details-page__row--full">
					<span class="merch-details-page__label">证件照片</span>
					<span class="merch-details-page__value merch-details-page__images">
						<img :src="tableData.card_fount" alt="" width="148" height="148">
						<img :src="tableData.card_back" alt="" width="148" height="148">
					</span>
				</div>
				<div class="merch-details-page__row merch-details-page__row--full">
					<span class="merch-details-page__label">手持证件照</span>
					<span class="merch-details-page__value merch-details-page__images">
						<img :src="tableData.business_license" alt="" width="148" height="148">
					</span>
				</div>
			</div>
		</div>

		<div class="nf-filter-card merch-details-page__actions">
			<el-button type="primary" icon="el-icon-back" @click="prev">返回列表</el-button>
			<el-button v-if="tableData.status == 1" type="primary" icon="el-icon-edit-outline" @click="dispose()">处理</el-button>
		</div>

		<!-- 处理弹框-->
		<el-dialog title="处理审核" :visible.sync="dialogFormVisible" center>
			<div style="margin-bottom: 10px;display: flex;">
				<span
					style="width: 200px;display: inline-block;text-align: right;position: relative;top: -5px;">入驻处理：</span>
				<el-radio-group v-model="status" @change="statusChange">
					<el-radio :label="2">同意入驻</el-radio>
					<el-radio :label="3">拒绝入驻</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 10px;" v-if="!isshow">
				<span
					style="width: 200px;display: inline-block;text-align: right;position:relative;top: -70px;">拒绝理由：</span>
				<el-input style="width:50%;" type="textarea" :rows="4" v-model="refundReason"
					placeholder="请输入拒绝理由"></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="disposeNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				id: '',
				status: 2,
				info: {
					stockDate: this.getNowTime(),
				},
				isshow: true,
				auditYears: '',
				refundReason: '',
				tableDataLoading: true,
				dialogFormVisible: false,
				tableData: {},
			}
		},
		methods: {
			prev() {
				this.$router.back()
			},
			getNowTime() {
				var now = new Date();
				var year = now.getFullYear();
				var month = now.getMonth();
				var date = now.getDate();
				month = month + 1;
				month = month.toString().padStart(2, "0");
				date = date.toString().padStart(2, "0");
				var defaultDate = `${year}-${month}-${date}`;
				return defaultDate;
				this.$set(this.info, "stockDate", defaultDate);
			},
			handleSizeChange(val) {
				this.size = val
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val
				this.dataSelect()
			},
			selectTrigger() {
				this.dataSelect()
			},
			select() {
				this.dataSelect()
			},
			cleans() {
				this.companyName = ''
				this.legalPhone = ''
				this.storeType = 0
				this.dataSelect()
			},
			statusChange(val) {
				if (val == 2) {
					this.isshow = true
					this.status = 2
					this.refundReason = '同意'
				} else {
					this.isshow = false
					this.status = 3
					this.refundReason = ''
				}
			},
			dispose() {
				this.isshow = true
				this.refundReason = '同意'
				this.dialogFormVisible = true
			},
			disposeNoticeTo() {
				if (this.refundReason == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入拒绝理由',
						type: 'warning'
					});
					return
				}
				let id = this.$route.query.id
				this.$http({
					url: this.$http.adornUrl2('/admin/storecomplain/handle_settle_in'),
					method: 'post',
					params: this.$http.adornParams({
						'merchant_apply_id': id,
						'status': this.status,
						'refund_reason': this.refundReason,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							message: '提交成功',
							type: 'success',
							duration: 1500,
						})
						this.dialogFormVisible = false
						this.status = 2
						this.refundReason = ''
						this.$router.replace({
							name: 'merchantEnter'
						})
					}
				})
			},
			dataSelect() {
				let id = this.$route.query.id
				this.$http({
					url: this.$http.adornUrl2('/admin/storecomplain/settle_in_info'),
					method: 'get',
					params: this.$http.adornParams({
						'merchant_apply_id': id,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.tableData = returnData
				})
			}
		},
		activated() {
			this.dataSelect()
		}
	}
</script>

<style scoped>
.merch-details-page__section {
	margin-bottom: 12px;
}

.merch-details-page__section-head {
	display: flex;
	align-items: center;
	gap: 8px;
	margin-bottom: 12px;
	padding-bottom: 8px;
	border-bottom: 1px solid #e8eaed;
	font-size: 14px;
	font-weight: 600;
	color: #303133;
}

.merch-details-page__section-icon {
	color: #5cc477;
	font-size: 16px;
}

.merch-details-page__grid {
	display: grid;
	grid-template-columns: 1fr 1fr;
	gap: 0 24px;
}

.merch-details-page__row {
	display: flex;
	align-items: flex-start;
	padding: 12px 0;
	border-bottom: 1px solid #f0f2f5;
	font-size: 14px;
	line-height: 1.5;
}

.merch-details-page__row--full {
	grid-column: 1 / -1;
}

.merch-details-page__label {
	flex: 0 0 120px;
	color: #909399;
}

.merch-details-page__value {
	flex: 1;
	color: #303133;
	word-break: break-all;
}

.merch-details-page__images img {
	border-radius: 6px;
	border: 1px solid #e8eaed;
	object-fit: cover;
	vertical-align: top;
}

.merch-details-page__images img + img {
	margin-left: 16px;
}

.merch-details-page__actions {
	display: flex;
	justify-content: center;
	align-items: center;
	gap: 12px;
	margin-bottom: 0;
}

@media (max-width: 768px) {
	.merch-details-page__grid {
		grid-template-columns: 1fr;
	}
}
</style>
