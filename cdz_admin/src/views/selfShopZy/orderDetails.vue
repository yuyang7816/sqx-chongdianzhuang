<template>
	<div class="nf-page order-details-zy-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">自营商城</span>
			<h2 class="nf-page-hero__title">订单详情</h2>
			<p class="nf-page-hero__desc">查看自营商城订单基本信息、商品明细、发货与收货信息，支持返回订单列表。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<div class="order-details-zy-page__layout">
			<div class="order-details-zy-page__col-main">
				<div class="nf-table-card order-details-zy-page__section">
					<div class="order-details-zy-page__section-head">
						<i class="el-icon-document order-details-zy-page__section-icon"></i>
						<span>订单信息</span>
					</div>
					<div class="order-details-zy-page__grid">
						<div class="order-details-zy-page__row">
							<span class="order-details-zy-page__label">订单号</span>
							<span class="order-details-zy-page__value">{{ tableData.orderNum }}</span>
						</div>
						<div class="order-details-zy-page__row">
							<span class="order-details-zy-page__label">订单备注</span>
							<span class="order-details-zy-page__value">{{ tableData.descrition ? tableData.descrition : '暂无备注' }}</span>
						</div>
						<div class="order-details-zy-page__row">
							<span class="order-details-zy-page__label">订单状态</span>
							<span class="order-details-zy-page__value">
								<span v-if="tableData.status == 1" class="order-details-zy-page__status">待付款</span>
								<span v-if="tableData.status == 2" class="order-details-zy-page__status">待发货</span>
								<span v-if="tableData.status == 3" class="order-details-zy-page__status">已发货</span>
								<span v-if="tableData.status == 4" class="order-details-zy-page__status">已收货</span>
								<span v-if="tableData.status == 5" class="order-details-zy-page__status">已取消</span>
								<span v-if="tableData.status == 6" class="order-details-zy-page__status">退款中</span>
								<span v-if="tableData.status == 7" class="order-details-zy-page__status">已退款</span>
								<span v-if="tableData.status === 8" class="order-details-zy-page__status">拒绝退款</span>
								<span v-if="tableData.status === 9" class="order-details-zy-page__status">拼团中</span>
								<span v-if="tableData.status === 10" class="order-details-zy-page__status">已评价</span>
							</span>
						</div>
						<div class="order-details-zy-page__row">
							<span class="order-details-zy-page__label">支付方式</span>
							<span class="order-details-zy-page__value">
								<span v-if="tableData.payWay === 1" class="order-details-zy-page__status">app微信</span>
								<span v-if="tableData.payWay === 2" class="order-details-zy-page__status">微信公众号</span>
								<span v-if="tableData.payWay === 3" class="order-details-zy-page__status">微信小程序</span>
								<span v-if="tableData.payWay === 4" class="order-details-zy-page__status">app支付宝</span>
								<span v-if="tableData.payWay === 5" class="order-details-zy-page__status">H5支付宝</span>
								<span v-if="tableData.payWay === 6" class="order-details-zy-page__status">零钱</span>
								<span v-if="tableData.payWay === 7" class="order-details-zy-page__status">积分</span>
								<span v-if="tableData.payWay === null || tableData.payWay === ''">未支付</span>
							</span>
						</div>
						<div class="order-details-zy-page__row">
							<span class="order-details-zy-page__label">创建时间</span>
							<span class="order-details-zy-page__value">{{ tableData.createAt }}</span>
						</div>
						<div class="order-details-zy-page__row">
							<span class="order-details-zy-page__label">付款时间</span>
							<span class="order-details-zy-page__value">{{ tableData.payTime }}</span>
						</div>
					</div>
				</div>

				<div class="nf-table-card order-details-zy-page__section">
					<div class="order-details-zy-page__section-head">
						<i class="el-icon-goods order-details-zy-page__section-icon"></i>
						<span>商品信息</span>
					</div>
					<div class="order-details-zy-page__product-table">
						<div class="order-details-zy-page__product-head">
							<span class="order-details-zy-page__product-col order-details-zy-page__product-col--img">商品图片</span>
							<span class="order-details-zy-page__product-col order-details-zy-page__product-col--title">商品标题</span>
							<span class="order-details-zy-page__product-col order-details-zy-page__product-col--price">商品价格</span>
							<span class="order-details-zy-page__product-col order-details-zy-page__product-col--postage">运费</span>
							<span class="order-details-zy-page__product-col order-details-zy-page__product-col--pay">支付金额</span>
						</div>
						<div class="order-details-zy-page__product-row">
							<span class="order-details-zy-page__product-col order-details-zy-page__product-col--img">
								<img
									v-if="tableData.img"
									:src="tableData.img"
									class="order-details-zy-page__product-img"
									width="40"
									height="40"
									alt=""
								>
								<span v-else>-</span>
							</span>
							<span class="order-details-zy-page__product-col order-details-zy-page__product-col--title">
								<span class="order-details-zy-page__product-title">{{ tableData.title }}</span>
								<span class="order-details-zy-page__product-spec">规格：{{ tableData.detailJson ? tableData.detailJson : '-' }}</span>
							</span>
							<span class="order-details-zy-page__product-col order-details-zy-page__product-col--price">￥{{ tableData.price }} x {{ tableData.number }}</span>
							<span class="order-details-zy-page__product-col order-details-zy-page__product-col--postage">￥{{ tableData.postagePrice }}</span>
							<span class="order-details-zy-page__product-col order-details-zy-page__product-col--pay order-details-zy-page__amount">￥{{ tableData.payMoney }}</span>
						</div>
					</div>
				</div>
			</div>

			<div class="order-details-zy-page__col-side">
				<div class="nf-table-card order-details-zy-page__section">
					<div class="order-details-zy-page__section-head">
						<i class="el-icon-s-promotion order-details-zy-page__section-icon"></i>
						<span>发货信息</span>
					</div>
					<div class="order-details-zy-page__grid order-details-zy-page__grid--single">
						<div class="order-details-zy-page__row">
							<span class="order-details-zy-page__label">发货人</span>
							<span class="order-details-zy-page__value">官方</span>
						</div>
						<div class="order-details-zy-page__row">
							<span class="order-details-zy-page__label">订单数量</span>
							<span class="order-details-zy-page__value">{{ tableData.number }}件</span>
						</div>
					</div>
				</div>

				<div class="nf-table-card order-details-zy-page__section">
					<div class="order-details-zy-page__section-head">
						<i class="el-icon-location-outline order-details-zy-page__section-icon"></i>
						<span>收货信息</span>
					</div>
					<div class="order-details-zy-page__grid order-details-zy-page__grid--single">
						<div class="order-details-zy-page__row">
							<span class="order-details-zy-page__label">收货人</span>
							<span class="order-details-zy-page__value">{{ tableData.consignee }}</span>
						</div>
						<div class="order-details-zy-page__row">
							<span class="order-details-zy-page__label">手机号</span>
							<span class="order-details-zy-page__value">{{ tableData.mobile }}</span>
						</div>
						<div class="order-details-zy-page__row order-details-zy-page__row--full">
							<span class="order-details-zy-page__label">收货地址</span>
							<span class="order-details-zy-page__value">{{ tableData.provinces }}{{ tableData.detail }}</span>
						</div>
					</div>
				</div>

				<div class="nf-table-card order-details-zy-page__section" v-if="tableData.isExpress == 1">
					<div class="order-details-zy-page__section-head">
						<i class="el-icon-truck order-details-zy-page__section-icon"></i>
						<span>物流信息</span>
					</div>
					<div class="order-details-zy-page__grid order-details-zy-page__grid--single" v-if="tableData.status == 3 || tableData.status == 4">
						<div class="order-details-zy-page__row">
							<span class="order-details-zy-page__label">物流公司</span>
							<span class="order-details-zy-page__value">{{ tableData.expressName }}</span>
						</div>
						<div class="order-details-zy-page__row">
							<span class="order-details-zy-page__label">运单号</span>
							<span class="order-details-zy-page__value">{{ tableData.expressNumber }}</span>
						</div>
					</div>
					<div class="order-details-zy-page__empty" v-else>暂无信息</div>
				</div>

				<div class="nf-table-card order-details-zy-page__section" v-else>
					<div class="order-details-zy-page__section-head">
						<i class="el-icon-tickets order-details-zy-page__section-icon"></i>
						<span>虚拟商品信息</span>
					</div>
					<div class="order-details-zy-page__grid order-details-zy-page__grid--single">
						<div class="order-details-zy-page__row">
							<span class="order-details-zy-page__label">商品标题</span>
							<span class="order-details-zy-page__value">{{ virtualData.title }}</span>
						</div>
						<div class="order-details-zy-page__row">
							<span class="order-details-zy-page__label">兑换码</span>
							<span class="order-details-zy-page__value">{{ virtualData.content }}</span>
						</div>
						<div class="order-details-zy-page__row order-details-zy-page__row--full">
							<span class="order-details-zy-page__label">兑换地址</span>
							<span class="order-details-zy-page__value">{{ virtualData.linkUrl }}</span>
						</div>
					</div>
				</div>
			</div>
		</div>

		<div class="nf-filter-card order-details-zy-page__actions">
			<el-button type="primary" icon="el-icon-back" @click="prev">返回</el-button>
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
        virtualId: '',
        dialogFormVisible: false,
        tableData: {},
        discussData: '',
        virtualData: {},
      }
    },

    methods: {
      // 获取评价数据
      discusselect() {
        let id = this.$route.query.id
        let page = this.page - 1
        this.tableDataLoading = true
        this.$http({
          url: this.$http.adornUrl2('/selfGoodsComment/findByOrderId'),
          method: 'get',
          params: this.$http.adornParams({
            'orderId': id
          })
        }).then(({
          data
        }) => {
          if (data && data.status == 0) {
            this.tableDataLoading = false
            let returnData = data.data;
            this.discussData = returnData
          } else {
            this.$message.error(data.msg)
          }

        })
      },
      // 返回上一级
      prev() {
        this.$router.back()
      },
      // 获取订单详情数据
      dataSelect() {
        let id = this.$route.query.id
        this.tableDataLoading = true
        this.$http({
          url: this.$http.adornUrl('shopOrders/find'),
          method: 'get',
          params: this.$http.adornParams({
            'id': id
          })
        }).then(({
          data
        }) => {
          this.tableDataLoading = false
          let returnData = data.data;
          this.tableData = returnData
          this.virtualId = returnData.virtualId
          console.log(this.virtualId)
          this.$http({
            url: this.$http.adornUrl('selfGoodsVirtual/find'),
            method: 'get',
            params: this.$http.adornParams({
              'id': this.virtualId
            })
          }).then(({
            data
          }) => {
            this.tableDataLoading = false
            let returnData = data.data;
            this.virtualData = returnData
          })
        })
      }
    },
    activated() {
      this.dataSelect()
      this.discusselect()
    }
  };
