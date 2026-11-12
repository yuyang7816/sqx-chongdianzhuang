<template>
	<div class="nf-page order-details-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">订单管理</span>
			<h2 class="nf-page-hero__title">订单详情</h2>
			<p class="nf-page-hero__desc">查看订单基本信息、商品信息与支付状态，支持返回订单列表。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<div class="nf-table-card order-details-page__section">
			<div class="order-details-page__section-head">
				<i class="el-icon-document order-details-page__section-icon"></i>
				<span>订单信息</span>
			</div>
			<div class="order-details-page__grid">
				<div class="order-details-page__row">
					<span class="order-details-page__label">订单号</span>
					<span class="order-details-page__value order-details-page__value--emphasis">{{ tableData.ordersNo }}</span>
				</div>
				<div class="order-details-page__row">
					<span class="order-details-page__label">订单备注</span>
					<span class="order-details-page__value order-details-page__value--emphasis">{{ tableData.remark ? tableData.remark : '未填写' }}</span>
				</div>
				<div class="order-details-page__row">
					<span class="order-details-page__label">订单状态</span>
					<span class="order-details-page__value">
						<span v-if="tableData.status == 1" class="order-details-page__status">待支付</span>
						<span v-if="tableData.status == 2" class="order-details-page__status">待使用</span>
						<span v-if="tableData.status == 3" class="order-details-page__status">使用中</span>
						<span v-if="tableData.status == 4" class="order-details-page__status">已完成</span>
						<span v-if="tableData.status == 5" class="order-details-page__status">已取消</span>
						<span v-if="tableData.status == 6" class="order-details-page__status">待评价</span>
						<span v-if="tableData.status == 7" class="order-details-page__status">已退款</span>
						<span v-if="tableData.status == 8" class="order-details-page__status">
							已失效
							<span class="order-details-page__value--warn">（失效时间：{{ tableData.expired_time | formatDate }}）</span>
						</span>
					</span>
				</div>
				<div class="order-details-page__row">
					<span class="order-details-page__label">支付方式</span>
					<span class="order-details-page__value">
						<span v-if="tableData.payWay === 1" class="order-details-page__status">微信</span>
						<span v-if="tableData.payWay === 2" class="order-details-page__status">支付宝</span>
						<span v-if="tableData.payWay === 3" class="order-details-page__status">零钱</span>
					</span>
				</div>
				<div class="order-details-page__row">
					<span class="order-details-page__label">创建时间</span>
					<span class="order-details-page__value">{{ tableData.createTime }}</span>
				</div>
				<div class="order-details-page__row">
					<span class="order-details-page__label">付款时间</span>
					<span class="order-details-page__value">{{ tableData.payTime ? tableData.payTime : '未付款' }}</span>
				</div>
				<div class="order-details-page__row">
					<span class="order-details-page__label">预留姓名</span>
					<span class="order-details-page__value">{{ tableData.userName }}</span>
				</div>
				<div class="order-details-page__row">
					<span class="order-details-page__label">预留电话</span>
					<span class="order-details-page__value">{{ tableData.phone ? tableData.phone : '未填写' }}</span>
				</div>
				<div class="order-details-page__row">
					<span class="order-details-page__label">续单时长</span>
					<span class="order-details-page__value">{{ tableData.addHour }}</span>
				</div>
				<div class="order-details-page__row">
					<span class="order-details-page__label">续单金额</span>
					<span class="order-details-page__value">{{ tableData.addPayMoney }}</span>
				</div>
				<div class="order-details-page__row">
					<span class="order-details-page__label">开始时间</span>
					<span class="order-details-page__value">{{ tableData.startTime }}</span>
				</div>
				<div class="order-details-page__row">
					<span class="order-details-page__label">结束时间</span>
					<span class="order-details-page__value">{{ tableData.endTime }}</span>
				</div>
				<div class="order-details-page__row order-details-page__row--full">
					<span class="order-details-page__label">过期时间</span>
					<span class="order-details-page__value">{{ tableData.expired_time }}</span>
				</div>
			</div>
		</div>

		<div class="nf-table-card order-details-page__section">
			<div class="order-details-page__section-head">
				<i class="el-icon-goods order-details-page__section-icon"></i>
				<span>商品信息</span>
			</div>
			<div class="order-details-page__product">
				<div class="order-details-page__product-item">
					<span class="order-details-page__product-label">所属台球馆</span>
					<span class="order-details-page__product-value">{{ tableData.tearoom ? tableData.tearoom.tearoomName : '-' }}</span>
				</div>
				<div class="order-details-page__product-item">
					<span class="order-details-page__product-label">台球馆图片</span>
					<span class="order-details-page__product-value">
						<img
							v-if="tableData.goodsCoverImage"
							:src="tableData.goodsCoverImage"
							class="order-details-page__product-img"
							width="60"
							height="60"
							alt=""
						>
						<span v-else>-</span>
					</span>
				</div>
				<div class="order-details-page__product-item">
					<span class="order-details-page__product-label">台球馆名称</span>
					<span class="order-details-page__product-value">{{ tableData.goodsName }}</span>
				</div>
				<div class="order-details-page__product-item">
					<span class="order-details-page__product-label">订单价格</span>
					<span class="order-details-page__product-value">{{ tableData.payMoney != null ? tableData.payMoney : '未付款' }}</span>
				</div>
			</div>
		</div>

		<div class="nf-filter-card order-details-page__actions">
			<el-button type="primary" icon="el-icon-back" @click="prev">返回列表</el-button>
		</div>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				id: '',
				expressName: '',
				expressNumber: '',
				orderName: '',
				orderNumber: '',
				dialogFormVisible: false,
				dialogVisible: false,
				tableData: {},
				discussData: {},
				wuliuDate: {},
				kdName: '快递单号',
				reverse: true,
				order_id: '',
			}
		},
		filters: {
			formatDate: function(value) {
				let date = new Date(value / 1000000);
				let y = date.getFullYear();
				let MM = date.getMonth() + 1;
				MM = MM < 10 ? ('0' + MM) : MM;
				let d = date.getDate();
				d = d < 10 ? ('0' + d) : d;
				let h = date.getHours();
				h = h < 10 ? ('0' + h) : h;
				let m = date.getMinutes();
				m = m < 10 ? ('0' + m) : m;
				let s = date.getSeconds();
				s = s < 10 ? ('0' + s) : s;
				return y + '-' + MM + '-' + d + ' ' + h + ':' + m + ':' + s;
			}
		},
		methods: {
			prev() {
				this.$router.back()
			},
			dataSelect() {
				let id = this.$route.query.ordersNo
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('orders/selectOrdersDetails'),
					method: 'get',
					params: this.$http.adornParams({
						'ordersNo': id
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData
					this.order_id = data.data.id
				})
			},
		},
		activated() {
			this.dataSelect()
		}
	};
