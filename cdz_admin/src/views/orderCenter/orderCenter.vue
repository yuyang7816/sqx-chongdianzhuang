<template>
	<div class="nf-page order-center-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">订单中心</span>
			<h2 class="nf-page-hero__title">订单中心</h2>
			<p class="nf-page-hero__desc">查询与管理全部充电订单，支持多条件筛选、导出及退款、换台、结束订单等操作。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<el-tabs v-model="activeName" @tab-click="handleClick" class="order-center-page__tabs">
		<el-tab-pane label="全部订单" name="first">
			<div class="nf-filter-card">
				<div class="nf-filter-card__head">
					<i class="el-icon-search nf-filter-card__head-icon"></i>
					<span>筛选条件</span>
				</div>
				<el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
					<el-form-item label="充电站名称">
						<el-input
							class="order-center-page__filter-input"
							@keydown.enter.native="select"
							placeholder="请输入充电站名称"
							v-model="tearoomNameT">
						</el-input>
					</el-form-item>
					<el-form-item label="手机号">
						<el-input
							class="order-center-page__filter-input"
							@keydown.enter.native="select"
							placeholder="请输入手机号"
							v-model="ordersType">
						</el-input>
					</el-form-item>
					<el-form-item label="用户昵称">
						<el-input
							class="order-center-page__filter-input"
							@keydown.enter.native="select"
							placeholder="请输入用户昵称"
							v-model="userName">
						</el-input>
					</el-form-item>
					<el-form-item label="充电桩名称">
						<el-input
							class="order-center-page__filter-input"
							@keydown.enter.native="select"
							placeholder="请输入充电桩名称"
							v-model="ordersUserName">
						</el-input>
					</el-form-item>
					<el-form-item label="状态">
						<el-select
							class="order-center-page__filter-select"
							v-model="status"
							placeholder="请选择状态"
							@change="animeDat(status)">
							<el-option v-for="item in statesnum" :key="item.value" :label="item.label" :value="item.value">
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item label="优惠券平台">
						<el-select
							class="order-center-page__filter-select"
							v-model="platformTypeT"
							placeholder="请选择平台"
							@change="animeDat()">
							<el-option v-for="item in platformNum" :key="item.value" :label="item.label" :value="item.value">
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item label="支付方式">
						<el-select
							class="order-center-page__filter-select"
							v-model="orderTypeT"
							placeholder="请选择支付方式"
							@change="animeDat()">
							<el-option v-for="item in typeNum" :key="item.value" :label="item.label" :value="item.value">
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item label="订单编号">
						<el-input
							class="order-center-page__filter-input order-center-page__filter-input--wide"
							@keydown.enter.native="select"
							placeholder="请输入订单编号"
							v-model="ordersNoT">
						</el-input>
					</el-form-item>
					<el-form-item label="开始时间">
						<el-date-picker
							class="order-center-page__filter-date"
							v-model="startTime"
							align="right"
							type="datetime"
							format="yyyy-MM-dd"
							value-format="yyyy-MM-dd"
							placeholder="选择开始时间">
						</el-date-picker>
					</el-form-item>
					<el-form-item label="截止时间">
						<el-date-picker
							class="order-center-page__filter-date"
							v-model="endTime"
							align="right"
							type="datetime"
							format="yyyy-MM-dd"
							value-format="yyyy-MM-dd"
							placeholder="选择截止时间">
						</el-date-picker>
					</el-form-item>
					<el-form-item class="order-center-page__filter-actions">
						<el-button type="primary" icon="el-icon-search" @click="select">查询</el-button>
						<el-button type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
						<el-button type="primary" icon="el-icon-download" @click="exportBtn">导出</el-button>
					</el-form-item>
				</el-form>
			</div>

			<div class="nf-table-card">
			<el-table v-loading="tableDataLoading" :data="tableData.list">
				<el-table-column fixed prop="ordersId" label="编号" width="80">
				</el-table-column>
				
				<el-table-column fixed prop="tearoomName" label="充电站" width="160">
				</el-table-column>
				<el-table-column fixed prop="goodsName" label="充电桩名称">
					<template slot-scope="scope">
						<span>{{scope.row.goodsName}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="ordersNo" label="订单号" width="200">
				</el-table-column>
				<el-table-column prop="nickname" label="用户昵称" width="120">
					<template slot-scope="scope">
						<span style="color: #5cc477;cursor: pointer;" v-if="scope.row.userId"
							@click="updates(scope.row.userId)">{{scope.row.payUserName ? scope.row.payUserName : '未绑定'}}</span>
						<span style="color: #f56c6c;" v-else>-</span>
					</template>
				</el-table-column>
				<!-- <el-table-column prop="hourPrice" label="每小时价格">
					<template slot-scope="scope">
						<span style="color: deepskyblue;"
							v-if="scope.row.goods">{{scope.row.goods.hourPrice}}元</span>
					</template>
				</el-table-column>
				<el-table-column prop="minutePrice" label="每分钟价格">
					<template slot-scope="scope">
						<span style="color: deepskyblue;"
							v-if="scope.row.goods">{{scope.row.goods.minutePrice}}元</span>
					</template>
				</el-table-column> -->
				<el-table-column prop="price" label="小时数">
					<template slot-scope="scope">
						<span v-if="scope.row.productId">-</span>
						<span v-else>{{scope.row.hour}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="platformType" label="优惠券平台">
					<template slot-scope="scope">
						<span v-if="scope.row.platformType==1">美团</span>
						<span v-else-if="scope.row.platformType==2">抖音</span>
						<span v-else-if="scope.row.platformType==3">快手</span>
						<span v-else>-</span>
					</template>
				</el-table-column>
				<el-table-column prop="couponName" label="优惠券名称" width="120">
				</el-table-column>
				<el-table-column prop="couponMoney" label="优惠券金额">
					<template slot-scope="scope">
						<span
							style="color: #f56c6c;cursor: pointer;">{{scope.row.couponMoney?scope.row.couponMoney:'0' }}元</span>
					</template>
				</el-table-column>
				<el-table-column prop="vipMoney" label="会员优惠金额">
					<template slot-scope="scope">
						<span
							style="color: #f56c6c;cursor: pointer;">{{scope.row.vipMoney?scope.row.vipMoney:'0' }}元</span>
					</template>
				</el-table-column>
				<el-table-column prop="payMoney" label="支付金额" width="100">
					<template slot-scope="scope">
						<span
							style="color: #f56c6c;cursor:pointer;">{{scope.row.payMoney ?scope.row.payMoney:0}}元</span>
						<!-- <el-button size="mini" type="primary" style="margin:3px" plain
							@click="moneyClick(scope.row)">其他金额
						</el-button> -->
					</template>
				</el-table-column>
				<el-table-column prop="refundMoney" label="已退款金额">
					<template slot-scope="scope">
						<span
							style="color: #f56c6c;cursor: pointer;">{{scope.row.refundMoney?scope.row.refundMoney+'元':'-' }}</span>
					</template>
				</el-table-column>
				<el-table-column prop="refundRemark" label="退款原因" width="160">
				</el-table-column>
				<el-table-column prop="endTime" label="结束时间" width="160">
				</el-table-column>
				<el-table-column prop="minuteNum" label="超出时间(分钟)">
				</el-table-column>
				<el-table-column prop="minuteMoney" label="超出费用">
				</el-table-column>
				<!-- <el-table-column prop="cashPledge" label="押金">
				</el-table-column> -->
				<el-table-column prop="shopMoney" label="商家总收益">
				</el-table-column>
				<el-table-column prop="payWay" label="支付方式">
					<template slot-scope="scope">
						<span style="color: #5cc477;" v-if='scope.row.payWay == 1'>微信</span>
						<span style="color: #5cc477;" v-if='scope.row.payWay == 2'>支付宝</span>
						<span style="color: #5cc477;" v-if='scope.row.payWay == 3'>零钱</span>
					</template>
				</el-table-column>
				<el-table-column fixed="right" prop="status" label="订单状态">
					<template slot-scope="scope">
						<span style="color: #5cc477;" v-if="scope.row.status === 1 ">待支付</span>
						<span style="color: #5cc477;" v-if="scope.row.status === 2 ">预约中 </span>
						<span style="color: #999999;" v-if="scope.row.status === 3 ">使用中 </span>
						<span style="color: #999999;" v-if="scope.row.status === 4 ">已完成</span>
						<span style="color: #999999;" v-if="scope.row.status === 5 ">已取消</span>
						<span style="color: #5cc477;" v-if="scope.row.status === 6 ">待评价</span>
						<span style="color: #999999;" v-if="scope.row.status === 7 ">待补费</span>
						<!-- <span style="color: #999999;" v-if="scope.row.status === 8 ">已失效</span> -->
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160">
				</el-table-column>
				<el-table-column prop="payTime" label="付款时间" width="160">
					<template slot-scope="scope">
						<span>{{scope.row.payTime ? scope.row.payTime : '未付款'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" fixed="right" label="操作" width="200">
					<template slot-scope="scope">
						<!-- <el-button size="mini" type="primary" plain style="margin:3px" @click="xufeiClick(scope.row)">续费记录
						</el-button> -->
						<el-button size="mini" v-if="scope.row.status === 3 " type="primary" plain style="margin:3px"
							:disabled="!isAuth('orderAdmin:huantai')" @click="huantaiClick(scope.row)">换台
						</el-button>
						<!-- <el-button size="mini" type="primary" style="margin:3px" @click="vidoClick(scope.row)">视频列表
						</el-button> -->
						<!-- <el-button size="mini" type="danger" plain style="margin:3px"
							:disabled="isAuth('orderAdmin:zhuandan')&&scope.row.userId"
							@click="userselect(scope.row)">转单
						</el-button>
						 -->
						<el-button size="mini" type="warning" style="margin:3px"  v-if="scope.row.status === 2||scope.row.status === 3||scope.row.status === 4"
							:disabled="!isAuth('orderAdmin:refund')||scope.row.payMoney==scope.row.refundMoney" plain
							@click="refundClickBf(scope.row)">部分退款
						</el-button>
						<el-button size="mini" type="warning" style="margin:3px"  v-if="scope.row.status === 2||scope.row.status === 3||scope.row.status === 4"
							:disabled="!isAuth('orderAdmin:refund')||scope.row.payMoney==scope.row.refundMoney" @click="refundClick(scope.row)">退全款
							
						</el-button>
						<div v-if="scope.row.isShop==1">
							<el-button size="mini" v-if="scope.row.status === 3||scope.row.status === 8" type="primary" style="margin:3px"
								:disabled="!isAuth('orderAdmin:refund')" @click="jieshuClickSj(scope.row)">结束订单
							</el-button>
							<el-button size="mini" v-if="scope.row.status === 3" type="primary" style="margin:3px"
								:disabled="!isAuth('orderAdmin:refund')" @click="jieshuClickGz(scope.row)">挂账
							</el-button>
						</div>
						
						<div v-else>
							<el-button size="mini" v-if="scope.row.status === 3||scope.row.status === 8" type="primary" style="margin:3px"
								:disabled="!isAuth('orderAdmin:refund')" @click="jieshuClick(scope.row)">结束订单
							</el-button>
						</div>
						
						<el-button size="mini" v-if="scope.row.status === 7" type="danger" style="margin:3px"
							:disabled="!isAuth('orderAdmin:refund')" @click="bufeiClick(scope.row)">完成补费
						</el-button>
						
						
						
					</template>
				</el-table-column>
			</el-table>
			<div class="order-center-page__summary">
				本页累计支付统计：<span class="order-center-page__summary-num">{{totalMoney.toFixed(2)}}</span>元；
				超出费用统计：<span class="order-center-page__summary-num">{{totalMinuteMoney.toFixed(2)}}</span>元；
				优惠券金额统计：<span class="order-center-page__summary-num">{{totalVipMoney.toFixed(2)}}</span>元
			</div>
			<div class="order-center-page__pagination">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
				</el-pagination>
			</div>
			</div>
		</el-tab-pane>
		</el-tabs>

		<!-- 用户列表 -->
		<el-dialog title="用户列表" :visible.sync="dialogFormVisibleU" center width="80%">
			<div style="margin:2% 0;display: inline-block;">
				<span>手机号</span>
				<el-input style="width: 150px;" @keydown.enter.native="userclick" clearable placeholder="请输入手机号"
					v-model="phone1"></el-input>&nbsp;&nbsp;
				<span>昵称</span>
				<el-input style="width: 150px;" @keydown.enter.native="userclick" clearable placeholder="请输入昵称"
					v-model="nickName2"></el-input>&nbsp;&nbsp;
				<!-- <span>邀请人邀请码</span>
				<el-input style="width: 150px;" @keydown.enter.native="userclick" clearable placeholder="请输入邀请人邀请码"
					v-model="inviterCode"></el-input> -->
				<!-- <span>是否会员：</span>
				<el-select v-model="isVipT" style="width:150px;margin-left: 10px;" @change="userclick()">
					<el-option v-for="item in isVipType" :key="item.value" :label="item.label" :value="item.value">
					</el-option>
				</el-select>&nbsp;&nbsp;&nbsp; -->
				<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="userclick">
					查询
				</el-button>
				<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="userclose">
					重置
				</el-button>
			</div>
			<el-table width="780px" v-loading="tableDataLoadingU" :data="userIds.list">
				<el-table-column prop="userId" width="80" label="编号">
				</el-table-column>
				<el-table-column prop="nickName" label="昵称">
					<template slot-scope="scope">
						<span style="color: #f56c6c;">{{scope.row.userName ? scope.row.userName : '未设置'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="phone" label="手机号">
					<template slot-scope="scope">
						<span>{{scope.row.phone ? scope.row.phone : '未绑定'}}</span>
					</template>
				</el-table-column>
				<!-- <el-table-column label="邀请人邀请码">
					<template slot-scope="scope">
						<span>{{ scope.row.inviterCode ? scope.row.inviterCode : '未绑定' }}</span>
					</template>
				</el-table-column> -->
				<el-table-column prop="age" label="年龄">
				</el-table-column>
				<el-table-column prop="sex" label="性别">
					<template slot-scope="scope">
						<span>{{scope.row.sex==1 ? '男' : '女'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间">
				</el-table-column>
				<!-- <el-table-column prop="createTime" label="创建时间">
				</el-table-column> -->
				<el-table-column label="操作" width="120">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" @click="confirm(scope.row)">确定
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange2" @current-change="handleCurrentChange2"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="sizeU" :current-page="pageU"
					layout="total,sizes, prev, pager, next,jumper" :total="userIds.totalPage">
				</el-pagination>
			</div>
		</el-dialog>
		<el-dialog title="续单" :visible.sync="dialogFormVisibleXd" center width="80%">
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">续单日期：</span>
				<span>
					<el-date-picker style="width: 200px;" v-model="xudanTime" align="right" type="date"
						format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择日期" @change="xdDatas()">
					</el-date-picker>
				</span>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">结束时间：</span>
				<div style="display: inline-block;margin-left: 200px;">
					<el-radio-group v-model="times" size="mini">
						<el-radio v-for="(item,index) in timesList" :key="index" :label="item.roomTime" border
							style="margin: 5px;" :disabled="item.type==1">
							{{item.roomTime}}
						</el-radio>
					</el-radio-group>
				</div>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisibleXd = false">取 消</el-button>
				<el-button type="primary" @click="xudanNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
		<el-dialog title="续单列表" :visible.sync="dialogFormVisibleXdL" center width="70%">
			<el-table v-loading="tableDataLoadingX" :data="xudanList">
				<el-table-column prop="id" label="编号" width="50"></el-table-column>
				<el-table-column prop="order_number" label="订单号">
				</el-table-column>
				<el-table-column prop="pay_order" label="支付单号">
				</el-table-column>
				<el-table-column prop="paymenttype" label="支付方式">
					<template slot-scope="scope">
						<span style="color: #5cc477;" v-if='scope.row.paymenttype === "alipay"'>支付宝</span>
						<span style="color: #5cc477;" v-if='scope.row.paymenttype ==="wechat"'>微信</span>
						<span style="color: #5cc477;" v-if='scope.row.paymenttype ==="lingqian"'>零钱</span>
					</template>
				</el-table-column>
				<el-table-column prop="payment" label="支付金额">
				</el-table-column>
				<el-table-column prop="start_time" label="续单开始时间">
				</el-table-column>
				<el-table-column prop="ending_time" label="续单结束时间">
				</el-table-column>
			</el-table>
		</el-dialog>
		<!-- 修改备注时间 补费金额-->
		<el-dialog title="修改补费金额" :visible.sync="dialogFormVisibleBz" center>
			<!-- <div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">开始时间：</span>
				<el-date-picker style="width: 50%;" v-model="startTime" align="right" type="datetime"
					format="yyyy-MM-dd HH:mm" value-format="yyyy-MM-dd HH:mm" placeholder="选择开始时间">
				</el-date-picker>&nbsp;&nbsp;&nbsp;
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">结束时间：</span>
				<el-date-picker style="width: 50%;" v-model="endTime" align="right" type="datetime"
					format="yyyy-MM-dd HH:mm" value-format="yyyy-MM-dd HH:mm" placeholder="选择结束时间">
				</el-date-picker>&nbsp;&nbsp;&nbsp;
			</div>
			<div style="margin-bottom: 10px;">
				<span
					style="width: 200px;display: inline-block;text-align: right;position: relative;top: -70px;">备注：</span>
				<el-input style="width:50%;" v-model="content" type="textarea" :rows="4" placeholder="请输入备注">
				</el-input>
			</div> -->
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">补费金额：</span>
				<el-input style="width:50%;" v-model="content" type="number" min="0" placeholder="请输入补费金额">
				</el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisibleBz = false">取 消</el-button>
				<el-button type="primary" @click="replyNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
		<el-dialog title="视频列表" :visible.sync="dialogFormVisibleVido" center width="80%">
			<div style="text-align: right;">
				<div style="position: relative;display: inline-block;margin: 3px;">
					<span>视频类型：</span>
					<el-select v-model="classifyT" style="width:150px;margin-left: 10px;" @change="vidoSx()">
						<el-option v-for="item in homeData1" :key="item.value" :label="item.name" :value="item.id">
						</el-option>
					</el-select>&nbsp;&nbsp;&nbsp;
				</div>
				<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="vidoSx">
					刷新
				</el-button>
			</div>
			<div v-for="(item,index) in choicenData.records" :key="index"
				style="display: inline-block;width: 20%;padding: 10px;">
				<video style="width: 100%;height: auto;" :src="item.videoUrl" type="audio/wav"
					controls="controls"></video>
				<div style="font-size: 12px;color: #666;display: inline-block;width: 60%;">编号：{{item.ordersVideoId}}
				</div>
				<div v-if="item.classify==1" style="display: inline-block;color: #13ce66;width: 37%;text-align: right;">
					原视频</div>
				<div v-if="item.classify==2" style="display: inline-block;color: #f56c6c;width: 37%;text-align: right;">
					剪辑视频</div>
				<div style="color: #999999;font-size: 10px;">视频时间：{{item.videoTime}}</div>
			</div>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChangeV" @current-change="handleCurrentChangeV"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="sizeF" :current-page="pageF"
					layout="total,sizes, prev, pager, next,jumper" :total="choicenData.total">
				</el-pagination>
			</div>
		</el-dialog>
		<!-- 桌台列表 -->
		<el-dialog title="桌台列表" :visible.sync="dialogFormVisiblezt" center width="80%">
			<div>
				<span>桌台类型：</span>
				<el-select v-model="goodsCategoryT" placeholder="请选择桌台类型" style="width:150px;" @change="selectZt()">
					<el-option v-for="item in goodsCategorymain" :key="item.value" :label="item.label"
						:value="item.value">
					</el-option>
				</el-select>&nbsp;&nbsp;&nbsp;&nbsp;
			</div>
			<el-table width="780px" v-loading="tableDataLoadingU" :data="goods_list">
				<el-table-column fixed prop="goodsId" width="80" label="编号">
				</el-table-column>
				<el-table-column prop="goodsCategory" label="桌馆类型" width="80">
					<template slot-scope="scope">
						<span style="color: #5cc477;" v-if="scope.row.goodsCategory  == 1">台球桌</span>
						<span style="color: #F56C6C;" v-if="scope.row.goodsCategory  == 2">棋牌桌</span>
						<span style="color: #13ce66;" v-if="scope.row.goodsCategory  == 3">其他桌</span>
						<span style="color: sandybrown;" v-if="scope.row.goodsCategory  == 4">柜子</span>
					</template>
				</el-table-column>
				<el-table-column fixed prop="goodsName" label="桌台名称">
				</el-table-column>
				<el-table-column prop="goodsCoverImage" label="开台前图片">
					<template slot-scope="scope">
						<img :src="scope.row.goodsCoverImage" alt="" width="60" height="60">
					</template>
				</el-table-column>
				<el-table-column prop="goodsTitleImage" label="开台后图片">
					<template slot-scope="scope">
						<img :src="scope.row.goodsTitleImage" alt="" width="60" height="60">
					</template>
				</el-table-column>
				<el-table-column prop="goodsType" label="桌台信息" width="200">
				</el-table-column>
				<el-table-column prop="iconImg" label="桌台图标">
					<template slot-scope="scope">
						<img :src="scope.row.iconImg" alt="" width="60" height="60">
					</template>
				</el-table-column>
				<el-table-column prop="hourPrice" label="小时价">
					<template slot-scope="scope">
						<span style="color: #f56c6c;">{{scope.row.hourPrice?scope.row.hourPrice:0 | numFilter}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="minutePrice" label="分钟价">
					<template slot-scope="scope">
						<span
							style="color: #f56c6c;">{{scope.row.minutePrice?scope.row.minutePrice:0 | numFilter}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="cashPledge" label="押金">
					<template slot-scope="scope">
						<span style="color: #f56c6c;">{{scope.row.cashPledge?scope.row.cashPledge:0 | numFilter}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="subscription" label="预约价格">
					<template slot-scope="scope">
						<span
							style="color: #f56c6c;">{{scope.row.subscription?scope.row.subscription:0 | numFilter}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="sales" label="台球桌销量">
					<template slot-scope="scope">
						<span>{{scope.row.sales ? scope.row.sales : '0'}}</span>
					</template>
				</el-table-column>
				<el-table-column fixed="right" label="操作" width="120">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" v-if="indexType==1&&hourPrice==Number(scope.row.hourPrice)"
							@click="confirmZt(scope.row)">确定
						</el-button>
						<el-button size="mini" type="primary" v-if="indexType==2&&goodsId!=scope.row.goodsId"
							@click="confirmZz(scope.row)">确定
						</el-button>
					</template>
				</el-table-column>
			</el-table>
		</el-dialog>
		<!-- 其他会员金额 -->
		<el-dialog title="其他会员金额" :visible.sync="dialogFormVisibleVip" center width="80%">
			<el-row>
				<div v-if="qitaData.vipList">
					<el-col :span="6" class="cards" v-for="(item,index) in qitaData.vipList" :key="index">
						<div class="box">
							<div class="box_num">
								<div class="box_color">{{item.vipName}}需支付金额</div>
								<div class="text_color"><span
										style="color: #5cc477;">{{Number(item.payMoney).toFixed(2) }}</span></div>
							</div>
						</div>
					</el-col>
				</div>
				<span>
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">点餐金额</div>
								<div class="text_color"><span
										style="color: #5cc477;">{{qitaData.tbOrder?(qitaData.tbOrder.orderMoney).toFixed(2):0}}</span>
								</div>
							</div>
						</div>
					</el-col>
				</span>
				<div v-if="qitaData.vipList">
					<div v-if="qitaData.tbOrder">
						<el-col :span="6" class="cards" v-for="(item,index) in qitaData.vipList" :key="index">
							<div class="box">
								<div class="box_num">
									<div class="box_color">{{item.vipName}}+点餐总支付金额</div>
									<div class="text_color"><span
											style="color: red;">{{(Number(item.payMoney)+Number(qitaData.tbOrder.orderMoney)).toFixed(2)}}</span>
									</div>
								</div>
							</div>
						</el-col>
					</div>
					<div v-else>
						<el-col :span="6" class="cards" v-for="(item,index) in qitaData.vipList" :key="index">
							<div class="box">
								<div class="box_num">
									<div class="box_color">{{item.vipName}}+点餐总支付金额</div>
									<div class="text_color"><span
											style="color: red;">{{Number(item.payMoney).toFixed(2)}}</span></div>
								</div>
							</div>
						</el-col>
					</div>
				</div>
			</el-row>
		</el-dialog>
		<!-- 部分退款 金额-->
		<el-dialog title="部分退款" :visible.sync="dialogFormVisibleBftk" center>

			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">退款金额：</span>
				<el-input style="width:50%;" v-model="refundMoney" type="number" min="0" placeholder="请输入退款金额">
				</el-input>
				<div style="color: red;margin-left: 200px;">* 最大可退款金额为：{{payMoney.toFixed(2)}}</div>
			</div>
			<div style="margin-bottom: 10px;">
				<span
					style="width: 200px;display: inline-block;text-align: right;position: relative;top: -70px;">备注：</span>
				<el-input style="width:50%;" v-model="content" type="textarea" :rows="4" placeholder="请输入备注">
				</el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisibleBftk = false">取 消</el-button>
				<el-button type="primary" @click="replyNoticeTk()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 续费记录 -->
		<el-dialog title="续费记录" :visible.sync="dialogFormVisibleXf" center width="80%">
			<el-table width="780px" v-loading="dialogFormVisibleVip" :data="xufeiData.list">
				<el-table-column fixed prop="id" width="80" label="编号">
				</el-table-column>
				<el-table-column prop="cashPledge" label="续费类型">
					<template slot-scope="scope">
						<span v-if="scope.row.type==1">优惠券</span>
						<span v-if="scope.row.type==2">会员卡</span>
						<span v-if="scope.row.type==3">微信</span>
					</template>
				</el-table-column>
				<el-table-column prop="title" label="标题" width="200">
				</el-table-column>
				<el-table-column prop="selfCouponUser.couponName" label="优惠券名称" width="200">
					<template slot-scope="scope">
						<span v-if="scope.row.type==1">{{scope.row.selfCouponUser?scope.row.selfCouponUser.couponName:'-'}}</span>
						<span v-else>-</span>
					</template>
				</el-table-column>
				
				<el-table-column prop="url" label="金额">
					<template slot-scope="scope">
						<span
							style="color: #f56c6c;">{{scope.row.url?scope.row.url:'-'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="createAt" label="操作时间" width="160">
				</el-table-column>
				
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChangeXf" @current-change="handleCurrentChangeXf"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="sizeU" :current-page="pageU"
					layout="total,sizes, prev, pager, next,jumper" :total="xufeiData.totalPage">
				</el-pagination>
			</div>
		</el-dialog>
		<!-- 结束订单 -->
		<el-dialog title="结束订单" :visible.sync="dialogFormVisibleJsdd" center width="80%">
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">折扣比例：</span>
				<el-radio-group  v-model="shopDiscount">
					<el-radio :label="1">不打折</el-radio>
					<el-radio :label="0.9">九折</el-radio>
					<el-radio :label="0.8">八折</el-radio>
					<el-radio :label="0.7">七折</el-radio>
					<el-radio :label="0.6">六折</el-radio>
					<el-radio :label="0.5">五折</el-radio>
					<el-radio :label="0.4">四折</el-radio>
					<el-radio :label="0.3">三折</el-radio>
					<el-radio :label="0.2">二折</el-radio>
					<el-radio :label="0.1">一折</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">支付金额：</span>
				<span style="color: red;">{{(Number(payMoneys)*shopDiscount).toFixed(2)}}</span> 
				<span>(原价：{{payMoneys}})</span>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">支付方式：</span>
				<el-radio-group  v-model="payWay">
					<el-radio :label="1">微信</el-radio>
					<el-radio :label="2">现金</el-radio>
					<el-radio :label="3">门店会员</el-radio>
					<el-radio :label="4">美团核销</el-radio>
					<el-radio :label="5">快手核销</el-radio>
					<el-radio :label="6">抖音核销</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 10px;">
				<span
					style="width: 200px;display: inline-block;text-align: right;position: relative;top: -70px;">备注：</span>
				<el-input style="width:50%;" v-model="content" type="textarea" :rows="4" placeholder="请输入备注">
				</el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisibleJsdd = false">取 消</el-button>
				<el-button type="primary" @click="replyNoticeJsdd()">确 定</el-button>
			</div>
		</el-dialog>
		
		<!-- 结束订单 -->
		<el-dialog title="挂账" :visible.sync="dialogFormVisibleGz" center width="80%">
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">选择桌台：</span>
				<el-input v-model="ordersGoodsName" @focus="Goodsselect()" style="width:50%;" class="margin15"
					placeholder="请选择选择桌台">
				</el-input>
			</div>
			
			
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisibleGz = false">取 消</el-button>
				<el-button type="primary" @click="replyNoticeGz()">确 定</el-button>
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
				state: 2,
				status: '',
				userName: '',
				ordersUserName: '',
				type: '',
				id: '',
				statesnum: [{
						value: '',
						label: '全部'
					},
					{
						value: 1,
						label: '待支付'
					},
					{
						value: 2,
						label: '预约中'
					},
					{
						value: 3,
						label: '使用中'
					},
					{
						value: 4,
						label: '已完成'
					},
					{
						value: 5,
						label: '已取消'
					},
					{
						value: 6,
						label: '待评价'
					},
					{
						value: 7,
						label: '待补费'
					},
					// {
					// 	value: 8,
					// 	label: '已失效'
					// }
				],
				typeNum: [{
						value: '',
						label: '全部'
					},
					{
						value: 1,
						label: '微信'
					},
					{
						value: 2,
						label: '支付宝'
					},
					{
						value: 3,
						label: '零钱'
					},
				],
				moneyT: '',
				typeNum2: [{
						value: '',
						label: '全部'
					},
					{
						value: 1,
						label: '是'
					},
					{
						value: 0,
						label: '否'
					},
				],
				platformNum:[
					{
							value: '',
							label: '全部'
						},
						{
							value: 1,
							label: '美团'
						},
						{
							value: 2,
							label: '抖音'
						},
						{
							value: 3,
							label: '快手'
						},
				],
				form: {
					id: '',
					title: '',
					url: '',
					type: '',
					state: '',
					types: [{
							value: 'word',
							label: '文字'
						},
						{
							value: 'url',
							label: '链接'
						}
					],
				},
				phone: '',
				content: '',
				startTime: '',
				endTime: '',
				flag: '',
				flags: [{
						value: 1,
						label: '单个用户'
					},
					{
						value: 2,
						label: '全部用户'
					}
				],
				formLabelWidth: '200px',
				activeName: 'first',
				tableDataLoading: false,
				dialogFormVisible1: false,
				dialogFormVisible2: false,
				dialogFormVisible: false,
				tableData: [],
				totalMoney: 0,
				totalCashPledge: 0,
				totalMinuteMoney: 0,
				totalVipMoney:0,
				info: {
					stockDate: this.getNowTime(), //日期
				},
				info2: {
					stockDate2: this.getNowTime2(), //日期
				},
				startTime: '',
				endTime: '',
				ordersType: '',
				ordersNoT: '',
				orderTypeT: '',
				tearoomNameT:'',
				platformTypeT:'',
				// 新增订单数据
				dialogFormVisibleO: false,
				dialogFormVisibleU: false,
				tableDataLoadingU: false,

				nickName2: '',
				inviterCode: '',
				phone1: '',

				userIds: {}, //用户列表
				sizeU: 10,
				pageU: 1,

				dialogFormVisibleF: false,
				tableDataLoadingF: false,
				classify: '',
				classifyList: [{
						id: '',
						name: '全部'
					},
					{
						id: 1,
						name: '线上'
					},
					{
						id: 2,
						name: '线下'
					}
				],
				classifyIds: '',

				search: '',
				statusId: 0,
				statuss: [{
						id: '',
						title: '全部'
					},
					{
						id: 0,
						title: '进行中'
					},
					{
						id: 1,
						title: '待审核'
					},
					{
						id: 2,
						title: '已下架'
					},
					{
						id: 3,
						title: '拒绝'
					}
				],
				sizeF: 10,
				pageF: 1,
				choicenData: {},
				orderType: 1,
				ordersNo: '',

				fuwuName: '',
				orderNumber: 1,
				payMoney: 0,
				orderTakingMoney: 0,
				orderTakingId: '',
				remarks: '',
				isVip: '',
				userIdss: '',
				nickName1: '',
				isVipT: -1,
				isVipType: [{
						value: -1,
						label: '全部'
					},
					{
						value: 1,
						label: '是'
					},
					{
						value: 0,
						label: '否'
					},
				],

				dialogFormVisibleXd: false,
				times: '',
				is_busy: 0,
				timesList: [{
						roomTime: '00:00',
						type: 0
					}, {
						roomTime: '01:00',
						type: 0
					}, {
						roomTime: '02:00',
						type: 0
					}, {
						roomTime: '03:00',
						type: 0
					},
					{
						roomTime: '04:00',
						type: 0
					}, {
						roomTime: '05:00',
						type: 0
					}, {
						roomTime: '06:00',
						type: 0
					}, {
						roomTime: '07:00',
						type: 0
					},
					{
						roomTime: '08:00',
						type: 0
					}, {
						roomTime: '09:00',
						type: 0
					}, {
						roomTime: '10:00',
						type: 0
					}, {
						roomTime: '11:00',
						type: 0
					},
					{
						roomTime: '12:00',
						type: 0
					}, {
						roomTime: '13:00',
						type: 0
					}, {
						roomTime: '14:00',
						type: 0
					}, {
						roomTime: '15:00',
						type: 0
					},
					{
						roomTime: '16:00',
						type: 0
					}, {
						roomTime: '17:00',
						type: 0
					}, {
						roomTime: '18:00',
						type: 0
					}, {
						roomTime: '19:00',
						type: 0
					},
					{
						roomTime: '20:00',
						type: 0
					}, {
						roomTime: '21:00',
						type: 0
					}, {
						roomTime: '22:00',
						type: 0
					}, {
						roomTime: '23:00',
						type: 0
					},

				],
				xudanTime: '',
				year_time: '',
				orderId: '',
				end_time: '',
				xudanList: [],
				tableDataLoadingX: false,
				dialogFormVisibleXdL: false,
				dialogFormVisibleBz: false,
				dialogFormVisibleVido: false,
				classifyT: '',
				homeData1: [{
						id: '',
						name: '全部'
					},
					{
						id: 1,
						name: '原视频'
					},
					{
						id: 2,
						name: '剪辑视频'
					},
				],
				dialogFormVisiblezt: false,
				tearoomId: '',
				goods_list: [],
				hourPrice: '',
				dialogFormVisibleVip: false,
				ordersId: '',
				qitaData: {},
				xufeiData:{},
				dialogFormVisibleBftk: false,
				refundMoney: 0,
				payMoney: 0,
				remark: '',
				dialogFormVisibleXf:false,
				goodsCategory: '', //类型 1台球 2棋牌 3其他桌 4柜子
				goodsCategoryListP: [{ // 1台球 2棋牌 3其他桌 4柜子
					value: '',
					label: '全部'
				}, {
					value: 1,
					label: '台球'
				}, {
					value: 2,
					label: '棋牌'
				}, {
					value: 3,
					label: '其他桌'
				}, {
					value: 4,
					label: '柜子'
				}, ],
				categoryListP:[
					{
						value: '',
						label: '全部'
					}, {
						value: 1,
						label: '商家开台'
					}, {
						value: 2,
						label: '用户开台'
					},
				],
				isShopT:'',
				dialogFormVisibleJsdd:false,
				shopDiscount:1,
				payMoneys:'',
				payWay:'',
				dialogFormVisibleGz:false,
				ordersGoodsId:'',
				ordersGoodsName:'',
				indexType:1,
				goodsId:'',
				goodsCategoryT: '',
				goodsCategorymain: [{
						value: '',
						label: '全部'
					},
					{
						value: 1,
						label: '台球桌'
					},
					{
						value: 2,
						label: '棋牌桌'
					},
					{
						value: 3,
						label: '其他桌'
					},
					{
						value: 4,
						label: '柜子'
					}
				],

			}
		},
		filters: {
			numFilter(value) {
				let realVal = ''
				if (!isNaN(value) && value !== '') {
					// 截取当前数据到小数点后两位
					realVal = parseFloat(value).toFixed(2)
				} else {
					realVal = '--'
				}
				return realVal
			},
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
			// 详情跳转
			updataDetails(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
					}
				});
			},
			handleSizeChange(val) {
				this.limit = val;
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.dataSelect()
			},
			handleSizeChange2(val) {
				this.sizeU = val
				this.userClass()
			},
			handleCurrentChange2(val) {
				this.pageU = val
				this.userClass()
			},
			handleSizeChange1(val) {
				this.sizeF = val
				this.InformationSelect()
			},
			handleCurrentChange1(val) {
				this.pageF = val
				this.InformationSelect()
			},
			handleClick(tab, event) {
				if (tab._props.label == '全部订单') {
					this.page = 1
					this.limit = 10
					this.ordersType = 1
					this.orderTypeT = ''
					this.moneyT = ''
					this.tearoomNameT = ''
					this.platformTypeT = ''
					this.dataSelect()
				}

			},
			// 消息推送
			magNotice() {
				this.dialogFormVisible2 = true
			},

			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.totalMoney = 0
				this.totalCashPledge = 0
				this.totalMinuteMoney = 0
				this.totalVipMoney = 0

				this.$http({
					url: this.$http.adornUrl('orders/selectOrdersList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'payWay': this.orderTypeT,
						'payPhone': this.ordersType,
						'payUserName': this.userName,
						'goodsName': this.ordersUserName,
						'ordersNo': this.ordersNoT,
						'status': this.status,
						'startTime': this.startTime,
						'endTime': this.endTime,
						'money': this.moneyT,
						'goodsCategory':this.goodsCategory,
						'tearoomName':this.tearoomNameT,
						'platformType':this.platformTypeT,
						'isShop':this.isShopT,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					for (var i in data.data.list) {
						if (data.data.list[i].payMoney) {
							this.totalMoney = this.totalMoney + data.data.list[i].payMoney
						}
						if (data.data.list[i].cashPledge) {
							this.totalCashPledge = this.totalCashPledge + data.data.list[i].cashPledge
						}
						if (data.data.list[i].minuteMoney) {
							this.totalMinuteMoney = this.totalMinuteMoney + data.data.list[i].minuteMoney
						}
						if (data.data.list[i].couponMoney) {
							this.totalVipMoney = this.totalVipMoney + data.data.list[i].couponMoney
						}
						if(data.data.list[i].refundMoney){
							data.data.list[i].refundMoneySf = Number(data.data.list[i].payMoney) -  Number(data.data.list[i].refundMoney)
						}else{
							data.data.list[i].refundMoneySf = Number(data.data.list[i].payMoney)
						}
						

					}
					let returnData = data.data;
					this.tableData = returnData

				})
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
			// 查询
			select() {
				this.page = 1
				this.limit = 10
				this.dataSelect()
			},
			// 重置
			cleans() {
				this.page = 1
				this.userName = ''
				this.ordersType = ''
				this.ordersUserName = ''
				this.ordersNoT = ''
				this.status = ''
				this.orderTypeT = ''
				this.startTime = ''
				this.endTime = ''
				this.moneyT = ''
				this.goodsCategory = ''
				this.tearoomNameT = ''
				this.platformTypeT = ''
				this.isShopT = ''
				this.dataSelect()
			},
			// select选择事件
			animeDat(state) {
				this.page = 1
				this.dataSelect()
			},
			// 删除订单
			deletes(row) {
				let delid = row.ordersId
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`orders/deleteOrders?id=${delid}`),
						method: 'post',
						params: this.$http.adornData({})
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
				})
			},
			cancelOrder(row, status) {
				let delid = row.ordersId
				let content = ''
				if (status == 2) {
					content = "确定完成此订单？"
				} else {
					content = "确定退款此订单？"
				}
				this.$confirm(content, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`orders/cancelOrder?id=${delid}&status=${status}`),
						method: 'get',
						params: this.$http.adornParams({})
					}).then(({
						data
					}) => {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					})
				})
			},
			// 导出
			exportBtn() {
				this.$http({
					url: this.$http.adornUrl('orders/excelOrders'),
					method: 'get',
					responseType: 'blob',
					params: this.$http.adornParams({
						// 'page': page,
						// 'size': this.size,
						'payWay': this.orderTypeT,
						'payPhone': this.ordersType,
						'payUserName': this.userName,
						'goodsName': this.ordersUserName,
						'ordersNo': this.ordersNoT,
						'status': this.status,
						'startTime': this.startTime,
						'endTime': this.endTime,
						'money': this.moneyT,
						'goodsCategory':this.goodsCategory,
						'tearoomName':this.tearoomNameT,
						'platformType':this.platformTypeT,
						'isShop':this.isShopT,
					})
				}).then(({
					data
				}) => {
					let blob = new Blob([data], {
						type: 'application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
					})
					if (window.navigator.msSaveOrOpenBlob) {
						navigator.msSaveBlob(blob)
					} else {
						let url = window.URL.createObjectURL(blob)
						let elink = document.createElement('a')
						elink.download = '订单列表.xlsx'
						elink.style.display = 'none'
						elink.href = url
						document.body.appendChild(elink)
						elink.click()
						document.body.removeChild(elink)
					}
				})
			},
			// 新增订单弹框
			orderAdd() {
				this.fuwuName = ''
				this.orderNumber = 1
				this.payMoney = 0
				this.orderTakingMoney = 0
				this.orderTakingId = ''
				this.remarks = ''
				this.isVip = ''
				this.userIdss = ''
				this.nickName1 = ''
				this.dialogFormVisibleO = true
			},
			// 获取用户列表弹框
			userselect(row) {
				this.ordersId = row.ordersId
				this.phone1 = ''
				this.nickName2 = ''
				this.inviterCode = ''
				this.isVipT = -1
				this.isVip = ''
				this.pageU = 1
				this.dialogFormVisibleU = true

				this.userClass()
			},
			// 获取用户列表
			userClass() {

				this.tableDataLoadingU = true
				this.$http({
					url: this.$http.adornUrl('user/selectUserList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.pageU,
						'limit': this.sizeU,
						'member': this.isVipT,
						'userName': this.nickName2,
						'inviterCode': this.inviterCode,
						'startTime': '',
						'endTime': '',
						'isAuthentication': -1,
						'phone': this.phone1
					})
				}).then(({
					data
				}) => {
					this.tableDataLoadingU = false
					let returnData = data.data
					this.userIds = returnData
				})
			},
			// 查询用户列表
			userclick() {
				this.pageU = 1
				this.userClass()
			},
			// 重置用户列表数据
			userclose() {
				this.phone1 = ''
				this.nickName2 = ''
				this.inviterCode = ''
				this.isVipT = -1
				this.isVip = ''
				this.pageU = 1
				this.userClass()
			},
			// 确定用户
			confirm(row) {

				this.$http({
					url: this.$http.adornUrl('orders/updateOrders'),
					method: 'post',
					// params: this.$http.adornParams({
					data: this.$http.adornData({
						'ordersId': this.ordersId,
						'userId': row.userId,
						'userNam': row.userName,
						'phone': row.phone
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
						this.dialogFormVisibleU = false
					} else {
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {}
						})
					}

				})
				// this.userIdss = row.userId
				// this.nickName1 = row.userName
				// if (row.isVip == 1) {
				// 	this.isVip = 1
				// } else {
				// 	this.isVip = ''
				// }
				// this.orderNumber = 1
				// this.payMoney = 0
				// this.orderTakingMoney = 0
				// this.orderTakingId = ''
				// this.remarks = ''
				// this.dialogFormVisibleU = false
			},
			// 服务弹框
			fuwuselect() {
				this.status = ''
				this.statusId = 0
				this.classify = ''
				this.search = ''
				this.classifyIds = ''
				this.pageF = 1
				this.homeSelect()
				this.InformationSelect()
				this.dialogFormVisibleF = true
			},
			// 获取游戏分类
			homeSelect() {
				this.$http({
					url: this.$http.adornUrl(`game/queryGame`),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					let returnData = data.data
					this.homeData1 = [{
						id: '',
						gameName: '全部'
					}]
					if (data.data) {
						for (var i in data.data) {
							this.homeData1.push(data.data[i])
							data.data[i].state = Number(data.data[i].state)
						}
					}
					this.homeData = data.data
				})
			},
			// 信息数据
			InformationSelect() {
				this.tableDataLoadingF = true
				this.$http({
					url: this.$http.adornUrl(`orderTaking/queryAllOrderTaking`),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.pageF,
						'limit': this.sizeF,
						'userName': this.search,
						'gameId': this.classifyIds,
						'status': this.statusId,
						'classify': this.classify,

					})
				}).then(({
					data
				}) => {
					this.tableDataLoadingF = false
					let returnData = data.data
					this.choicenData = returnData

				})
			},
			// 查询
			phoneSelect() {
				this.pageF = 1
				this.InformationSelect()
			},
			// 重置
			cleans2() {
				this.status = ''
				this.statusId = 0
				this.classify = ''
				this.search = ''
				this.classifyIds = ''
				this.pageF = 1
				this.InformationSelect()
			},
			// 选择服务
			choiCompile(row) {
				this.orderTakingId = row.id
				this.fuwuName = row.gameName + '/' + row.myLevel
				if (this.isVip == 1) {
					this.orderTakingMoney = row.memberMoney
					this.payMoney = row.memberMoney
				} else {
					this.orderTakingMoney = row.money
					this.payMoney = row.money
				}

				this.dialogFormVisibleF = false
			},
			// 修改数量
			orderNumberBtn() {
				if (this.orderNumber === '' || this.orderNumber == null) {

					this.$notify({
						title: '提示',
						duration: 1800,
						message: '数量不能为空',
						type: 'warning'
					});
					return
				}
				this.payMoney = Number(this.orderTakingMoney) * this.orderNumber
			},
			// 确认添加
			addmissionNoticeTo() {
				if (this.userIdss === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择下单人',
						type: 'warning'
					});
					return
				}
				if (this.orderTakingId === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择下单服务',
						type: 'warning'
					});
					return
				}
				if (this.orderNumber === '' || this.orderNumber == null) {

					this.$notify({
						title: '提示',
						duration: 1800,
						message: '数量不能为空',
						type: 'warning'
					});
					return
				}

				this.$http({
					url: this.$http.adornUrl('orders/insertOrders'),
					method: 'post',
					// params: this.$http.adornParams({
					data: this.$http.adornData({
						'orderTakingId': this.orderTakingId,
						'orderTakingMoney': this.orderTakingMoney,
						'userId': this.userIdss,
						'payMoney': this.payMoney,
						'remarks': this.remarks,
						'orderNumber': this.orderNumber,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							message: '添加成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
								this.dialogFormVisibleO = false
							}
						})
					} else {
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
			// 详情
			orderDetails(id) {
				this.$router.push({
					path: '/orderDetails',
					query: {
						ordersNo: id
					}
				})
			},
			// 退全款 
			refundClick(row) {
				if(row.refundMoney){
					var payMoney = Number(row.payMoney) -  Number(row.refundMoney) 
				}else{
					var payMoney = Number(row.payMoney) 
				}
				this.$confirm(`确定退全款?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('orders/refundOrdersMoney'),
						method: 'post',
						params: this.$http.adornParams({
							'ordersNo': row.ordersNo,
							'refundMoney': payMoney
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '退款成功',
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
								onClose: () => {}
							})
						}
					})
				}).catch(() => {})
			},
			// 部分退款
			refundClickBf(row) {
				this.refundMoney = ''
				if(row.refundMoney){
					this.payMoney = Number(row.payMoney) -  Number(row.refundMoney) 
				}else{
					this.payMoney = Number(row.payMoney) 
				}
				this.ordersNo = row.ordersNo
				this.content = ''
				this.dialogFormVisibleBftk = true

			},
			// 确认 部分退款
			replyNoticeTk() {
				if (this.refundMoney === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入退款金额',
						type: 'warning'
					});
					return
				}
				if (Number(this.refundMoney) <= 0) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '退款金额必须大于0',
						type: 'warning'
					});
					return
				}
				if (Number(this.refundMoney) > Number(this.payMoney)) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '退款金额不能大于支付金额',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl('orders/refundOrdersMoney'),
					method: 'post',
					params: this.$http.adornParams({
						'ordersNo': this.ordersNo,
						'refundMoney': this.refundMoney,
						'refundRemark': this.content,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							message: '退款成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dialogFormVisibleBftk = false
								this.dataSelect()
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'error',
							duration: 1500,
							onClose: () => {}
						})
					}
				})
			},
			// 结束订单
			jieshuClick(row) {
				this.$confirm(`确定要结束订单?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('orders/shopCloseOrders'),
						method: 'post',
						params: this.$http.adornParams({
							'ordersId': row.ordersId
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
						} else {
							this.$message({
								message: data.msg,
								type: 'error',
								duration: 1500,
								onClose: () => {}
							})
						}
					})
				}).catch(() => {})
			},
			// 续单弹框
			contactxd(row) {
				this.xudanTime = this.getNowTime()
				this.ordersId = row.goodsId
				this.orderId = row.ordersNo
				this.year_time = ''
				this.contactTime()
				this.dialogFormVisibleXd = true
			},
			// 获取续单时间  api/index/order/time_manage
			contactTime() {
				this.$http({
					url: this.$http.adornUrl('roomTime/selectRoomTimeByDate'),
					method: "get",
					params: this.$http.adornParams({
						'goodsId': this.ordersId,
						'date': this.xudanTime,
					}),
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						if (data.data.length > 0) {
							for (var i in data.data) {
								for (var j in this.timesList) {
									if (data.data[i].roomTime == this.timesList[j].roomTime) {
										this.timesList[j].type = 1
									}
								}
							}
						} else {
							this.timesList = [{
									roomTime: '00:00',
									type: 0
								}, {
									roomTime: '01:00',
									type: 0
								}, {
									roomTime: '02:00',
									type: 0
								}, {
									roomTime: '03:00',
									type: 0
								},
								{
									roomTime: '04:00',
									type: 0
								}, {
									roomTime: '05:00',
									type: 0
								}, {
									roomTime: '06:00',
									type: 0
								}, {
									roomTime: '07:00',
									type: 0
								},
								{
									roomTime: '08:00',
									type: 0
								}, {
									roomTime: '09:00',
									type: 0
								}, {
									roomTime: '10:00',
									type: 0
								}, {
									roomTime: '11:00',
									type: 0
								},
								{
									roomTime: '12:00',
									type: 0
								}, {
									roomTime: '13:00',
									type: 0
								}, {
									roomTime: '14:00',
									type: 0
								}, {
									roomTime: '15:00',
									type: 0
								},
								{
									roomTime: '16:00',
									type: 0
								}, {
									roomTime: '17:00',
									type: 0
								}, {
									roomTime: '18:00',
									type: 0
								}, {
									roomTime: '19:00',
									type: 0
								},
								{
									roomTime: '20:00',
									type: 0
								}, {
									roomTime: '21:00',
									type: 0
								}, {
									roomTime: '22:00',
									type: 0
								}, {
									roomTime: '23:00',
									type: 0
								},

							]
						}
						// this.timesList = data.data
						console.log('this.timesList', this.timesList)
					}

				});
			},
			// 选择续单日期
			xdDatas() {
				console.log('this.getNowTime()', this.getNowTime())
				console.log('this.xudanTime', this.xudanTime)
				if (this.xudanTime == this.getNowTime()) {
					this.year_time = ''
				} else {
					this.year_time = this.xudanTime
				}

				this.contactTime()
			},
			// 确认续单
			xudanNoticeTo() {
				// if (this.xudanTime == '' || this.xudanTime == this.getNowTime()) {
				// 	var times = this.xudanTime
				// } else {
				var times = this.xudanTime + ' ' + this.times
				// }
				this.$http({
					url: this.$http.adornUrl('orders/payAddOrders'),
					method: "post",
					params: this.$http.adornParams({
						'ordersNo': this.orderId,
						'endTime': times,
					}),
				}).then(({
					data
				}) => {

					if (data.code == 0) {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dialogFormVisibleXd = false
								this.dataSelect()
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'success',
							duration: 1500,
							onClose: () => {}
						})
					}

				});
			},
			// 续单列表
			orderXd(row) {
				this.xudanList = row.extend_time_list
				this.dialogFormVisibleXdL = true
			},
			// 修改备注、时间
			updateBz(row) {


				this.ordersId = row.ordersId
				// this.startTime = row.startTime
				// this.endTime = row.endTime

				this.content = Math.abs(row.sumMoney)
				this.dialogFormVisibleBz = true
			},
			// 修改
			replyNoticeTo() {
				if (this.content === '') {

					this.$notify({
						title: '提示',
						duration: 1800,
						message: '补费金额不能为空',
						type: 'warning'
					});
					return
				}
				if (Number(this.content) < 0) {

					this.$notify({
						title: '提示',
						duration: 1800,
						message: '补费金额不能小于0',
						type: 'warning'
					});
					return
				}
				var sumMoney = -this.content
				this.$http({ //updateOrdersTime
					url: this.$http.adornUrl('orders/updateOrders'),
					method: 'post',
					// params: this.$http.adornParams({
					data: this.$http.adornData({
						// 'ordersNo': this.ordersId,
						'ordersId': this.ordersId,
						'sumMoney': sumMoney,
						// 'startTime': this.startTime,
						// 'remark': this.content,
						// 'endTime': this.endTime,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							message: '修改成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
						this.dialogFormVisibleBz = false
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
			// 补费
			bufeiClick(row) {

				this.$confirm('补费金额为：' + Math.abs(row.sumMoney) + ',确定要补费?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('orders/updateOrders'),
						method: 'post',
						// params: this.$http.adornParams({
						data: this.$http.adornData({
							'ordersId': row.ordersId,
							'status': 4,
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
						} else {
							this.$message({
								message: data.msg,
								type: 'error',
								duration: 1500,
								onClose: () => {}
							})
						}
					})
				}).catch(() => {})
			},
			// 视频列表弹框
			vidoClick(row) {
				this.ordersId = row.ordersId
				this.pageF = 1
				this.classifyT = ''
				this.VideoSelect()
				this.dialogFormVisibleVido = true
			},
			// 视频列表弹框
			vidoSx() {
				this.pageF = 1

				this.VideoSelect()
			},
			VideoSelect() {
				this.tableDataLoadingF = true
				this.$http({
					url: this.$http.adornUrl('ordersVideo/selectOrdersVideoList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.pageF,
						'limit': this.sizeF,
						'userName': this.userName,
						'ordersId': this.ordersId,
						'classify': this.classifyT,
						// 'status': this.statusId,
						// 'classify': this.classify
					})
				}).then(({
					data
				}) => {
					this.tableDataLoadingF = false
					let returnData = data.data
					this.choicenData = returnData

				})
			},
			handleCurrentChangeV(val) {
				this.pageF = val;
				this.VideoSelect()
			},
			handleSizeChangeV(val) {
				this.sizeF = val
				this.VideoSelect()
			},
			// 挂账
			huantaiClick(row) {
				this.indexType = 1
				this.hourPrice = Number(row.hourMoney)
				this.ordersId = row.ordersId
				this.tearoomId = row.tearoomId
				
				this.zhuotaiSelect()
				this.dialogFormVisiblezt = true
			},
			selectZt() {
				
				this.zhuotaiSelect()
			},
			// 获取 桌台列表
			zhuotaiSelect() {
				this.tableDataLoadingU = true
				this.$http({
					url: this.$http.adornUrl('goods/selectGoodsList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': 1,
						'limit': 100,
						'goodsName': '',
						'status': '',
						'tearoomId': this.tearoomId,
						'goodsCategory': this.goodsCategoryT,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoadingU = false
					let returnData = data.data;
					this.goods_list = returnData.list
				})
			},
			confirmZt(row) {
				this.$http({
					url: this.$http.adornUrl('orders/roomChange'),
					method: 'post',
					params: this.$http.adornParams({
						// data: this.$http.adornData({
						'ordersId': this.ordersId,
						'goodsId': row.goodsId,
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
						this.dialogFormVisiblezt = false
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
			moneyClick(row) {
				this.$http({
					url: this.$http.adornUrl('orders/selectOrdersDetailsVip'),
					method: 'get',
					params: this.$http.adornParams({
						// data: this.$http.adornData({
						'ordersNo': row.ordersNo,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.qitaData = data.data
						this.dialogFormVisibleVip = true
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
			// 续费记录 弹框
			xufeiClick(row){
				this.ordersNo = row.ordersNo
				this.xufeiSelect()
				this.dialogFormVisibleXf = true
			},
			xufeiSelect() {
				this.dialogFormVisibleVip = true
				this.$http({
					url: this.$http.adornUrl('message/selectMessageByType'),
					method: 'get',
					params: this.$http.adornParams({
						// data: this.$http.adornData({
						'page':this.pageU,
						'limit':this.sizeU,
						'content': this.ordersNo,
						'state':400,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.xufeiData = data.data
						this.dialogFormVisibleVip = false
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
			handleSizeChangeXf(val) {
				this.sizeU = val
				this.xufeiSelect()
			},
			handleCurrentChangeXf(val) {
				this.pageU = val
				this.xufeiSelect()
			},
			// 商家开台 结束订单
			jieshuClickSj(row){
				this.shopDiscount = 1
				this.payMoneys = row.payMoney
				this.payWay = ''
				this.content = row.userRemark
				this.ordersId = row.ordersId
				this.dialogFormVisibleJsdd = true
			},
			// 结束订单
			replyNoticeJsdd(row) {
				this.$confirm(`确定要结束订单?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('orders/shopCloseOrders'),
						method: 'post',
						params: this.$http.adornParams({
							'ordersId': this.ordersId,
							'shopDiscount': this.shopDiscount,
							'payWay': this.payWay,
							'userRemark':this.content,
							
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
									this.dialogFormVisibleJsdd = false
									this.dataSelect()
								}
							})
						} else {
							this.$message({
								message: data.msg,
								type: 'error',
								duration: 1500,
								onClose: () => {}
							})
						}
					})
				}).catch(() => {})
			},
			// 挂账 弹框
			jieshuClickGz(row){
				this.ordersGoodsId = ''
				this.tearoomId = row.tearoomId
				this.ordersId = row.ordersId
				this.goodsId = row.goodsId
				this.ordersGoodsName = ''
				this.dialogFormVisibleGz = true
			},
			// 换台
			Goodsselect() {
				this.indexType = 2
				this.goodsCategoryT = ''
				this.zhuotaiSelect()
				this.dialogFormVisiblezt = true
			},
			confirmZz(row){
				this.ordersGoodsId = row.goodsId
				this.ordersGoodsName = row.goodsName
				this.dialogFormVisiblezt = false
			},
			// 挂账订单
			replyNoticeGz(row) {
				this.$confirm(`确定要挂账订单?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('orders/shopUpdateOrdersStatus'),
						method: 'post',
						params: this.$http.adornParams({
							'ordersId': this.ordersId,
							'ordersGoodsId': this.ordersGoodsId,
							
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
									this.dialogFormVisibleGz = false
									this.dataSelect()
								}
							})
						} else {
							this.$message({
								message: data.msg,
								type: 'error',
								duration: 1500,
								onClose: () => {}
							})
						}
					})
				}).catch(() => {})
			},
		},
		mounted() {
			this.dataSelect()
		}
	};
</script>

<style scoped>
.order-center-page__filter-select {
	width: 150px;
}

.order-center-page__filter-input {
	width: 150px;
}

.order-center-page__filter-input--wide {
	width: 200px;
}

.order-center-page__filter-date {
	width: 160px;
}

.order-center-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.order-center-page__tabs {
	margin-bottom: 0;
}

.order-center-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.order-center-page__tabs >>> .el-tabs__item.is-active {
	color: #5cc477;
}

.order-center-page__tabs >>> .el-tabs__active-bar {
	background-color: #5cc477;
}

.order-center-page__tabs .nf-filter-card {
	margin-top: 12px;
}

.order-center-page__tabs .nf-table-card {
	margin-top: 12px;
	margin-bottom: 0;
}

.order-center-page__summary {
	margin-top: 12px;
	font-size: 14px;
	color: #606266;
	line-height: 1.6;
}

.order-center-page__summary-num {
	font-size: 16px;
	font-weight: 600;
	color: #5cc477;
}

.order-center-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

.box {
	padding: 44px;
	border: 1px solid #e8eaed;
	margin: 15px 10px;
	border-radius: 6px;
}

.box_num {
	font-size: 14px;
	color: #5cc477;
}

.box_num .box_color {
	color: #333;
	font-size: 14px;
	margin-bottom: 15px;
}

.box_num div span {
	font-size: 20px;
	margin-left: 5px;
}

.text_color {
	color: #5cc477;
}

.text_color span {
	margin-right: 5px;
}
</style>