</script>

<style scoped>
.order-details-zy-page__layout {
	display: grid;
	grid-template-columns: minmax(0, 1.6fr) minmax(0, 1fr);
	gap: 12px;
	align-items: start;
}

.order-details-zy-page__section {
	margin-bottom: 12px;
}

.order-details-zy-page__col-main .order-details-zy-page__section:last-child,
.order-details-zy-page__col-side .order-details-zy-page__section:last-child {
	margin-bottom: 0;
}

.order-details-zy-page__section-head {
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

.order-details-zy-page__section-icon {
	color: #5cc477;
	font-size: 16px;
}

.order-details-zy-page__grid {
	display: grid;
	grid-template-columns: 1fr 1fr;
	gap: 0 24px;
}

.order-details-zy-page__grid--single {
	grid-template-columns: 1fr;
}

.order-details-zy-page__row {
	display: flex;
	align-items: flex-start;
	padding: 12px 0;
	border-bottom: 1px solid #f0f2f5;
	font-size: 14px;
	line-height: 1.5;
}

.order-details-zy-page__row--full {
	grid-column: 1 / -1;
}

.order-details-zy-page__label {
	flex: 0 0 80px;
	color: #909399;
}

.order-details-zy-page__value {
	flex: 1;
	color: #303133;
	word-break: break-all;
}

.order-details-zy-page__status {
	color: #5cc477;
	font-weight: 600;
}

.order-details-zy-page__amount {
	color: #f56c6c;
	font-weight: 600;
}

.order-details-zy-page__product-table {
	border: 1px solid #e8eaed;
	border-radius: 8px;
	overflow: hidden;
}

.order-details-zy-page__product-head,
.order-details-zy-page__product-row {
	display: grid;
	grid-template-columns: 80px 1fr 120px 72px 100px;
	align-items: center;
	gap: 12px;
	padding: 12px 16px;
	font-size: 14px;
}

.order-details-zy-page__product-head {
	background: #f7f8fa;
	color: #909399;
	font-size: 13px;
	border-bottom: 1px solid #e8eaed;
}

.order-details-zy-page__product-row {
	color: #303133;
}

.order-details-zy-page__product-col {
	word-break: break-all;
}

.order-details-zy-page__product-col--title {
	display: flex;
	flex-direction: column;
	gap: 4px;
}

.order-details-zy-page__product-title {
	line-height: 1.5;
}

.order-details-zy-page__product-spec {
	font-size: 13px;
	color: #909399;
}

.order-details-zy-page__product-img {
	border-radius: 6px;
	border: 1px solid #e8eaed;
	object-fit: cover;
	vertical-align: middle;
}

.order-details-zy-page__empty {
	padding: 12px 0;
	font-size: 14px;
	color: #909399;
}

.order-details-zy-page__actions {
	display: flex;
	justify-content: center;
	align-items: center;
	gap: 12px;
	margin-top: 12px;
	margin-bottom: 0;
}

@media (max-width: 992px) {
	.order-details-zy-page__layout {
		grid-template-columns: 1fr;
	}

	.order-details-zy-page__product-head,
	.order-details-zy-page__product-row {
		grid-template-columns: 1fr;
		gap: 8px;
	}

	.order-details-zy-page__product-head {
		display: none;
	}

	.order-details-zy-page__product-col--price::before {
		content: '商品价格：';
		color: #909399;
	}

	.order-details-zy-page__product-col--postage::before {
		content: '运费：';
		color: #909399;
	}

	.order-details-zy-page__product-col--pay::before {
		content: '支付金额：';
		color: #909399;
	}
}

@media (max-width: 768px) {
	.order-details-zy-page__grid {
		grid-template-columns: 1fr;
	}
}
</style>