</script>

<style scoped>
.order-details-page__section {
	margin-bottom: 12px;
}

.order-details-page__section-head {
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

.order-details-page__section-icon {
	color: #5cc477;
	font-size: 16px;
}

.order-details-page__grid {
	display: grid;
	grid-template-columns: 1fr 1fr;
	gap: 0 24px;
}

.order-details-page__row {
	display: flex;
	align-items: flex-start;
	padding: 12px 0;
	border-bottom: 1px solid #f0f2f5;
	font-size: 14px;
	line-height: 1.5;
}

.order-details-page__row--full {
	grid-column: 1 / -1;
}

.order-details-page__label {
	flex: 0 0 100px;
	color: #909399;
}

.order-details-page__value {
	flex: 1;
	color: #303133;
	word-break: break-all;
}

.order-details-page__value--emphasis {
	color: #f56c6c;
	font-weight: 600;
}

.order-details-page__value--warn {
	color: #f56c6c;
	font-weight: 400;
}

.order-details-page__status {
	color: #5cc477;
	font-weight: 600;
}

.order-details-page__product {
	display: grid;
	grid-template-columns: repeat(4, 1fr);
	gap: 16px;
}

.order-details-page__product-item {
	display: flex;
	flex-direction: column;
	gap: 8px;
	padding: 12px;
	background: #f7f8fa;
	border-radius: 8px;
	border: 1px solid #e8eaed;
}

.order-details-page__product-label {
	font-size: 13px;
	color: #909399;
}

.order-details-page__product-value {
	font-size: 14px;
	color: #303133;
	word-break: break-all;
}

.order-details-page__product-img {
	border-radius: 6px;
	border: 1px solid #e8eaed;
	object-fit: cover;
}

.order-details-page__actions {
	display: flex;
	justify-content: center;
	align-items: center;
	gap: 12px;
	margin-bottom: 0;
}

@media (max-width: 992px) {
	.order-details-page__product {
		grid-template-columns: repeat(2, 1fr);
	}
}

@media (max-width: 768px) {
	.order-details-page__grid {
		grid-template-columns: 1fr;
	}

	.order-details-page__product {
		grid-template-columns: 1fr;
	}
}
</style>
