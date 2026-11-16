<template>
	<div class="nf-page user-detail-page">
		<a class="user-detail-page__back" href="#" @click.prevent="prev">
			<icon-svg name="jiantou" class="user-detail-page__back-icon"></icon-svg>
			返回
		</a>

		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">用户管理</span>
			<h2 class="nf-page-hero__title">用户详情</h2>
			<p class="nf-page-hero__desc">查看用户基本信息、余额与会员数据，并管理消息、提现、钱包、订单及相关明细记录。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<el-tabs v-model="activeName" @tab-click="handleClick" class="user-detail-page__tabs">
			<el-tab-pane label="用户详情" name="first">
				<div class="nf-table-card user-detail-page__detail-card">
				<div class="detailtabel">
					<div class="table_main">
						<table>
							<tbody>
								<tr>
									<th>已邀请人数</th>
									<td>{{userEntity.count?userEntity.count:0}}人</td>
									<th>本月支付金额</th>
									<td>{{userEntity.consume ? `${userEntity.consume}元` : '0元'}}</td>
									<th>本月提现金额</th>
									<td class="border-rt">
										{{userEntity.income ? `${userEntity.income}元` : '0元'}}
									</td>
								</tr>
								<tr>
									</td>
									<th>用户余额</th>
									<td style="color: rgb(245, 108, 108)">
										{{userEntity.money ? `${userEntity.money}元` : '0元'}}
										<el-button size="mini" :disabled="!isAuth('userList:updatejf')"
											@click="rechargenone(1)">修改金额
										</el-button>
									</td>
									<th>商家收益</th>
									<td>{{userEntity.shopMoney? userEntity.shopMoney+'元' : '0元'}}</td>
									<th>累计会员值</th>
									<td class="border-rt">{{huiyuanzhiData.sumMoney? huiyuanzhiData.money : '0'}}
									</td>
									<!-- <th>分销收益</th>
									<td class="border-rt">
										{{userEntity.inviteMoney}}
										<el-button size="mini" :disabled="!isAuth('userList:updatejf')"
											@click="rechargenone(2)">修改金额
										</el-button>
									</td> -->
								</tr>
								<!-- <tr>
									<th>有效会员值</th>
									<td>
										{{huiyuanzhiData.money ? huiyuanzhiData.money : '0'}}
									</td>
									<th>保证金</th>
									<td class="border-rt">
										{{ userEntity.cashDeposit ? `${userEntity.cashDeposit}元` : '0元' }}
										<el-button size="mini" :disabled="!isAuth('userList:updatejf')"
											@click="rechargenoneB(userEntity.cashDeposit,2)">修改金额</el-button>
									</td>
								</tr> -->
								<tr>
									<th>积分</th>
									<td>
										{{jifenData.integralNum ? jifenData.integralNum : '0'}}
										<el-button size="mini" :disabled="!isAuth('userList:updateStatus')"
											@click="ChangeuserUpdate(jifenData,7)">修改</el-button>
									</td>
									<th>微信名称</th>
									<td>{{tableData.userName}}
										<el-button size="mini" :disabled="!isAuth('userList:updateStatus')"
											@click="ChangeuserUpdate(tableData,3)">修改</el-button>
									</td>
									<th>用户编号</th>
									<td class="border-rt">{{tableData.userId}}</td>
								</tr>
								<tr>
									<th>支付宝账号</th>
									<td>{{tableData.zhiFuBao ? tableData.zhiFuBao : '未绑定'}}</td>
									<th>支付宝姓名</th>
									<td>{{tableData.zhiFuBaoName ? tableData.zhiFuBaoName : '未绑定'}}</td>
									<th>用户状态</th>
									<td class="border-rt">
										<span v-if="tableData.status == '1'">正常</span>
										<span v-else style="color: #f56c6c;">禁用</span>
										<el-button size="mini" :disabled="!isAuth('userList:updateStatus')"
											@click="stateChange(tableData.userId,tableData.status)">更改状态</el-button>
									</td>
								</tr>
								<tr>
									<th>创建时间</th>
									<td>{{tableData.createTime}}</td>
									<th>手机号</th>
									<td>{{tableData.phone ? tableData.phone : '未绑定'}}
										<el-button size="mini" :disabled="!isAuth('userList:update')"
											@click="ChangeuserUpdate(tableData,4)">修改</el-button>
									</td>
									<th>图像</th>
									<td class="border-rt"><img
											:src="tableData.avatar ? tableData.avatar : 'https://www.gomyorder.cn/logo.png'"
											width="80" height="80" />
										<el-button size="mini" :disabled="!isAuth('userList:updateStatus')"
											@click="ChangeuserUpdate(tableData,5)">修改</el-button>
									</td>
								</tr>
								<tr>
									<th>性别</th>
									<td>{{tableData.sex == 1 ? '男' : (tableData.sex == 2 ?'女' : '未知') }}</td>
									<th>邀请人邀请码</th>
									<td>
										<span style="color: #5cc477;cursor:pointer;"
											@click="colonelClick(tableData.inviterCode)">{{tableData.inviterCode}}</span>
										<el-button size="mini" :disabled="!isAuth('userList:updateStatus')"
											@click="ChangeuserUpdate(tableData,6)">修改</el-button>
									</td>
									<th>邀请码</th>
									<td class="border-rt" style="color: #5cc477;">
										{{tableData.invitationCode ? tableData.invitationCode : '未绑定'}}
									</td>
								</tr>
								<tr>
									<th>app微信登录openid</th>
									<td>{{tableData.openId ? tableData.openId : '未授权'}}</td>
									<th>openId</th>
									<td>
										{{tableData.open_id ? tableData.open_id : '未绑定'}}
										<!-- <span v-if="!tableData.tearoomId || tableData.tearoomId ==0">普通用户</span>
										<span v-else>保洁</span>
										<el-button size="mini" :disabled="!isAuth('userList:updateStatus')"
											@click="Changeuser(tableData.userId,tableData.isAuthentication)">修改用户</el-button> -->
									</td>
									<th>渠道</th>
									<td class="border-rt">
										{{tableData.platform}}
										<!-- <span v-if="tableData.platform == 'weixin' ">公众号</span>
										<span v-if="tableData.platform == 'H5' ">网站</span>
										<span v-if="tableData.platform == 'app' ">APP</span>
										<span v-if="tableData.platform == 'mp' ">小程序</span>
										<span v-if="tableData.platform == 'web' ">网站</span>
										<span v-if="tableData.platform == '' ">未知</span> -->
									</td>
								</tr>
								<!-- <tr>
									<th>生日</th>
									<td>
										{{tableData.birthday ? tableData.birthday : '-'}}
										<el-button size="mini" :disabled="!isAuth('userList:update')"
											@click="ChangeuserUpdate(tableData,9)">修改</el-button>
									</td>
									<th>实名姓名</th>
									<td>{{userCertification.name}}
									</td>
									<th>身份证号</th>
									<td class="border-rt">
										{{userCertification.idNumber}}
									</td>
								</tr>
								<tr>
									<th>身份证正面</th>
									<td>
										<img v-if="userCertification.front" :src="userCertification.front" width="80"
											height="80" />
									</td>
									<th>身份证背面</th>
									<td>
										<img v-if="userCertification.back" :src="userCertification.back" width="80"
											height="80" />
									</td>
									<th>认证状态</th>
									<td class="border-rt">
										<span v-if="userCertification.status == 0">待审核</span>
										<span v-if="userCertification.status == 1">审核成功</span>
										<span v-if="userCertification.status == 2">已拒绝</span>
									</td>
								</tr>
								 -->
								<!-- <tr>
									<th>是否自动开台</th>
									<td>
										{{tableData.isOpenGoods==1?'是':'否'}}
										<el-button size="mini" 											:disabled="!isAuth('userList:update')"
											@click="ageChangeKt(tableData)">修改</el-button>
									</td>
									<th>桌台名称</th>
									<td>
										{{tableData.userGoodsName}}
									</td>
									<th>用户头像</th>
									<td class="border-rt">
										<div style="margin: 2px;" v-if="tableData.userPhoto">
											<div v-for="(item,index) in tableData.userPhoto.split(',')"
												style="display: inline-block;margin: 5px;">
												<el-popover placement="top-start" title="" trigger="hover">
													<img style="width: 50px; height: 50px" :src="item" alt=""
														slot="reference">
													<img style="width: 300px; height: auto" :src="item" alt="">
												</el-popover>
											</div>
										</div>
										<span v-else> - </span>
									</td>
								</tr> -->
								<tr>
									<th>是否是会员</th>
									<td>
										{{tableData.isVip==1?'是':'否'}}
									</td>
									<th>会员到期时间</th>
									<td>
										{{tableData.vipEndTime?tableData.vipEndTime:'-'}}
									</td>
									<th>信用分</th>
									<td class="border-rt">
										{{tableData.creditScore?tableData.creditScore:0}}
										<el-button size="mini" 											:disabled="!isAuth('userList:update')"
											@click="creditScoreChange(tableData.creditScore)">修改</el-button>
									</td>
								</tr>
								<tr>
									<th class="border-bt">相册</th>
									<td class="border-bt">
										<div style="margin: 2px;" v-if="tableData.photo">
											<div v-for="(item,index) in tableData.photo.split(',')"
												style="display: inline-block;margin: 5px;">
												<el-popover placement="top-start" title="" trigger="hover">
													<img style="width: 50px; height: 50px" :src="item" alt=""
														slot="reference">
													<img style="width: 300px; height: auto" :src="item" alt="">
												</el-popover>
											</div>
										</div>
										<span v-else> - </span>
									</td>
									<th class="border-bt"></th>
									<td class="border-bt">
										
									</td>
									<th class="border-bt"></th>
									<td class="border-bt border-rt">
										
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
				</div>
			</el-tab-pane>
			<el-tab-pane label="用户消息" name="third">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="userData.list">
					<el-table-column prop="id" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="title" label="消息标题">
					</el-table-column>
					<el-table-column prop="content" label="消息内容">
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
				</el-table>
				<div class="user-detail-page__pagination">
					<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="userData.totalCount">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>
			<el-tab-pane label="提现记录" name="sixth">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="withdrawData.list">
					<el-table-column fixed prop="id" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="zhifubao" label="支付宝账号">
						<template slot-scope="scope">
							<span style="color: #5cc477;cursor: pointer;"
								@click="updates(scope.row.userId)">{{scope.row.zhifubao ? scope.row.zhifubao : 'null'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhifubaoName" label="支付宝名称">
						<template slot-scope="scope">
							<span>{{scope.row.zhifubaoName ? scope.row.zhifubaoName : 'null'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="classify" label="提现方式" width="100">
						<template slot-scope="scope">
							<span v-if="scope.row.classify == 1">支付宝</span>
							<span v-if="scope.row.classify == 2">微信小程序</span>
							<span v-if="scope.row.classify == 3">微信公众号</span>
						</template>
					</el-table-column>
					<el-table-column prop="money" label="提现金额" width="100">
					</el-table-column>
					<el-table-column prop="state" label="状态" width="100" fixed="right">
						<template slot-scope="scope">
							<span style="color: #5cc477;cursor: pointer;" v-if="scope.row.state == 0">待转账</span>
							<span v-if="scope.row.state == 1">已转账</span>
							<span v-if="scope.row.state == -1">已退款</span>
						</template>
					</el-table-column>
					<el-table-column prop="refund" label="退款原因" width="220">
					</el-table-column>
					<el-table-column prop="createAt" label="申请时间" width="170">
					</el-table-column>
					<el-table-column prop="outAt" label="转账/退款时间" width="180">
					</el-table-column>
				</el-table>
				<div class="user-detail-page__pagination">
					<el-pagination @size-change="handleSizeChange11" @current-change="handleCurrentChange11"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="withdrawData.totalCount">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>
			<el-tab-pane label="钱包明细" name="eighth">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="walletData.list">
					<el-table-column prop="id" label="编号" width="100"></el-table-column>
					<el-table-column prop="title" label="标题"></el-table-column>
					<el-table-column prop="content" label="内容"></el-table-column>
					<el-table-column prop="money" label="金额" width="100">
						<template slot-scope="scope">
							<span style="color: #f56c6c;" v-if="scope.row.type==2">- {{ scope.row.money }}</span>
							<span style="color: #006600;" v-if="scope.row.type==1">+ {{ scope.row.money }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
				</el-table>
				<div class="user-detail-page__pagination">
					<el-pagination @size-change="handleSizeChange2" @current-change="handleCurrentChange2"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="walletData.totalCount">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>
			<el-tab-pane label="商家收益明细" name="eighthShop">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="walletData.list">
					<el-table-column prop="id" label="编号" width="100"></el-table-column>
					<el-table-column prop="title" label="标题"></el-table-column>
					<el-table-column prop="content" label="内容"></el-table-column>
					<el-table-column prop="money" label="金额" width="100">
						<template slot-scope="scope">
							<span style="color: #f56c6c;" v-if="scope.row.type==2">- {{ scope.row.money }}</span>
							<span style="color: #006600;" v-if="scope.row.type==1">+ {{ scope.row.money }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
				</el-table>
				<div class="user-detail-page__pagination">
					<el-pagination @size-change="handleSizeChangeShop" @current-change="handleCurrentChangeShop"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="walletData.totalCount">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>
			<el-tab-pane label="分销收益明细" name="eighthS">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="walletData.list">
					<el-table-column prop="id" label="编号" width="100"></el-table-column>
					<el-table-column prop="title" label="标题"></el-table-column>
					<el-table-column prop="content" label="内容"></el-table-column>
					<el-table-column prop="money" label="金额" width="100">
						<template slot-scope="scope">
							<span style="color: #f56c6c;" v-if="scope.row.type==2">- {{ scope.row.money }}</span>
							<span style="color: #006600;" v-if="scope.row.type==1">+ {{ scope.row.money }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
				</el-table>
				<div class="user-detail-page__pagination">
					<el-pagination @size-change="handleSizeChangeSy" @current-change="handleCurrentChangeSy"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="walletData.totalCount">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>
			<el-tab-pane label="支付明细" name="ninth">
				<div class="nf-filter-card">
					<div class="nf-filter-card__head">
						<i class="el-icon-search nf-filter-card__head-icon"></i>
						<span>筛选条件</span>
					</div>
					<el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
						<el-form-item label="开始时间">
							<el-date-picker class="user-detail-page__filter-date" v-model="startTime" align="right"
								type="datetime" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间">
							</el-date-picker>
						</el-form-item>
						<el-form-item label="截止时间">
							<el-date-picker class="user-detail-page__filter-date" v-model="endTime" align="right"
								type="datetime" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择截止时间">
							</el-date-picker>
						</el-form-item>
						<el-form-item class="user-detail-page__filter-actions">
							<el-button type="primary" icon="el-icon-search" @click="timeDate2">查询</el-button>
						</el-form-item>
					</el-form>
				</div>
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="rechargeData.list">
					<el-table-column prop="id" label="编号" width="50"></el-table-column>
					<el-table-column prop="orderId" label="支付订单号" width="200"></el-table-column>
					<el-table-column prop="money" label="支付金额"></el-table-column>
					<el-table-column prop="userId" label="会员编号"></el-table-column>
					<el-table-column label="分类">
						<template slot-scope="scope">
							<span style="color: #5cc477;cursor: pointer;" v-if="scope.row.classify == 1">app微信</span>
							<span style="color: #5cc477;cursor: pointer;" v-if="scope.row.classify == 2">微信公众号</span>
							<span style="color: #5cc477;cursor: pointer;" v-if="scope.row.classify == 3">微信小程序</span>
							<span style="color: #5cc477;cursor: pointer;" v-if="scope.row.classify == 4">支付宝</span>
							<span style="color: #5cc477;cursor: pointer;" v-if="scope.row.classify == 5">系统充值</span>
						</template>
					</el-table-column>
					<el-table-column label="状态">
						<template slot-scope="scope">
							<span style="color: #5cc477;cursor: pointer;" v-if="scope.row.state == 0">待支付</span>
							<span style="color: #5cc477;cursor: pointer;" v-if="scope.row.state == 1">支付成功</span>
							<span style="color: #5cc477;cursor: pointer;" v-if="scope.row.state == 2">支付失败</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="170">
					</el-table-column>
					<el-table-column prop="payTime" label="支付时间" width="170">
					</el-table-column>
				</el-table>
				<div class="user-detail-page__pagination">
					<el-pagination @size-change="handleSizeChange3" @current-change="handleCurrentChange3"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="rechargeData.totalCount">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>
			<!-- <el-tab-pane label="积分明细" name="eighthJf">
				<el-table v-loading="tableDataLoading" :data="walletData.records">
					<el-table-column prop="id" label="编号" width="100"></el-table-column>
					<el-table-column prop="content" label="内容"></el-table-column>
					<el-table-column prop="num" label="金额" width="100">
						<template slot-scope="scope">
							<span style="color: #f56c6c;" v-if="scope.row.type==2">- {{ scope.row.num }}</span>
							<span style="color: #006600;" v-if="scope.row.type==1">+ {{ scope.row.num }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
				</el-table>
				<div class="user-detail-page__pagination">
					<el-pagination @size-change="handleSizeChangeJf" @current-change="handleCurrentChangeJf"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="walletData.total">
					</el-pagination>
				</div>
			</el-tab-pane> -->
			<!-- 会员值明细 -->
			<!-- <el-tab-pane label="会员值明细" name="eighthXfed">
				<div style="text-align: right;margin-right: 10px;">
					<el-button size="mini" type="primary" @click="confirmAdd()"
						:disabled="!isAuth('userList:addHyz')">添加
					</el-button>
				</div>
				<el-table v-loading="tableDataLoading" :data="walletDataHyz.records">
					<el-table-column prop="userVipDetailsId" label="编号" width="100"></el-table-column>
					<el-table-column prop="money" label="类型" width="100">
						<template slot-scope="scope">
							<span style="color: #f56c6c;" v-if="scope.row.classify==2">消费</span>
							<span style="color: #006600;" v-if="scope.row.classify==1">充值</span>
						</template>
					</el-table-column>
					<el-table-column prop="ordersNo" label="订单号"></el-table-column>
					<el-table-column prop="money" label="会员值" width="100">
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
					<el-table-column label="操作" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="danger" @click="confirmDelete(scope.row)"
								:disabled="!isAuth('userList:deleteHyz')">删除
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div class="user-detail-page__pagination">
					<el-pagination @size-change="handleSizeChangeHyz" @current-change="handleCurrentChangeHyz"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="walletDataHyz.total">
					</el-pagination>
				</div>
			</el-tab-pane> -->

			<!-- <el-tab-pane label="信用分明细" name="xinyongfen">
				<el-table v-loading="tableDataLoading" :data="tableDataXyf.records">
					<el-table-column prop="recordId" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="sysUserName" label="操作用户" width="150">
						<template slot-scope="scope">
							<span
								style="color: #5cc477;cursor: pointer;">{{scope.row.sysUserName ? scope.row.sysUserName : '未绑定'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="lastCreditScore" label="修改前信用分"></el-table-column>
					<el-table-column prop="nextCreditScore" label="修改后信用分"></el-table-column>
					<el-table-column prop="num" label="信用分" width="100">
						<template slot-scope="scope">
							<span style="color: #f56c6c;" v-if="scope.row.type==2">- {{ scope.row.num }}</span>
							<span style="color: #006600;" v-if="scope.row.type==1">+ {{ scope.row.num }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="reason" label="备注"></el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
				</el-table>
				<div class="user-detail-page__pagination">
					<el-pagination @size-change="handleSizeChangeXyf" @current-change="handleCurrentChangeXyf"
						:page-sizes="[10, 20, 30, 40]" :page-size="size1" :current-page="page1"
						layout="total,sizes, prev, pager, next,jumper" :total="tableDataXyf.total">
					</el-pagination>
				</div>
			</el-tab-pane> -->
			<el-tab-pane label="我的订单" name="myorder">
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
					
					<el-table-column prop="hourPrice" label="每小时价格">
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
					</el-table-column>
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
							<el-button size="mini" type="primary" style="margin:3px" plain
								@click="moneyClick(scope.row)">其他金额
							</el-button>
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
				</el-table>
				<div class="user-detail-page__pagination">
					<el-pagination @size-change="handleSizeChangeOr" @current-change="handleCurrentChangeOr"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>
			<el-tab-pane label="我的优惠券" name="youhuiquanJf">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="tableDataYhq.content">
					<el-table-column prop="couponId" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="platformType" label="优惠券平台">
						<template slot-scope="scope">
							<span v-if="scope.row.platformType==1">美团</span>
							<span v-else-if="scope.row.platformType==2">抖音</span>
							<span v-else-if="scope.row.platformType==3">快手</span>
							<span v-else>-</span>
						</template>
					</el-table-column>
					<el-table-column prop="tearoom.tearoomName" label="台球馆名称">
					</el-table-column>
					<el-table-column prop="couponName" label="名称" width="150">
					</el-table-column>
					<el-table-column prop="hour" label="使用时长">
					</el-table-column>
					<el-table-column prop="minHour" label="最低使用时长(小时)">
					</el-table-column>
					<el-table-column prop="teamRule" label="使用规则">
					</el-table-column>
					<el-table-column prop="buyRule" label="购买须知">
					</el-table-column>
					<el-table-column label="状态">
						<template slot-scope="scope">
							<span style="color: #5cc477;" v-if="scope.row.status == 1">未使用</span>
							<span v-if="scope.row.status == 2">已使用</span>
							<span v-if="scope.row.status == 3">已过期</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="150">
					</el-table-column>
				</el-table>
				<div class="user-detail-page__pagination">
					<el-pagination @size-change="handleSizeChangeYhq" @current-change="handleCurrentChangeYhq"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="tableDataYhq.totalElements">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>
			<el-tab-pane label="兑换记录" name="duihuanquan">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="tableDataYhq.list">
					<el-table-column prop="id" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="userName" label="用户昵称">
						<template slot-scope="scope">
							<span style="color: #13ce66;cursor: pointer;"
								@click="updates(scope.row.userId)">{{scope.row.userName}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="image" label="用户头像">
						<template slot-scope="scope">
							<img v-if="scope.row.image" :src="scope.row.image" alt="" width="60" height="60">
						</template>
					</el-table-column>
					<el-table-column prop="type" label="平台">
						<template slot-scope="scope">
							<span v-if="scope.row.type==1">美团</span>
							<span v-if="scope.row.type==2">抖音</span>
							<span v-if="scope.row.type==3">快手</span>
						</template>
					</el-table-column>
					<el-table-column prop="content" label="兑换码">
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间" width="160">
					</el-table-column>
					<!-- <el-table-column label="状态">
						<template slot-scope="scope">
							<span style="color: #5cc477;" v-if="scope.row.status == 1">未使用</span>
							<span v-if="scope.row.status == 2">已使用</span>
							<span v-if="scope.row.status == 3">已过期</span>
						</template>
					</el-table-column> -->
				</el-table>
				<div class="user-detail-page__pagination">
					<el-pagination @size-change="handleSizeChangeDhq" @current-change="handleCurrentChangeDhq"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="tableDataYhq.totalCount">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>
			<!-- <el-tab-pane label="我的优惠卡" name="youhuika">
				<el-table v-loading="tableDataLoading" :data="tableData.list">
					<el-table-column fixed prop="cardId" label="编号" width="50">
					</el-table-column>
					<el-table-column prop="cardType" label="类型">
						<template slot-scope="scope">
							<div v-if="scope.row.cardType==1">月卡</div>
							<div v-if="scope.row.cardType==2">次卡</div>
						</template>
					</el-table-column>
					<el-table-column prop="cardName" label="名称">
					</el-table-column>
					<el-table-column prop="sumNum" label="总数量">
					</el-table-column>
					<el-table-column prop="num" label="剩余数量">
					</el-table-column>
					<el-table-column prop="status" label="服务项目">
						<template slot-scope="scope">
							<div v-if="scope.row.orderTakingList">
								<span v-for="(item,index) in scope.row.orderTakingList" :key="index">
									{{item?item.myLevel:''}};
								</span>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间">
					</el-table-column>
					<el-table-column prop="status" label="状态">
						<template slot-scope="scope">
							<div v-if="scope.row.status==1">正常</div>
							<div v-if="scope.row.status==2">已使用</div>
							<div v-if="scope.row.status==3">已过期</div>
						</template>
					</el-table-column>
				</el-table>
				<div class="user-detail-page__pagination">
					<el-pagination @size-change="handleSizeChangeYhk" @current-change="handleCurrentChangeYhk"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane> -->
			<el-tab-pane label="团队成员" name="fenxiaoshouyi">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="tableDatamin">
					<el-table-column fixed prop="userId" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="userName" label="昵称">
						<template slot-scope="scope">
							<span style="color: #f56c6c;"
								@click="updates(scope.row.userId)">{{ scope.row.userName ? scope.row.userName : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column label="图像">
						<template slot-scope="scope">
							<img v-if="scope.row.avatar==null" src="~@/assets/img/avatar.png" alt="" width="40"
								height="40">
							<img v-else :src="scope.row.avatar" alt="" width="40" height="40">
						</template>
					</el-table-column>
					<!-- <el-table-column prop="money" label="收益">
					</el-table-column> -->
					<el-table-column prop="createTime" label="创建时间" width="160">
					</el-table-column>
				</el-table>
				<div class="user-detail-page__pagination">
					<el-pagination @size-change="handleSizeChangeTd" @current-change="handleCurrentChangeTd"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="totalnum">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>
			<!-- 我的车辆 -->
			<el-tab-pane label="我的车辆" name="quanzi">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="tableDataQz.records">
					<el-table-column prop="carId" fixed="left" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="carType" fixed label="车辆型号">
					</el-table-column>
					<!-- <el-table-column prop="circleImg" label="圈子图片">
						<template slot-scope="scope">
							<div v-if="scope.row.circleImg">
								<div v-for="(item,index) in scope.row.circleImg.split(',')" :key="index"
									style="display: inline-block; margin: 3px;">
									<el-popover placement="top-start" title="" trigger="hover">
										<img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
										<img style="width: 300px; height: auto" :src="item" alt="">
									</el-popover>
								</div>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="circleDesc" label="圈子描述" width="300">
						<template slot-scope="scope">
							<el-popover placement="top-start" title="" trigger="hover">
								<div style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;"
									slot="reference">{{scope.row.circleDesc}}</div>
								<div style="width: 300px;">{{scope.row.circleDesc}}</div>
							</el-popover>
						</template>
					</el-table-column> -->
					<el-table-column prop="carNo" label="车牌号">
					</el-table-column>
					<el-table-column prop="carColor" label="加车辆颜色入的人数">
					</el-table-column>
					<el-table-column prop="carName" label="车主姓名"></el-table-column>
					<el-table-column prop="carPhone" label="车主电话"></el-table-column>
					<el-table-column prop="createTime" label="创建时间">
					</el-table-column>
					<!-- <el-table-column label="圈子类型" fixed="right">
						<template slot-scope="scope">
							<span v-if="scope.row.circleType==1" style="color:#13ce66">公开</span>
							<span v-if="scope.row.circleType==2" style="color:#ff4949">私密</span>
						</template>
					</el-table-column>
					<el-table-column label="状态" fixed="right">
						<template slot-scope="scope">
							<span v-if="scope.row.circleStatus==1" style="color:#13ce66">正常</span>
							<span v-if="scope.row.circleStatus==2" style="color:#ff4949">已封禁</span>
						</template>
					</el-table-column> -->
				</el-table>
				<div class="user-detail-page__pagination">
					<el-pagination @size-change="handleSizeChangeQz" @current-change="handleCurrentChangeQz"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="tableDataQz.total">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>
			<el-tab-pane label="常用电站" name="huodong">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="liwuData.list">
					<el-table-column fixed="left" prop="tearoomId" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="shopType" label="店铺类型" width="90">
						<template slot-scope="scope">
							<span style="color: #F56C6C;" v-if="scope.row.shopType == 1">自营</span>
							<span style="color: #5cc477;" v-if="scope.row.shopType == 2">三方</span>
						</template>
					</el-table-column>
					<el-table-column prop="tearoomName" fixed="left" label="充电站名称" width="120">
					</el-table-column>
					<el-table-column prop="tearoomPhone" label="充电站电话">
					</el-table-column>
					<el-table-column prop="tearoomType" label="充电类型" width="90">
						<template slot-scope="scope">
							<span style="color: #F56C6C;" v-if="scope.row.tearoomType == 1">电瓶车</span>
							<span style="color: #5cc477;" v-if="scope.row.tearoomType == 2">汽车桩</span>
						</template>
					</el-table-column>
					<el-table-column prop="tearoomTitleImage" label="充电站logo">
						<template slot-scope="scope">
							<span v-if="scope.row.tearoomTitleImage == null">暂无图片</span>
							<img v-else :src="scope.row.tearoomTitleImage" alt="" width="40" height="40">
						</template>
					</el-table-column>
					<el-table-column prop="nickname" label="用户昵称" width="120">
						<template slot-scope="scope">
							<span style="color: #5cc477;cursor: pointer;"
								@click="updates(scope.row.userId)">{{scope.row.userName ? scope.row.userName : '未绑定'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="store_address" label="充电站地址" width="200">
						<template slot-scope="scope">
							<div>
								{{scope.row.tearoomProvince}}{{scope.row.tearoomCity}}{{scope.row.tearoomDistrict}}{{scope.row.tearoomAddress}}
							</div>
							<div>经度：{{scope.row.tearoomLongitude ? scope.row.tearoomLongitude : '未绑定'}}</div>
							<div>纬度：{{scope.row.tearoomLatitude ? scope.row.tearoomLatitude : '未绑定'}}</div>
						</template>
					</el-table-column>
					<el-table-column prop="rate" label="佣金比例">
					</el-table-column>
					<el-table-column prop="wxCode" label="微信二维码">
						<template slot-scope="scope">
							<div v-if="scope.row.wxCode">
								<span v-for="(item,index) in scope.row.wxCode.split(',')">
									<el-popover placement="top-start" title="" trigger="hover">
										<img style="width: 50px; height: 50px;margin: 3px;" :src="item" alt="" slot="reference">
										<img style="width: 300px; height: 300px" :src="item" alt="">
									</el-popover>
								</span>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="qunImg" label="群二维码" width="200">
						<template slot-scope="scope">
							<div v-if="scope.row.qunImg">
								<span v-for="(item,index) in scope.row.qunImg.split(',')">
									<el-popover placement="top-start" title="" trigger="hover">
										<img style="width: 50px; height: 50px;margin: 3px;" :src="item" alt="" slot="reference">
										<img style="width: 300px; height: 300px" :src="item" alt="">
									</el-popover>
								</span>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="startTime" label="营业时间" width="100">
						<template slot-scope="scope">
							<div>{{scope.row.startTime}}--{{scope.row.endTime}}</div>
						</template>
					</el-table-column>
					<el-table-column prop="sales" label="销量">
						<template slot-scope="scope">
							<span>{{scope.row.sales ? scope.row.sales : '0'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="status" fixed="right" label="状态" width="80">
						<template slot-scope="scope">
							<span style="color: #13ce66;" v-if="scope.row.status == 1">正常</span>
							<span style="color: #F56C6C;" v-if="scope.row.status == 2">打烊</span>
							<span style="color: #5cc477;" v-if="scope.row.status == 3">今日已满</span>
						</template>
					</el-table-column>
				</el-table>
				<div class="user-detail-page__pagination">
					<el-pagination @size-change="handleSizeChangeLw" @current-change="handleCurrentChangeLw"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="liwuData.totalCount">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>
			<!-- 我的报名 -->
			<!-- <el-tab-pane label="我的报名" name="yuewanjilu">
				<el-table v-loading="tableDataLoading" :data="yuewanData.list">
					<el-table-column prop="activityApplyId" label="编号" width="80" fixed="left">
					</el-table-column>
					<el-table-column prop="userName" label="发布用户">
						<template slot-scope="scope">
							　<span style="color: #5cc477;cursor: pointer;" v-if="scope.row.activity"
								@click="updates1(scope.row.userId)">
								{{scope.row.activity.userEntity? scope.row.activity.userEntity.userName : '未绑定'}}
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="activityPhone" label="联系电话" width="100">
					</el-table-column>
					<el-table-column prop="shopName" label="店铺名称" width="150">
					</el-table-column>
					<el-table-column prop="scShop.shopAddress" label="商家地址" width="150">
					</el-table-column>
					<el-table-column prop="activityName" label="活动名称" width="150">
					</el-table-column>
					<el-table-column prop="activityImg" label="主图" width="200">
						<template slot-scope="scope">
							<div v-if="scope.row.activityImg">
								<div v-for="(item,index) in scope.row.activityImg.split(',')" :key="index"
									style="display: inline-block; margin: 3px;">
									<el-popover placement="top-start" title="" trigger="hover">
										<img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
										<img style="width: 300px; height: auto" :src="item" alt="">
									</el-popover>
								</div>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="activityDetailsImg" label="展示图" width="200">
						<template slot-scope="scope">
							<div v-if="scope.row.activityDetailsImg">
								<div v-for="(item,index) in scope.row.activityDetailsImg.split(',')" :key="index"
									style="display: inline-block; margin: 3px;">
									<el-popover placement="top-start" title="" trigger="hover">
										<img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
										<img style="width: 300px; height: auto" :src="item" alt="">
									</el-popover>
								</div>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="activityDetails" label="活动须知" width="250">
						<template slot-scope="scope">
							<el-popover placement="top-start" title="" trigger="hover">
								<div style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;"
									slot="reference">{{scope.row.activityDetails}}</div>
								<div style="width: 300px;">{{scope.row.activityDetails}}</div>
							</el-popover>
						</template>
					</el-table-column>
					<el-table-column prop="activityDate" label="活动开始时间" width="100">
					</el-table-column>
					<el-table-column prop="activityEndTime" label="活动结束时间" width="100">
						<template slot-scope="scope">
							<span>{{scope.row.activityEndTime?scope.row.activityEndTime:'未设置'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="counts" label="已报名人数">
					</el-table-column>
					<el-table-column prop="signCounts" label="已签到人数">
					</el-table-column>
					<el-table-column prop="isSex" label="是否限制男女">
						<template slot-scope="scope">
							<div v-if="scope.row.isSex==1">是</div>
							<div v-else>否</div>
						</template>
					</el-table-column>
					<el-table-column prop="giftMouseNum" label="活动人数" width="160">
						<template slot-scope="scope">
							<div v-if="scope.row.isSex==1">
								<div>男生人数：{{scope.row.activityMan}}</div>
								<div>女生人数：{{scope.row.activityGirl}}</div>
							</div>
							<div v-else>
								<div>总人数：{{scope.row.activityGirl}}</div>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="activityEndDate" label="报名截止时间" width="160">
					</el-table-column>
					<el-table-column prop="paymentType" label="费用方式">
						<template slot-scope="scope">
							<div v-if="scope.row.paymentType==1">线上收费</div>
							<div v-if="scope.row.paymentType==2">免费</div>
							<div v-if="scope.row.paymentType==3">线下收费</div>
						</template>
					</el-table-column>
					<el-table-column prop="price" label="报名费">
					</el-table-column>
					<el-table-column prop="auditContent" label="审核理由" width="160">
					</el-table-column>
					<el-table-column prop="status" label="状态" width="100" fixed='right'>
						<template slot-scope="scope">
							<span style="color: #5cc477;" v-if="scope.row.status === 1 ">报名中</span>
							<span style="color: #13ce66;" v-if="scope.row.status === 2 ">进行中</span>
							<span style="color: #ff4949;" v-if="scope.row.status === 3 ">已结束</span>
							<span style="color: #999;" v-if="scope.row.status === 5 ">已取消</span>
							<span style="color: orange;" v-if="scope.row.status === 6 ">待开始</span>
							<span style="color: #ff4949;" v-if="scope.row.status === 7 ">待审核</span>
							<span style="color: #999;" v-if="scope.row.status === 8 ">已拒绝</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160">
					</el-table-column>
				</el-table>
				<div class="user-detail-page__pagination">
					<el-pagination @size-change="handleSizeChangeYw" @current-change="handleCurrentChangeYw"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="yuewanData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane> -->
			<!-- <el-tab-pane label="店铺订单" name="tenth">
				<el-table v-loading="tableDataLoading" :data="takeData.list">
					<el-table-column fixed prop="orderId" label="编号" width="80"></el-table-column>
					<el-table-column prop="tearoomName" label="台球馆名称"></el-table-column>
					<el-table-column prop="goodsName" label="桌台名称"></el-table-column>
					<el-table-column prop="orderNumber" label="订单号" width="180"></el-table-column>
					<el-table-column prop="orderGoodsList" label="内容" width="300">
						<template slot-scope="scope">
							<el-popover placement="top-start" title="" trigger="hover">
								<div style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;"
									slot="reference">
									<div v-for="(item, index) in scope.row.orderGoodsList" :key="index">
										<span>{{ index + 1 }}.
											【商铺：{{item.shopName}}】商品名：{{ item.goodsName }},</span>
										<span>单价：{{item.goodsPrice}}*{{ item.goodsNum }}; </span>
										<span>规格：{{ item.skuMessage }}; </span>
										<span>打包费：{{ item.goodsPack }}; </span>
										<span v-if="item.goodRemarks">备注：{{ item.goodRemarks }}</span>
									</div>
								</div>
								<div style="width: 500px; height: auto;">
									<div v-for="(item, index) in scope.row.orderGoodsList" :key="index">
										<span>{{ index + 1 }}.
											【商铺：{{item.shopName}}】商品名：{{ item.goodsName }},</span>
										<span>单价：{{item.goodsPrice}}*{{ item.goodsNum }}; </span>
										<span>规格：{{ item.skuMessage }}; </span>
										<span>打包费：{{ item.goodsPack }}; </span>
										<span v-if="item.goodRemarks">备注：{{ item.goodRemarks }}</span>
									</div>
								</div>
							</el-popover>
						</template>
					</el-table-column>
					<el-table-column prop="orderRemarks" label="订单备注" width="200">
						<template slot-scope="scope">
							{{scope.row.orderRemarks}}
						</template>
					</el-table-column>
					<el-table-column prop="orderMoney" label="商品总价" width="100">
					</el-table-column>
					<el-table-column prop="vipMoney" label="优惠金额" width="100">
						<template slot-scope="scope">
							<span>{{ scope.row.vipMoney ? scope.row.vipMoney : '0' }}</span>
						</template>
					</el-table-column>
					<el-table-column fixed="right" prop="status" label="状态" width="100">
						<template slot-scope="scope">
							<span style="color: #5cc477;" v-if="scope.row.status === 2">已下单</span>
							<span style="color: #f56c6c;" v-if="scope.row.status === 4">已完成</span>
							<span style="color: #999;" v-if="scope.row.status === 5">已取消</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间">
					</el-table-column>
				</el-table>
				<div class="user-detail-page__pagination">
					<el-pagination @size-change="handleSizeChange6" @current-change="handleCurrentChange6"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="takeData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane> -->
			<!-- <el-tab-pane label="聊天记录" name="pinglun">
				<el-table v-loading="tableDataLoading" :data="autonymData.records">
					<el-table-column fixed prop="chatId" label="id" width="80">
					</el-table-column>
					<el-table-column prop="prompt" label="问题">
						<template slot-scope="scope">
							<div>
								<el-popover placement="top-start" title="" trigger="hover">
									<div style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;"
										slot="reference">{{scope.row.prompt}}</div>
									<div style="width: 200px;height: auto;">{{scope.row.prompt}}</div>
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="answer" label="答案">
						<template slot-scope="scope">
							<div>
								<div v-if="scope.row.messageType==1">
									<div>
										<el-popover placement="top-start" title="" trigger="hover">
											<div style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;"
												slot="reference">{{scope.row.answer}}
											</div>
											<div
												style="width: 400px;height: auto;word-break: break-all;max-height: 500px;overflow: auto">
												{{scope.row.answer}}
											</div>
										</el-popover>
									</div>
								</div>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160">
					</el-table-column>
				</el-table>
				<div class="user-detail-page__pagination">
					<el-pagination @size-change="handleSizeChange9" @current-change="handleCurrentChange9"
						:page-sizes="[10, 15, 20]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="autonymData.total">
					</el-pagination>
				</div>
			</el-tab-pane> -->
			<!-- 发布的约球记录 -->
			<!-- <el-tab-pane label="发布的约球记录" name="fabuyueqiu">
				<el-table v-loading="tableDataLoading" :data="tableData.list">
					<el-table-column prop="dateBallId" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="phone" label="联系电话">
					</el-table-column>
					<el-table-column prop="tearoomName" label="台球馆名称">
					</el-table-column>
					<el-table-column prop="dateBallClassify" label="对局类型">
						<template slot-scope="scope">
							<span v-if="scope.row.dateBallClassify==1" style="color: #f56c6c;">公开</span>
							<span v-if="scope.row.dateBallClassify==2" style="color: #5cc477;">单约</span>
						</template>
					</el-table-column>
					<el-table-column prop="ballType" label="约球类型">
					</el-table-column>
					<el-table-column prop="ballNum" label="约球局数">
					</el-table-column>
					<el-table-column prop="ballTime" label="约球时间">
					</el-table-column>
					<el-table-column prop="peopleNum" label="邀请人数">
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间">
					</el-table-column>
					<el-table-column label="状态" width="120">
						<template slot-scope="scope">
							<span v-if="scope.row.status==1" style="color: #f56c6c;">待开始</span>
							<span v-if="scope.row.status==2" style="color: #5cc477;">进行中</span>
							<span v-if="scope.row.status==3" style="color: #f56c6c;">已结束</span>
							<span v-if="scope.row.status==4" style="color: #999;">已取消</span>
						</template>
					</el-table-column>
					<el-table-column fixed="right" label="操作" width="160">
						<template slot-scope="scope">
							<el-button size="mini" style="margin: 2px;" type="warning"
								@click="chengyuanBtn(scope.row)">约球成员</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div class="user-detail-page__pagination">
					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane> -->
			<!-- 参加的约球记录 -->
			<!-- <el-tab-pane label="参加的约球记录" name="yuyueyueqiu">
				<el-table v-loading="tableDataLoading" :data="tableData.list">
					<el-table-column prop="dateBallId" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="dateBall.userName" label="发起用户">
						<template slot-scope="scope">
							　<span v-if="scope.row.dateBall" style="color: #5cc477;cursor: pointer;"
								@click="updatesvideo(scope.row.dateBall.userId)">
								{{scope.row.dateBall.userName? scope.row.dateBall.userName : '未绑定'}}
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="dateBall.phone" label="联系电话">
					</el-table-column>
					<el-table-column prop="dateBall.tearoomName" label="台球馆名称">
					</el-table-column>
					<el-table-column prop="dateBall.dateBallClassify" label="对局类型">
						<template slot-scope="scope">
							<div v-if="scope.row.dateBall">
								<span v-if="scope.row.dateBall.dateBallClassify==1" style="color: #f56c6c;">公开</span>
								<span v-if="scope.row.dateBall.dateBallClassify==2" style="color: #5cc477;">单约</span>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="dateBall.ballType" label="约球类型">
					</el-table-column>
					<el-table-column prop="dateBall.ballNum" label="约球局数">
					</el-table-column>
					<el-table-column prop="dateBall.ballTime" label="约球时间">
					</el-table-column>
					<el-table-column prop="dateBall.peopleNum" label="邀请人数">
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间">
					</el-table-column>
					<el-table-column label="状态" width="120">
						<template slot-scope="scope">
							<span v-if="scope.row.status==1" style="color: #f56c6c;">待签到</span>
							<span v-if="scope.row.status==2" style="color: #5cc477;">已签到</span>
							<span v-if="scope.row.status==3" style="color: #f56c6c;">已结束</span>
							<span v-if="scope.row.status==4" style="color: #999;">已取消 </span>
						</template>
					</el-table-column>
				</el-table>
				<div class="user-detail-page__pagination">
					<el-pagination @size-change="handleSizeChangeYyyq" @current-change="handleCurrentChangeYyyq"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane> -->
			<!-- 我的留言 -->
			<!-- <el-tab-pane label="我的留言" name="pinglunjilu">
				<el-table v-loading="tableDataLoading" :data="liuyanData.list">
					<el-table-column prop="leaveMessageId" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="content" label="评价内容">
					</el-table-column>
					<el-table-column prop="score" label="评分">
						<template slot-scope="scope">
							<span>{{scope.row.score?scope.row.score:'暂无评分'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="评价时间" width="180">
					</el-table-column>
				</el-table>
				<div class="user-detail-page__pagination">
					<el-pagination @size-change="handleSizeChangePlLy" @current-change="handleCurrentChangePlLy"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="liuyanData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane> -->
			<!-- 我的地址 -->
			<!-- <el-tab-pane label="我的地址" name="dizhi">
				<el-table v-loading="tableDataLoading" :data="dizhiData">
					<el-table-column prop="id" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="consignee" label="姓名">
					</el-table-column>
					<el-table-column prop="mobile" label="电话">
					</el-table-column>
					<el-table-column prop="consignee" label="地址">
						<template slot-scope="scope">
							<span>{{scope.row.provinces}}{{scope.row.detail}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="isDefault" label="是否为默认地址">
						<template slot-scope="scope">
							<span>{{scope.row.isDefault==1?'是':'否'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间" width="180">
					</el-table-column>
				</el-table>
			</el-tab-pane> -->
			<!-- 教练模块 -->
			<!-- <el-tab-pane label="教练详情" name="jishi" v-if="tableData.isAuthentication==2">
				<div class="detailtabel">
					<div class="table_main">
						<table>
							<tbody>
								<tr>
									<th>教练名称</th>
									<td>{{ artificer.artificerName ? `${artificer.artificerName}` : '未绑定' }}
										<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
											@click="xiugaiJsxx(artificer)">修改信息</el-button>
									</td>
									<th>教练Id</th>
									<td>{{ artificer.artificerId }}</td>
									<th>教练头像</th>
									<td class="border-rt">
										<img v-if="artificer.artificerImg==null" src="~@/assets/img/avatar.png" alt=""
											width="80" height="80">
										<img v-else :src="artificer.artificerImg" width="80" height="80" />
									</td>
									</td>
								</tr>
								<tr>
									<th>证书</th>
									<td>
										<div v-if="artificer.certificate">
											<img v-for="(item,index) in artificer.certificate" :key="index" :src="item"
												alt="" width="80" height="80" style="margin: 2px;">
										</div>
										<div v-else> - </div>
									</td>
									<th>教练生活照</th>
									<td>
										<div v-if="artificer.lifePhoto" style="min-width: 300px;">
											<img v-for="(item,index) in artificer.lifePhoto" :key="index" :src="item"
												alt="" width="80" height="80" style="margin: 2px;">
										</div>
										<div v-else> - </div>
									</td>
									<th>经度</th>
									<td class="border-rt">
										{{ artificer.longitude }}
									</td>
								</tr>
								<tr>
									<th>简介</th>
									<td>
										<div style="max-width: 300px;">
											{{ artificer.content}}
										</div>
									</td>
									</td>
									<th>教练销量</th>
									<td>
										{{ artificer.ordersCount?artificer.ordersCount:0 }}
									</td>
									<th>纬度</th>
									<td class="border-rt">{{ artificer.latitude }}</td>
								</tr>
								<tr>
									<th>是否优选</th>
									<td>
										<span>{{artificer.isGoods==1?'是':'否'}}</span>
									</td>
									<th>是否是高级教练</th>
									<td>
										<span>{{artificer.isHot==1?'是':'否'}}</span>
									</td>
									<th>项目类型</th>
									<td class="border-rt">
										{{artificer.classifyName}}
									</td>
								</tr>
								<tr>
									<th>收藏数量</th>
									<td>
										{{artificer.collectCount}}
									</td>
									<th>信誉分</th>
									<td>
										{{artificer.creditScore}}
										<el-button size="mini" v-if="tablenum.artificer"
											:disabled="!isAuth('userList:updateXyf')"
											@click="ageChange2(tableData.userId,tableData.creditScore)">修改</el-button>

									</td>
									<th>完成订单数量</th>
									<td class="border-rt">
										{{artificer.ordersCount}}
									</td>
								</tr>
								<tr>
									<th>教练抽成</th>
									<td>
										{{artificer.rate}}
										<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
											@click="xiugaiJs(artificer)">
											修改</el-button>
										</span>
									</td>
									<th>城市</th>
									<td>{{artificer.city}}</td>
									<th>忙时</th>
									<td class=" border-rt">
										<span>日期：</span>
										<el-date-picker style="width: 160px;margin-left: 10px;" v-model="artificerDate"
											align="right" type="date" format="yyyy-MM-dd" value-format="yyyy-MM-dd"
											placeholder="选择时间" @change="mangshi(artificerDate)">
										</el-date-picker>
										<div v-if="timeList.length>0" style="margin-top: 5px;">
											<el-tag v-for="(item,index) in timeList" :key="index" style="margin:3px">
												{{item.artificerTime}}
											</el-tag>
										</div>
										<el-tag type="info" style="cursor:pointer" @click="updateMs">修改</el-tag>
									</td>
								</tr>
								<tr>
									<th>出行方式</th>
									<td class=" border-rt">
										<span v-if="artificer.tripWay==1">公交</span>
										<span v-if="artificer.tripWay==2">出租</span>
										<span v-if="artificer.tripWay==3">免费</span>
									</td>
								</tr>
								<tr>
									<th class="border-bt">评分</th>
									<td class="border-bt">
										{{artificer.ordersScore}}
									</td>
									<th class="border-bt"></th>
									<td class="border-bt">
									</td>
									<th class="border-bt"></th>
									<td class="border-bt border-rt" style="min-width: 300px;">

									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</el-tab-pane> -->
			<!-- <el-tab-pane label="我的评价" name="dingdan">
				<el-table v-loading="tableDataLoading" :data="pingluData.list">
					<el-table-column prop="id" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="userName" label="教练昵称" width="150">
						<template slot-scope="scope">
							　<span>{{scope.row.artificerName?scope.row.artificerName:'未绑定'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="artificerImg" label="教练头像" width="150">
						<template slot-scope="scope">
							<div v-if="scope.row.artificerImg &&scope.row.artificerImg!=''"
								style="display: inline-block; margin: 3px;">
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px" :src="scope.row.artificerImg" alt=""
										slot="reference">
									<img style="width: 300px; height: auto" :src="scope.row.artificerImg" alt="">
								</el-popover>
							</div>
							<span v-else>暂无图片</span>
						</template>
					</el-table-column>
					<el-table-column prop="content" label="评价内容">
					</el-table-column>
					<el-table-column prop="score" label="评分" width="80">
					</el-table-column>
					<el-table-column prop="createTime" label="评价时间" width="180">
					</el-table-column>
				</el-table>
				<div class="user-detail-page__pagination">
					<el-pagination @size-change="handleSizeChangePlJl" @current-change="handleCurrentChangePlJl"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="pingluData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane>
			<el-tab-pane label="收到的评价" name="dingdanS" v-if="tableData.isAuthentication==2">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="pinglunData.list">
					<el-table-column prop="id" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="userName" label="评论用户" width="150">
						<template slot-scope="scope">
							<span style="color: #5cc477;cursor: pointer;"
								@click="updates(scope.row.userId)">{{scope.row.userName ? scope.row.userName : '未绑定'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="avatar" label="头像">
						<template slot-scope="scope">
							<img :src="scope.row.avatar" alt="" width="60" height="60">
						</template>
					</el-table-column>
					<el-table-column prop="title" label="项目">
					</el-table-column>
					<el-table-column prop="content" label="评价内容">
					</el-table-column>
					<el-table-column prop="score" label="评分">
						<template slot-scope="scope">
							<span>{{scope.row.score?scope.row.score:'暂无评分'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="180">
					</el-table-column>
				</el-table>
				<div class="user-detail-page__pagination">
					<el-pagination @size-change="handleSizeChangebPl" @current-change="handleCurrentChangebPl"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="pinglunData.totalCount">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>
			<el-tab-pane label="我的下单" name="qiye">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="dingdanData.list">
					<el-table-column prop="ordersId" label="编号" width="80" fixed="left">
					</el-table-column>
					<el-table-column prop="userName" label="教练用户">
						<template slot-scope="scope">
							<span style="color: #008000;cursor: pointer;"
								@click="updates(scope.row.artificerUserId)">{{ scope.row.artificerName?scope.row.artificerName:'未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="ordersNo" label="订单编号">
					</el-table-column>
					<el-table-column prop="price" label="项目价格">
					</el-table-column>
					<el-table-column prop="couponName" label="优惠券名称">
					</el-table-column>
					<el-table-column prop="couponMoney" label="优惠券价格">
					</el-table-column>
					<el-table-column prop="artificerMoney" label="教练收益">
					</el-table-column>
					<el-table-column prop="payMoney" label="支付金额">
					</el-table-column>
					<el-table-column prop="taxiMoney" label="打车费">
					</el-table-column>
					<el-table-column prop="oneUserName" label="推广用户名称">
						<template slot-scope="scope">
							<span style="color: #008000;cursor: pointer;"
								@click="updates(scope.row.oneUserId)">{{ scope.row.oneUserName?scope.row.oneUserName:'未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="oneUserMoney" label="推广用户佣金">
					</el-table-column>
					<el-table-column prop="oneArtificerUserName" label="推广教练用户名">
						<template slot-scope="scope">
							<span style="color: #B94A48;cursor: pointer;"
								@click="updates(scope.row.oneArtificerUserId)">{{ scope.row.oneArtificerUserName?scope.row.oneArtificerUserName:'未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="oneArtificerMoney" label="推广教练佣金">
					</el-table-column>
					<el-table-column prop="addMoney" label="加钟价格">
					</el-table-column>
					<el-table-column prop="addTime" label="加钟时长">
					</el-table-column>
					<el-table-column prop="addNum" label="加钟次数">
					</el-table-column>
					<el-table-column prop="addArtificerMoney" label=" 加钟教练收益">
					</el-table-column>
					<el-table-column prop="sumArtificerMoney" label="教练总收益">
					</el-table-column>
					<el-table-column prop="sumMoney" label="订单总金额">
					</el-table-column>
					<el-table-column prop="payWay" label="支付方式">
						<template slot-scope="scope">
							<span v-if="scope.row.payWay==1">app微信</span>
							<span v-if="scope.row.payWay==2">微信公众号</span>
							<span v-if="scope.row.payWay==3">微信小程序</span>
							<span v-if="scope.row.payWay==4">支付宝</span>
							<span v-if="scope.row.payWay==5">零钱</span>
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="联系电话">
					</el-table-column>
					<el-table-column prop="serveTime" label="服务时间" width="100">
					</el-table-column>
					<el-table-column prop="startTime" label="开始服务时间">
					</el-table-column>
					<el-table-column prop="endTime" label="服务地址" width="200">
						<template slot-scope="scope">
							<span>{{scope.row.address}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="remark" label="备注">
					</el-table-column>
					<el-table-column prop="status" label="是否加钟" fixed="right">
						<template slot-scope="scope">
							<span style="color: #f56c6c;" v-if="scope.row.status==1">是</span>
							<span style="color: #999;" v-else>否</span>
						</template>
					</el-table-column>
					<el-table-column prop="status" label="订单状态" fixed="right">
						<template slot-scope="scope">
							<span style="color: #f56c6c;" v-if="scope.row.status==1">待支付</span>
							<span style="color: #E87D0D;" v-if="scope.row.status==2">待服务</span>
							<span style="color: #009900;" v-if="scope.row.status==3">待评价</span>
							<span style="color: #999;" v-if="scope.row.status==4">已取消</span>
							<span style="color: #999;" v-if="scope.row.status==5">已完成</span>
							<span style="color: red;" v-if="scope.row.status==6">服务中</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160">
					</el-table-column>
					<el-table-column prop="createTime" fixed="right" label="操作" width="100">
						<template slot-scope="scope">
							<el-button style="margin:5px;" size="mini" type="primary" icon="document"
								@click="selectBtn(scope.row)">加钟记录
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div class="user-detail-page__pagination">
					<el-pagination @size-change="handleSizeChangeXd" @current-change="handleCurrentChangeXd"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="dingdanData.totalCount">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane> -->
			<!-- <el-tab-pane label="我的接单" name="jiedan" v-if="tableData.isAuthentication==2">
				<el-table v-loading="tableDataLoading" :data="jiedanData.list">
					<el-table-column prop="ordersId" label="编号" width="80" fixed="left">
					</el-table-column>
					<el-table-column prop="userName" label="下单用户昵称">
						<template slot-scope="scope">
							　<span>{{scope.row.userName?scope.row.userName:'未绑定'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="artificerImg" label="教练头像">
						<template slot-scope="scope">
							<div v-if="scope.row.artificerImg &&scope.row.artificerImg!=''"
								style="display: inline-block; margin: 3px;">
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px" :src="scope.row.artificerImg" alt=""
										slot="reference">
									<img style="width: 300px; height: auto" :src="scope.row.artificerImg" alt="">
								</el-popover>
							</div>
							<span v-else>暂无图片</span>
						</template>
					</el-table-column>
					<el-table-column prop="ordersNo" label="订单编号">
					</el-table-column>
					<el-table-column prop="payMoney" label="支付金额">
					</el-table-column>
					<el-table-column prop="payWay" label="支付方式">
						<template slot-scope="scope">
							<span v-if="scope.row.payWay==1">app微信</span>
							<span v-if="scope.row.payWay==2">微信公众号</span>
							<span v-if="scope.row.payWay==3">微信小程序</span>
							<span v-if="scope.row.payWay==4">支付宝</span>
							<span v-if="scope.row.payWay==5">零钱</span>
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="联系电话">
					</el-table-column>
					<el-table-column prop="serveTime" label="服务时间" width="100">
					</el-table-column>
					<el-table-column prop="artificerMoney" label="教练收益">
					</el-table-column>
					<el-table-column prop="endTime" label="服务地址" width="200">
						<template slot-scope="scope">
							<span>{{scope.row.address}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="remark" label="备注">
					</el-table-column>
					<el-table-column prop="status" label="订单状态" fixed="right">
						<template slot-scope="scope">
							<span style="color: #f56c6c;" v-if="scope.row.status==1">待支付</span>
							<span style="color: #E87D0D;" v-if="scope.row.status==2">服务中</span>
							<span style="color: #009900;" v-if="scope.row.status==3">待评价</span>
							<span style="color: #999;" v-if="scope.row.status==4">已取消</span>
							<span style="color: #999;" v-if="scope.row.status==5">已完成</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160">
					</el-table-column>
				</el-table>
				<div class="user-detail-page__pagination">
					<el-pagination @size-change="handleSizeChangeJd" @current-change="handleCurrentChangeJd"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="jiedanData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane>
			<el-tab-pane label="我的项目" name="fabu" v-if="tableData.isAuthentication==2">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="takeDataXm">
					<el-table-column prop="massageTypeId" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="title" label="项目名称">
					</el-table-column>
					<el-table-column prop="massageImg" label="项目图片">
						<template slot-scope="scope">
							　　<img v-if="scope.row.massageImg &&scope.row.massageImg!=''" :src="scope.row.massageImg"
								width="40" height="40" />
							<span v-else>暂无图片</span>
						</template>
					</el-table-column>
					<el-table-column prop="contentImg" label="项目详情图" width="250">
						<template slot-scope="scope">
							<div v-if="scope.row.contentImg.length>0">
								<div v-for="(item,index) in scope.row.contentImg" :key="index"
									style="display: inline-block; margin: 3px;">
									<el-popover placement="top-start" title="" trigger="hover">
										<img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
										<img style="width: 300px; height: auto" :src="item" alt="">
									</el-popover>
								</div>
							</div>
							<div v-else>暂无图片</div>
						</template>
					</el-table-column>
					<el-table-column prop="region" label="城市" width="100">
						<template slot-scope="scope">
							<span v-if="scope.row.city">{{scope.row.city}}</span>
							<span v-else>不限</span>
						</template>
					</el-table-column>
					<el-table-column prop="oldPrice" label="原价">
					</el-table-column>
					<el-table-column prop="price" label="现价">
					</el-table-column>
					<el-table-column prop="memberPrice" label="会员价">
					</el-table-column>
					<el-table-column prop="artificerPrice" label="教练价">
					</el-table-column>
					<el-table-column prop="duration" label="时长">
					</el-table-column>
					<el-table-column prop="labels" label="标签" width="200">
					</el-table-column>
					<el-table-column prop="sales" label="销量">
					</el-table-column>
					<el-table-column prop="content" label="详情" width="300">
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="180">
					</el-table-column>
				</el-table>
				</div>
			</el-tab-pane>
			<el-tab-pane label="项目统计" name="shouyi" v-if="tableData.isAuthentication==2">
				<div style="display: inline-block;">
					<span>开始时间：</span>
					<el-date-picker style="width: 160px;margin-left: 10px;" v-model="startTime" align="right"
						type="datetime" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间">
					</el-date-picker>&nbsp;&nbsp;&nbsp;
					<span>截止时间：</span>
					<el-date-picker style="width: 160px;margin-left: 10px;" v-model="endTime" align="right"
						type="datetime" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择截止时间">
					</el-date-picker>
					<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="timeDate">查询
					</el-button>
				</div>
				<div>
					<el-row>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">总收益</div>
									<div class="text_color">
										<span>{{guanzhuData.sumMoney ? guanzhuData.sumMoney : 0}}</span>元
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">总销量</div>
									<div class="text_color">
										<span>{{guanzhuData.countOrderNum ? guanzhuData.countOrderNum : 0}}</span>单
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">用户评价数</div>
									<div class="text_color">
										<span>{{guanzhuData.countTakingByUserId ? guanzhuData.countTakingByUserId : 0}}</span>个
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">订单总数</div>
									<div class="text_color">
										<span>{{guanzhuData.countOrder ? guanzhuData.countOrder : 0}}</span>单
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">退款金额</div>
									<div class="text_color">
										<span>{{guanzhuData.sumRefund ? guanzhuData.sumRefund : 0}}</span>元
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">访问人数</div>
									<div class="text_color">
										<span>{{guanzhuData.countByUserId ? guanzhuData.countByUserId : 0}}</span>人
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">服务中的订单</div>
									<div class="text_color">
										<span>{{guanzhuData.countOrderIng ? guanzhuData.countOrderIng : 0}}</span>单
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">待评价订单</div>
									<div class="text_color">
										<span>{{guanzhuData.countOrderAppraise ? guanzhuData.countOrderAppraise : 0}}</span>单
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">已取消订单</div>
									<div class="text_color">
										<span>{{guanzhuData.countOrderCancel ? guanzhuData.countOrderCancel : 0}}</span>单
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">已完成订单</div>
									<div class="text_color">
										<span>{{guanzhuData.countOrderEnd ? guanzhuData.countOrderEnd : 0}}</span>单
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">上架中(我的项目)</div>
									<div class="text_color">
										<span>{{guanzhuData.putaway ? guanzhuData.putaway : 0}}</span>个
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">已下架(我的项目)</div>
									<div class="text_color">
										<span>{{guanzhuData.soldOut ? guanzhuData.soldOut : 0}}</span>个
									</div>
								</div>
							</div>
						</el-col>
					</el-row>
				</div>
			</el-tab-pane>
			<el-tab-pane label="教练信用分明细" name="xinyongfenJl">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="tableDataXyf.list">
					<el-table-column prop="id" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="title" label="标题">
					</el-table-column>
					<el-table-column prop="content" label="内容">
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间" width="100"></el-table-column>
				</el-table>
				<div class="user-detail-page__pagination">
					<el-pagination @size-change="handleSizeChangeXyfJl" @current-change="handleCurrentChangeXyfJl"
						:page-sizes="[10, 20, 30, 40]" :page-size="size1" :current-page="page1"
						layout="total,sizes, prev, pager, next,jumper" :total="tableDataXyf.totalCount">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>
			<el-tab-pane label="教练位置记录" name="weizhi">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="tableDataWz.list">
					<el-table-column fixed prop="id" label="编号" width="80">
						<template slot-scope="scope">
							<span>
								{{scope.$index+1}}
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="电话" width="160">
					</el-table-column>
					<el-table-column prop="address" label="地址"></el-table-column>
					<el-table-column prop="longitude" label="经度" width="160">
					</el-table-column>
					<el-table-column prop="latitude" label="纬度" width="160">
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
				</el-table>
				<div class="user-detail-page__pagination">
					<el-pagination @size-change="handleSizeChangeWz" @current-change="handleCurrentChangeWz"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="tableDataWz.totalCount">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane> -->
			<!-- 我的点歌 -->
			<!-- <el-tab-pane label="我的点歌" name="diange">
				<el-table v-loading="tableDataLoading" :data="tableDataDg.list">
					<el-table-column prop="musicPlayId" label="编号" width="80" fixed="left">
					</el-table-column>
					<el-table-column prop="tearoomName" label="门店名称" fixed="left">
					</el-table-column>
					<el-table-column prop="musicName" label="歌曲名称"  width="150">
					</el-table-column>
					<el-table-column prop="musicUrl" label="歌曲地址" width="350">
						<template slot-scope="scope">
							<audio :id="'myAudio'+scope.row.musicLibraryId"
								v-if="scope.row.musicUrl!=''&&scope.row.musicUrl!=null&&scope.row.musicUrl!='null'"
								duration :src="scope.row.musicUrl" type="audio/wav" controls preload="metadata"></audio>
							<span v-else>暂无语音</span>
						</template>
					</el-table-column>
					<el-table-column prop="musicAuthor" label="歌曲作者" width="150">
					</el-table-column>
					<el-table-column prop="musicPicUrl" label="歌曲封面图" width="80">
						<template slot-scope="scope">
							　　<img v-if="scope.row.musicPicUrl &&scope.row.musicPicUrl!=''" :src="scope.row.musicPicUrl"
								width="40" height="40" />
							<span v-else>暂无图片</span>
						</template>
					</el-table-column>
					<el-table-column prop="status" label="是否已播放" width="80" fixed="right">
						<template slot-scope="scope">
							<span v-if="scope.row.status==1">是</span>
							<span v-if="scope.row.status==2">否</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160">
					</el-table-column>
				</el-table>
				<div class="user-detail-page__pagination">
					<el-pagination @size-change="handleSizeChangeDg" @current-change="handleCurrentChangeDg"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="tableDataDg.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane> -->
			<!-- 收藏资讯列表 -->
			<!-- <el-tab-pane label="收藏资讯列表" name="shoucangzixun">
				<el-table v-loading="tableDataLoading" :data="tableDataZx.records">
					<el-table-column fixed="left" prop="informationId" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="tearoomName" fixed="left" label="台球馆名称" width="120">
					</el-table-column>
					<el-table-column prop="phone" label="电话">
					</el-table-column>
					<el-table-column prop="informationName" label="资讯名称">
					</el-table-column>
					<el-table-column prop="informationCoverImage" label="封面图">
						<template slot-scope="scope">
							<span v-if="scope.row.informationCoverImage == null">暂无图片</span>
							<img v-else :src="scope.row.informationCoverImage" alt="" width="40" height="40">
						</template>
					</el-table-column>
					<el-table-column prop="informationImage" label="详情图" width="200">
						<template slot-scope="scope">
							<div v-if="scope.row.informationImage">
								<span v-for="(item,index) in scope.row.informationImage.split(',')">
									<el-popover placement="top-start" title="" trigger="hover">
										<img style="width: 50px; height: 50px;margin: 3px;" :src="item" alt="" slot="reference">
										<img style="width: 300px; height: 300px" :src="item" alt="">
									</el-popover>
								</span>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="informationLabel" label="标签">
					</el-table-column>
					<el-table-column prop="informationPrice" label="价格">
					</el-table-column>
					<el-table-column prop="store_address" label="地址" width="200">
						<template slot-scope="scope">
							<div>
								{{scope.row.province}}{{scope.row.city}}{{scope.row.district}}{{scope.row.address}}
							</div>
							<div>经度：{{scope.row.longitude ? scope.row.longitude : '未绑定'}}</div>
							<div>纬度：{{scope.row.latitude ? scope.row.latitude : '未绑定'}}</div>
						</template>
					</el-table-column>
					<el-table-column prop="informationDetails" label="详情">
						<template slot-scope="scope">
							<el-popover placement="top-start" title="" trigger="hover">
								<div style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;"
									slot="reference">{{scope.row.informationDetails}}</div>
								<div style="width: 300px;">{{scope.row.informationDetails}}</div>
							</el-popover>
						</template>
					</el-table-column>
					<el-table-column prop="status" fixed="right" label="状态" width="80">
						<template slot-scope="scope">
							<span style="color: #13ce66;" v-if="scope.row.status == 1">正常</span>
							<span style="color: #F56C6C;" v-if="scope.row.status == 2">下架</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160">
					</el-table-column>
				</el-table>
				<div class="user-detail-page__pagination">
					<el-pagination @size-change="handleSizeChangeZx" @current-change="handleCurrentChangeZx"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="tableDataZx.total">
					</el-pagination>
				</div>
			</el-tab-pane> -->
			<!--  -->
			<!-- 修改金额 -->
			<el-dialog title="修改金额" :visible.sync="dialogFormVisible1" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">修改类型：</span>
					<el-radio-group v-model="type">
						<el-radio :label="1">添加</el-radio>
						<el-radio :label="2">减少</el-radio>
					</el-radio-group>
				</div>
				<div style="margin-bottom: 10px;" v-if="indexType==1&&type==1">
					<span style="width: 200px;display: inline-block;text-align: right;">是否设置会员：</span>
					<el-radio-group v-model="isVip">
						<el-radio :label="1">是</el-radio>
						<el-radio :label="2">否</el-radio>
					</el-radio-group>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">金额：</span>
					<el-input style="width:50%;" v-model="money" type="number" :min="0" :max="100" :controls="false"
						placeholder="请输入金额"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible1 = false">取 消</el-button>
					<el-button type="primary" v-if="tableDataLoadingje" v-loading="tableDataLoadingje">确 定</el-button>
					<el-button type="primary" @click="StairNoticeToYe()" v-if="!tableDataLoadingje">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 修改用户身份 -->
			<el-dialog title="修改身份" :visible.sync="dialogFormVisibleuser" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">修改类型：</span>
					<el-radio-group v-model="typeuser">
						<el-radio :label="1">用户人员</el-radio>
						<el-radio :label="3">保洁人员</el-radio>
					</el-radio-group>
				</div>
				<div v-if="typeuser==3">
					<div style="margin:2% 0;display: inline-block;">
						<span>台球馆名称：</span>
						<el-input style="width: 150px;" @keydown.enter.native="selectchaguan" clearable
							placeholder="请输入台球馆名称" v-model="titleqqq"></el-input>
					</div>
					<el-table :data="tableDataqqq.list">
						<el-table-column fixed="left" prop="tearoomId" label="编号" width="80">
						</el-table-column>
						<el-table-column prop="tearoomName" fixed="left" label="台球馆名称" width="120">
						</el-table-column>
						<el-table-column prop="tearoomTitleImage" label="台球馆logo">
							<template slot-scope="scope">
								<span v-if="scope.row.tearoomTitleImage == null">暂无图片</span>
								<img v-else :src="scope.row.tearoomTitleImage" alt="" width="40" height="40">
							</template>
						</el-table-column>
						<el-table-column prop="tearoomCoverImage" label="台球馆轮播图" width="200">
							<template slot-scope="scope">
								<div v-if="scope.row.tearoomCoverImage">
									<span v-for="(item,index) in scope.row.tearoomCoverImage.split(',')">
										<el-popover placement="top-start" title="" trigger="hover">
											<img style="width: 50px; height: 50px;margin: 3px;" :src="item" alt=""
												slot="reference">
											<img style="width: 300px; height: 300px" :src="item" alt="">
										</el-popover>
									</span>
								</div>
							</template>
						</el-table-column>
						<el-table-column prop="startTime" label="营业时间" width="100">
							<template slot-scope="scope">
								<div>{{scope.row.startTime}}--{{scope.row.endTime}}</div>
							</template>
						</el-table-column>
						<el-table-column prop="score" label="台球馆评分">
						</el-table-column>
						<el-table-column prop="status" fixed="right" label="状态" width="80">
							<template slot-scope="scope">
								<span style="color: #13ce66;" v-if="scope.row.status == 1">正常</span>
								<span style="color: #F56C6C;" v-if="scope.row.status == 2">打烊</span>
								<span style="color: #5cc477;" v-if="scope.row.status == 3">今日已满</span>
							</template>
						</el-table-column>
						<el-table-column prop="createTime" label="创建时间" width="160">
						</el-table-column>
						<el-table-column fixed='right' label="操作" width="120">
							<template slot-scope="scope">
								<el-button size="mini" type="primary" :disabled="btnisShow"
									v-if="store_idT!=scope.row.store_id" @click="addbaojie(scope.row.tearoomId)">添加
								</el-button>
							</template>
						</el-table-column>
					</el-table>
				</div>
				<div slot="footer" class="dialog-footer" v-if="typeuser!=3">
					<el-button @click="dialogFormVisibleuser = false">取 消</el-button>
					<el-button type="primary" @click="StairNoticeTo1user()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 修改信息 -->
			<el-dialog title="修改信息" :visible.sync="dialogFormVisibleXx" center>
				<div style="margin-bottom: 10px;" v-if="typeT==1">
					<span style="width: 200px;display: inline-block;text-align: right;">一级佣金：</span>
					<el-input style="width:50%;" v-model="money" type="number" :min="0" :max="100" :controls="false"
						placeholder="请输入一级佣金"></el-input>
				</div>
				<div style="margin-bottom: 10px;" v-if="typeT==2">
					<span style="width: 200px;display: inline-block;text-align: right;">二级佣金：</span>
					<el-input style="width:50%;" v-model="money" type="number" :min="0" :max="100" :controls="false"
						placeholder="请输入二级佣金"></el-input>
				</div>
				<div style="margin-bottom: 10px;" v-if="typeT==3">
					<span style="width: 200px;display: inline-block;text-align: right;">用户昵称：</span>
					<el-input style="width:50%;" v-model="money" :controls="false" placeholder="请输入用户昵称"></el-input>
				</div>
				<div style="margin-bottom: 10px;" v-if="typeT==4">
					<span style="width: 200px;display: inline-block;text-align: right;">手机号：</span>
					<el-input style="width:50%;" v-model="money" :controls="false" placeholder="请输入手机号"></el-input>
				</div>
				<div style="margin-bottom: 10px;" v-if="typeT==5">
					<span style="width: 200px;display: inline-block;text-align: right;">头像：</span>
					<el-upload class="avatar-uploader" v-model="money" style="display: inline-block;"
						:action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
						:on-success="handleAvatarSuccess1">
						<img v-if="money" :src="money" class="avatar"
							style="border-radius: 6px;width:148px;height: 148px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon"></i>
					</el-upload>
				</div>
				<div style="margin-bottom: 10px;" v-if="typeT==6">
					<span style="width: 200px;display: inline-block;text-align: right;">邀请人邀请码：</span>
					<el-input style="width:50%;" v-model="money" :controls="false" placeholder="请输入邀请人邀请码"></el-input>
				</div>
				<div style="margin-bottom: 10px;" v-if="typeT==7">
					<span style="width: 200px;display: inline-block;text-align: right;">修改类型：</span>
					<el-radio-group v-model="type">
						<el-radio :label="1">添加</el-radio>
						<el-radio :label="2">减少</el-radio>
					</el-radio-group>
				</div>
				<div style="margin-bottom: 10px;" v-if="typeT==7">
					<span style="width: 200px;display: inline-block;text-align: right;">积分：</span>
					<el-input style="width:50%;" v-model="money" type="number" :min="0" :max="100" :controls="false"
						placeholder="请输入积分"></el-input>
				</div>
				<div style="margin-bottom: 10px;" v-if="typeT==8">
					<span style="width: 200px;display: inline-block;text-align: right;">会员编号：</span>
					<el-input style="width:50%;" v-model="money" :controls="false" placeholder="请输入会员编号"></el-input>
				</div>
				<div style="margin-bottom: 10px;" v-if="typeT==9">
					<span style="width: 200px;display: inline-block;text-align: right;">生日：</span>
					<el-date-picker style="width: 50%;" v-model="money" align="right" type="date" format="MM-dd"
						value-format="MM-dd" placeholder="选择生日">
					</el-date-picker>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisibleXx = false">取 消</el-button>
					<el-button type="primary" @click="StairNoticeToXx()">确 定</el-button>
				</div>
			</el-dialog>
			<el-dialog :title="titles" :visible.sync="dialogFormVisible2" center>
				<div style="margin-bottom: 10px;" v-if="titles=='次数'||titles=='门票'">
					<span style="width: 200px;display: inline-block;text-align: right;">修改类型：</span>
					<el-radio-group v-model="type">
						<el-radio :label="1">添加</el-radio>
						<el-radio :label="2">减少</el-radio>
					</el-radio-group>
				</div>
				<div style="margin-bottom: 10px;display:flex;flex-direction: row;flex-wrap: wrap;" v-if="titles=='次数'">
					<span style="width: 200px;display: inline-block;text-align: right;">次数：</span>
					<el-input style="width:50%;" v-model="age" :controls="false" :placeholder="titles"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible2 = false">取 消</el-button>
					<el-button type="primary" @click="StairNoticeTo4()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 报名列表 -->
			<el-dialog title="报名列表" :visible.sync="dialogFormVisibleBm" center width="80%">
				<el-table width="780px" v-loading="tableDataLoading4" :data="baomingData.list">
					<el-table-column label="编号" prop="activityApplyId" width="80">
					</el-table-column>
					<el-table-column prop="nickName" label="昵称">
						<template slot-scope="scope">
							<span
								v-if="scope.row.activity">{{scope.row.activity.userEntity? scope.row.activity.userEntity.userName : '未绑定'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="手机号">
						<template slot-scope="scope">
							<span
								v-if="scope.row.activity">{{scope.row.activity.userEntity? scope.row.activity.userEntity.phone : '未绑定'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="性别">
						<template slot-scope="scope">
							<span>{{scope.row.sex==1 ? '男' : '女'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="role" label="角色">
						<template slot-scope="scope">
							<span v-if="scope.row.role==1">猫</span>
							<span v-else-if="scope.row.role==2">老鼠</span>
							<span v-else>未分配角色</span>
						</template>
					</el-table-column>
					<el-table-column prop="ordersNo" label="订单号">
					</el-table-column>
					<el-table-column prop="payMoney" label="支付金额">
						<template slot-scope="scope">
							　<span style="color: red;cursor: pointer;"
								@click="updatesvideo(scope.row.payMoney)">{{scope.row.payMoney?scope.row.payMoney:'0'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="nickName" label="一级佣金">
						<template slot-scope="scope">
							　<span style="color: red;cursor: pointer;"
								@click="updatesvideo(scope.row.zhiUserId)">{{scope.row.zhiMoney?scope.row.zhiMoney:'0'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhiUserName" label="一级用户昵称">
						<template slot-scope="scope">
							　<span style="color: #13ce66;cursor: pointer;"
								@click="updatesvideo(scope.row.zhiUserId)">{{scope.row.zhiUserName?scope.row.zhiUserName:'-'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="nickName" label="二级佣金">
						<template slot-scope="scope">
							　<span style="color: red;cursor: pointer;"
								@click="updatesvideo(scope.row.feiUserId)">{{scope.row.feiMoney?scope.row.feiMoney:'0'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="feiUserName" label="二级用户昵称">
						<template slot-scope="scope">
							　<span style="color: #13ce66;cursor: pointer;"
								@click="updatesvideo(scope.row.zhiUserId)">{{scope.row.feiUserName?scope.row.feiUserName:'-'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="pingMoney" label="平台佣金">
						<template slot-scope="scope">
							　<span
								style="color: red;cursor: pointer;">{{scope.row.pingMoney?scope.row.pingMoney:'0'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间">
					</el-table-column>
					<el-table-column prop="runDistances" label="运动距离">
					</el-table-column>
					<el-table-column prop="reviveNum" label="复活次数">
					</el-table-column>
					<el-table-column label="状态" width="120">
						<template slot-scope="scope">
							<span v-if="scope.row.status==1" style="color: #f56c6c;">已报名</span>
							<span v-if="scope.row.status==2" style="color: #5cc477;">游戏中</span>
							<span v-if="scope.row.status==3" style="color: #f56c6c;">已结束</span>
							<span v-if="scope.row.status==4" style="color: chocolate;">已签到</span>
							<span v-if="scope.row.status==5" style="color: #999;">已取消</span>
						</template>
					</el-table-column>
				</el-table>
				<div class="user-detail-page__pagination">
					<el-pagination @size-change="handleSizeChangeBm" @current-change="handleCurrentChangeBm"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limitB" :current-page="pageB"
						layout="total,sizes, prev, pager, next,jumper" :total="baomingData.totalCount">
					</el-pagination>
				</div>
			</el-dialog>
			<!-- 签到列表 -->
			<el-dialog title="签到列表" :visible.sync="dialogFormVisibleQd" center width="80%">
				<el-table v-loading="tableDataLoading" :data="baomingData.records">
					<el-table-column prop="id" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="userName" label="签到用户">
						<template slot-scope="scope">
							　<span v-if="scope.row.userName" style="color: #5cc477;cursor: pointer;"
								@click="updates1(scope.row.userId)">
								{{scope.row.userName? scope.row.userName : '未绑定'}}
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="手机号">
					</el-table-column>
					<el-table-column prop="title" label="活动名称">
					</el-table-column>
					<el-table-column prop="createAt" label="签到时间">
					</el-table-column>
				</el-table>
				<div class="user-detail-page__pagination">
					<el-pagination @size-change="handleSizeChangeQd" @current-change="handleCurrentChangeQd"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limitB" :current-page="pageB"
						layout="total,sizes, prev, pager, next,jumper" :total="baomingData.total">
					</el-pagination>
				</div>
			</el-dialog>
			<!-- 活动相册  -->
			<el-dialog title="活动相册" :visible.sync="dialogFormVisibleXc" center width="80%">
				<el-table v-loading="tableDataLoadingB" :data="baomingData.list">
					<el-table-column prop="activityPhotoId" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="userName" label="上传用户" width="120">
						<template slot-scope="scope">
							　<span v-if="scope.row.userName" style="color: #5cc477;cursor: pointer;"
								@click="updates1(scope.row.userId)">
								{{scope.row.userName? scope.row.userName : '未绑定'}}
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="手机号" width="120">
					</el-table-column>
					<el-table-column prop="photo" label="上传图片">
						<template slot-scope="scope">
							<div v-if="scope.row.photo">
								<div v-for="(item,index) in scope.row.photo.split(',')" :key="index"
									style="display: inline-block; margin: 3px;">
									<el-popover placement="top-start" title="" trigger="hover">
										<img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
										<img style="width: 300px; height: auto" :src="item" alt="">
									</el-popover>
								</div>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="上传时间" width="160">
					</el-table-column>
				</el-table>
				<div class="user-detail-page__pagination">
					<el-pagination @size-change="handleSizeChangeXc" @current-change="handleCurrentChangeXc"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limitB" :current-page="pageB"
						layout="total,sizes, prev, pager, next,jumper" :total="baomingData.totalCount">
					</el-pagination>
				</div>
			</el-dialog>
			<el-dialog title="视频列表" :visible.sync="dialogFormVisibleVido" center width="80%">
				<div style="text-align: right;">
					<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="vidoSx">
						刷新
					</el-button>
				</div>
				<div v-for="(item,index) in choicenData.records" :key="index"
					style="display: inline-block;width: 20%;padding: 10px;">
					<video style="width: 100%;height: auto;" :src="item.videoUrl" type="audio/wav"
						controls="controls"></video>
					<div style="font-size: 12px;color: #666;">编号：{{item.ordersVideoId}}</div>
					<div style="color: #999999;font-size: 10px;">视频时间：{{item.videoTime}}</div>
				</div>
				<div class="user-detail-page__pagination">
					<el-pagination @size-change="handleSizeChangeV" @current-change="handleCurrentChangeV"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="sizeF" :current-page="pageF"
						layout="total,sizes, prev, pager, next,jumper" :total="choicenData.total">
					</el-pagination>
				</div>
			</el-dialog>
			<!-- 修改会员 -->
			<el-dialog title="修改会员" :visible.sync="dialogFormVisible3" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">会员等级：</span>
					<el-select v-model="vipIndex" style="width:200px;margin-left: 10px;">
						<el-option v-for="(item,index) in tableDataVip" :key="index" :value="item.vipIndex"
							:label="item.vipName">
							<!-- 等级{{item.vipIndex}}:{{item.vipName}} -->
						</el-option>
					</el-select>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible3 = false">取 消</el-button>
					<el-button type="primary" @click="StairNoticeTo3()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 修改信用分 -->
			<el-dialog title="修改信用分" :visible.sync="dialogFormVisibleXyf" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">修改类型：</span>
					<el-radio-group v-model="type">
						<el-radio :label="1">添加</el-radio>
						<el-radio :label="2">减少</el-radio>
					</el-radio-group>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">信用分：</span>
					<el-input style="width:50%;" v-model="money" type="number" :min="0" :max="100" :controls="false"
						placeholder="请输入信用分"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">理由：</span>
					<el-input style="width:50%;" v-model="content" type="textarea" :rows="4" :controls="false"
						placeholder="请输入理由"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisibleXyf = false">取 消</el-button>
					<el-button type="primary" @click="StairNoticeToXyf()">确 定</el-button>
				</div>
			</el-dialog>
			<el-dialog title="约球成员" :visible.sync="dialogFormVisibleYq" center width="80%">
				<div style="margin:10px;display: inline-block;">
					<el-button size="mini" type="primary" @click="selectYy()">刷新</el-button>
					<!-- <el-button size="mini" type="primary" @click="clearYy()">重置</el-button> -->
				</div>
				<el-table v-loading="tableDataLoadingPt" :data="tableDataUder.list">
					<el-table-column prop="dateBallId" label="编号" width="80"></el-table-column>
					<el-table-column prop="userName" label="昵称">
						<template slot-scope="scope">
							<span style="color: #f56c6c;"
								@click="updates(scope.row.userId)">{{ scope.row.userName ? scope.row.userName : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column label="图像">
						<template slot-scope="scope">
							<img v-if="scope.row.avatar==null||scope.row.avatar==''" src="../../assets/img/avatar.png"
								alt="" width="40" height="40">
							<img v-else :src="scope.row.avatar" alt="" width="40" height="40">
						</template>
					</el-table-column>
					<!-- <el-table-column prop="phone" label="手机号">
					</el-table-column>
					<el-table-column prop="age" label="年龄">
					</el-table-column> -->
					<el-table-column prop="createTime" label="创建时间">
					</el-table-column>
					<el-table-column label="状态" width="120">
						<template slot-scope="scope">
							<span v-if="scope.row.status==1" style="color: #f56c6c;">待签到</span>
							<span v-if="scope.row.status==2" style="color: #5cc477;">已签到</span>
							<span v-if="scope.row.status==3" style="color: #f56c6c;">已结束</span>
							<span v-if="scope.row.status==4" style="color: #999;">已取消</span>
						</template>
					</el-table-column>
				</el-table>
				<div class="user-detail-page__pagination">
					<el-pagination @size-change="handleSizeChangeYy" @current-change="handleCurrentChangeYy"
						:page-sizes="[5, 10, 15, 20]" :page-size="limitU" :current-page="pageU"
						layout="total,sizes, prev, pager, next,jumper" :total="tableDataUder.totalCount">
					</el-pagination>
				</div>
			</el-dialog>
			<!-- 添加 会员值 -->
			<el-dialog title="添加会员值" :visible.sync="dialogFormVisibleHyz" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">类型：</span>
					<el-radio-group v-model="type">
						<el-radio :label="1">充值</el-radio>
						<el-radio :label="2">消费</el-radio>
					</el-radio-group>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">会员值：</span>
					<el-input style="width:50%;" v-model="money" type="number" :min="0" :max="100" :controls="false"
						placeholder="请输入会员值"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisibleHyz = false">取 消</el-button>
					<el-button type="primary" @click="StairNoticeToHyz()" v-if="!Loading">确 定</el-button>
					<el-button type="primary" v-else v-loading="Loading">确 定</el-button>
				</div>
			</el-dialog>

			<!-- 教练模块 弹框 -->
			<!-- 修改教练信息 -->
			<el-dialog title="修改教练信息" :visible.sync="dialogFormVisible10" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">教练名称：</span>
					<el-input style="width:50%;" v-model="artificerName" type="text" placeholder="请输入教练名称"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">简介：</span>
					<el-input style="width:50%;" v-model="content" type="text" placeholder="请输入简介"></el-input>
				</div>
				<!-- <div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">接单开始时间：</span>
					<el-date-picker style="width: 50%;margin-left: 10px;" v-model="beginTime" align="right"
						type="datetime"  format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择时间">
					</el-date-picker>&nbsp;&nbsp;&nbsp;
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">接单结束时间：</span>
					<el-date-picker style="width: 50%;margin-left: 10px;" v-model="finishTime" align="right"
						type="datetime"  format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择时间">
					</el-date-picker>&nbsp;&nbsp;&nbsp;
				</div> -->
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">城市：</span>
					<el-input style="width:50%;" v-model="city" type="text" placeholder="请输入城市"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">经度：</span>
					<el-input style="width:50%;" v-model="longitude" type="text" placeholder="请输入经度"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">纬度：</span>
					<el-input style="width:50%;" v-model="latitude" type="text" placeholder="请输入纬度"></el-input>
				</div>
				<div style="margin-bottom: 10px;display:flex;">
					<span style="width: 200px;display: inline-block;text-align: right;">项目类型：</span>
					<el-select v-model="classifyId" style="width:50%;margin-left: 10px;">
						<el-option v-for="item in fwData" :key="item.id" :label="item.value" :value="item.id">
						</el-option>
					</el-select>&nbsp;&nbsp;
				</div>
				<div style="margin-bottom: 10px;display:flex;">
					<span style="width: 200px;display: inline-block;text-align: right;">是否优选：</span>
					<el-radio-group v-model="isGoods">
						<el-radio :label="1">是</el-radio>
						<el-radio :label="2">否</el-radio>
					</el-radio-group>
				</div>
				<div style="margin-bottom: 10px;display:flex;">
					<span style="width: 200px;display: inline-block;text-align: right;">是否是高级教练：</span>
					<el-radio-group v-model="isHot">
						<el-radio :label="1">是</el-radio>
						<el-radio :label="2">否</el-radio>
					</el-radio-group>
				</div>
				<div style="margin-bottom: 10px;display:flex;">
					<span style="width: 200px;display: inline-block;text-align: right;">出行方式：</span>
					<el-radio-group v-model="tripWay">
						<el-radio :label="1">公交</el-radio>
						<el-radio :label="2">出租</el-radio>
						<el-radio :label="3">免费</el-radio>
					</el-radio-group>
				</div>
				<!-- <div style="margin-bottom: 10px;display:flex;">
					<span style="width: 200px;display: inline-block;text-align: right;">是否到店：</span>
					<el-radio-group v-model="isShop">
						<el-radio :label="1">是</el-radio>
						<el-radio :label="2">否</el-radio>
					</el-radio-group>
				</div>
				<div style="margin-bottom: 10px;display:flex;">
					<span style="width: 200px;display: inline-block;text-align: right;">是否上门：</span>
					<el-radio-group v-model="isHome">
						<el-radio :label="1">是</el-radio>
						<el-radio :label="2">否</el-radio>
					</el-radio-group>
				</div> -->
				<!-- <div style="margin-bottom: 10px;display:flex;">
					<span style="width: 200px;display: inline-block;text-align: right;">性别限制：</span>
					<el-radio-group v-model="isSex">
						<el-radio :label="0">不限制</el-radio>
						<el-radio :label="1">男</el-radio>
						<el-radio :label="2">女</el-radio>
					</el-radio-group>
				</div> -->
				<div style="margin-bottom: 10px;display:flex;">
					<span style="width: 200px;display: inline-block;text-align: right;">教练头像：</span>
					<div
						style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
						<el-upload class="avatar-uploader" v-model="artificerImg"
							:action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
							:on-success="handleAvatarSuccess1Jl" :on-progress="onprogress2">
							<!-- <el-progress type="circle" :percentage="100" status="success"></el-progress> -->
							<img v-if="artificerImg!=''" :src="artificerImg" class="avatar"
								style="border-radius: 6px;width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon iconss"></i>
							<!-- <el-progress v-if="percentage2>0&&percentage2<100" type="circle" :percentage="percentage2">
							</el-progress> -->
						</el-upload>
					</div>
				</div>
				<div style="margin-bottom: 10px;display:flex;">
					<span style="width: 200px;display: inline-block;text-align: right;">证书：</span>
					<div class="imgs" v-for="(item,index) in certificate" :key="index">
						<img width="100%" class="images" height="100%" :src="item" alt="">
						<span class="dels">
							<i class="el-icon-delete" @click="clear1(index)"></i>
						</span>
					</div>
					<div class="imgs" style="width: 50%;">
						<el-upload :action="$http.adornUrlUp('alioss/uploadWatermark')" list-type="picture-card"
							:show-file-list="false" :on-success="handleUploadSuccess3" :on-progress="onprogress1">
							<el-progress v-if="percentage1>0 && percentage1<100" type="circle"
								:percentage="percentage1">
							</el-progress>
							<i v-else class="el-icon-plus"></i>
						</el-upload>
					</div>
				</div>
				<div style="margin-bottom: 10px;display:flex;">
					<span style="width: 200px;display: inline-block;text-align: right;">教练生活照：</span>
					<div class="imgs" v-for="(item,index) in lifePhoto" :key="index">
						<img width="100%" class="images" height="100%" :src="item" alt="">
						<span class="dels">
							<i class="el-icon-delete" @click="clear2(index)"></i>
						</span>
					</div>
					<div class="imgs" style="width: 50%;">
						<el-upload :action="$http.adornUrlUp('alioss/upload')" list-type="picture-card"
							:show-file-list="false" :on-success="handleUploadSuccess4" :on-progress="onprogress1">
							<el-progress v-if="percentage1>0 && percentage1<100" type="circle"
								:percentage="percentage1">
							</el-progress>
							<i v-else class="el-icon-plus"></i>
						</el-upload>
					</div>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible10 = false">取 消</el-button>
					<el-button type="primary" @click="refusetoUp()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 修改忙时 -->
			<el-dialog title="修改忙时" :visible.sync="dialogFormVisibleMs" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">日期：</span>
					<el-date-picker style="width: 160px;margin-left: 10px;" v-model="artificerDates" align="right"
						type="date" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择时间"
						@change="mangshi(artificerDates)">
					</el-date-picker>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">时间：</span>
					<el-checkbox-group v-model="times">
						<el-checkbox v-for="(item,index) in timeLis" :key="index" :label="item.times"
							:disabled="item.classify==1">{{item.times}}</el-checkbox>
					</el-checkbox-group>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisibleMs = false">取 消</el-button>
					<el-button type="primary" @click="timesUp()">确 定</el-button>
				</div>
			</el-dialog>
			<!--  -->
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
							<span
								v-if="scope.row.type==1">{{scope.row.selfCouponUser?scope.row.selfCouponUser.couponName:'-'}}</span>
							<span v-else>-</span>
						</template>
					</el-table-column>

					<el-table-column prop="url" label="金额">
						<template slot-scope="scope">
							<span style="color: #f56c6c;">{{scope.row.url?scope.row.url:'-'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="createAt" label="操作时间" width="160">
					</el-table-column>
				</el-table>
				<div class="user-detail-page__pagination">
					<el-pagination @size-change="handleSizeChangeXf" @current-change="handleCurrentChangeXf"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="sizeU" :current-page="pageU"
						layout="total,sizes, prev, pager, next,jumper" :total="xufeiData.totalPage">
					</el-pagination>
				</div>
			</el-dialog>
			<!-- 不可提现添加金额 -->
			<el-dialog title="修改金额" :visible.sync="dialogFormVisibleBzj" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">修改类型：</span>
					<el-radio-group v-model="type">
						<el-radio :label="1">添加</el-radio>
						<el-radio :label="2">减少</el-radio>
					</el-radio-group>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">金额：</span>
					<el-input style="width:50%;" v-model="money" type="number" :min="0" :max="100" :controls="false"
						placeholder="请输入金额"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisibleBzj = false">取 消</el-button>
					<el-button type="primary" @click="StairNoticeTo1()">确 定</el-button>
				</div>
			</el-dialog>
		</el-tabs>
	</div>


</template>
<script>
	export default {
		data() {
			return {
				btnisShow: false,
				store_id: '',
				store_idT: '',
				tableDataqqq: [],
				openValue: 1,
				closeValue: 2,
				state: 1,
				size: 10,
				limit: 10,
				page: 1,
				userId: '',
				phone: '',
				totalnum: 0,
				startTime: '',
				endTime: '',
				flag: 1,
				content: '',
				title: '',
				status: 0,
				info: {
					stockDate: this.getNowTime(), //日期
				},
				info2: {
					stockDate2: this.getNowTime2(), //日期
				},
				tablenum: {},
				tableData: {},
				userCertification: {},
				userData: [],
				tableDatamin: [],
				withdrawData: [],
				integralData: [],
				walletData: [],
				rechargeData: [],
				DiscussData: [],
				activeName: 'first',
				tableDataLoading: true,
				dialogFormVisible1: false,
				dialogFormVisibleuser: false,
				typeuser: '',
				relationId: '',
				type: '',
				money: '',
				useryonghuid: '',
				titleqqq: '',
				tableDataYhq: {},
				typeT: 1,
				dialogFormVisibleXx: false,
				userEntity: {},
				states: 4,
				classify: 1,
				statusmain: [{
						value: 1,
						label: '一级'
					},
					{
						value: 2,
						label: '二级'
					},
				],
				tableDataLoadingje: false,
				isVip: 2,
				kamiData: {},
				indexType: 1,
				jifenData: {},
				dialogFormVisible2: false,
				titles: '',
				age: '',
				liwuData: {},
				tableDataQz: {},
				yuewanData: {},
				baomingData: {},

				activityId: '',
				limitB: 10,
				pageB: 1,
				dialogFormVisibleQd: false,
				dialogFormVisibleXc: false,
				// handleSizeChangeBm:false,
				dialogFormVisibleBm: false,
				tableDataLoading4: false,
				tableDataLoadingB: false,
				dialogFormVisibleVido: false,
				choicenData: {},
				ordersId: '',
				pageF: 1,
				sizeF: 10,
				dialogFormVisible3: false,
				tableDataVip: [],
				vipIndex: '',
				takeData: {},
				autonymData: {},
				tableDataXyf: {},
				size1: 10,
				page1: 1,
				dialogFormVisibleXyf: false,
				dialogFormVisibleYq: false,
				tableDataLoadingPt: false,
				tableDataUder: {},
				limitU: 10,
				pageU: 1,
				userNameU: '',
				phoneU: '',
				dateBallId: '',
				liuyanData: {},
				dizhiData: [],
				huiyuanzhiData: {},
				walletDataHyz: {},
				dialogFormVisibleHyz: false,
				Loading: false,
				// 教练模块 参数
				dialogFormVisible10: false,
				percentage1: 0,
				percentage2: 0,
				artificerXg: {},
				// 教练信息
				artificerName: '',
				content: '',
				beginTime: '',
				finishTime: '',
				longitude: '',
				latitude: '',
				artificerImg: '',
				certificate: [],
				lifePhoto: [],
				artificerId: '',
				fwData: [],
				classifyId: '',
				classType: '',
				isGoods: '',
				isHot: '',
				tripWay: '',
				city: '',
				isShop: '',
				isHome: '',
				isSex: 0,
				artificerDate: '',
				artificer: {},
				pingluData: {},
				pinglunData: {},
				dingdanData: {},
				jiedanData: {},
				takeDataXm: [],
				guanzhuData: {},
				tableDataWz: {},
				tableDataDg:{},
				tableDataZx:{},
				timeList: [],
				timeLis: [{
						times: '00:00',
						classify: 2
					}, {
						times: '01:00',
						classify: 2
					}, {
						times: '02:00',
						classify: 2
					}, {
						times: '03:00',
						classify: 2
					}, {
						times: '04:00',
						classify: 2
					},
					{
						times: '05:00',
						classify: 2
					}, {
						times: '06:00',
						classify: 2
					}, {
						times: '07:00',
						classify: 2
					}, {
						times: '08:00',
						classify: 2
					},
					{
						times: '09:00',
						classify: 2
					}, {
						times: '10:00',
						classify: 2
					}, {
						times: '11:00',
						classify: 2
					}, {
						times: '12:00',
						classify: 2
					},
					{
						times: '13:00',
						classify: 2
					}, {
						times: '14:00',
						classify: 2
					}, {
						times: '15:00',
						classify: 2
					}, {
						times: '16:00',
						classify: 2
					},
					{
						times: '17:00',
						classify: 2
					}, {
						times: '18:00',
						classify: 2
					}, {
						times: '19:00',
						classify: 2
					}, {
						times: '20:00',
						classify: 2
					},
					{
						times: '21:00',
						classify: 2
					}, {
						times: '22:00',
						classify: 2
					}, {
						times: '23:00',
						classify: 2
					},
				],
				timeLis2: [{
					times: '00:00',
					classify: 2
				}, {
					times: '00:30',
					classify: 2
				}, {
					times: '01:00',
					classify: 2
				}, {
					times: '01:30',
					classify: 2
				}, {
					times: '02:00',
					classify: 2
				}, {
					times: '02:30',
					classify: 2
				}, {
					times: '03:00',
					classify: 2
				}, {
					times: '03:30',
					classify: 2
				}, {
					times: '04:00',
					classify: 2
				}, {
					times: '04:30',
					classify: 2
				}, {
					times: '05:00',
					classify: 2
				}, {
					times: '05:30',
					classify: 2
				}, {
					times: '06:00',
					classify: 2
				}, {
					times: '06:30',
					classify: 2
				}, {
					times: '07:00',
					classify: 2
				}, {
					times: '07:30',
					classify: 2
				}, {
					times: '08:00',
					classify: 2
				}, {
					times: '08:30',
					classify: 2
				}, {
					times: '09:00',
					classify: 2
				}, {
					times: '09:30',
					classify: 2
				}, {
					times: '10:00',
					classify: 2
				}, {
					times: '10:30',
					classify: 2
				}, {
					times: '11:00',
					classify: 2
				}, {
					times: '11:30',
					classify: 2
				}, {
					times: '12:00',
					classify: 2
				}, {
					times: '12:30',
					classify: 2
				}, {
					times: '13:00',
					classify: 2
				}, {
					times: '13:30',
					classify: 2
				}, {
					times: '14:00',
					classify: 2
				}, {
					times: '14:30',
					classify: 2
				}, {
					times: '15:00',
					classify: 2
				}, {
					times: '15:30',
					classify: 2
				}, {
					times: '16:00',
					classify: 2
				}, {
					times: '16:30',
					classify: 2
				}, {
					times: '17:00',
					classify: 2
				}, {
					times: '17:30',
					classify: 2
				}, {
					times: '18:00',
					classify: 2
				}, {
					times: '18:30',
					classify: 2
				}, {
					times: '19:00',
					classify: 2
				}, {
					times: '19:30',
					classify: 2
				}, {
					times: '20:00',
					classify: 2
				}, {
					times: '20:30',
					classify: 2
				}, {
					times: '21:00',
					classify: 2
				}, {
					times: '21:30',
					classify: 2
				}, {
					times: '22:00',
					classify: 2
				}, {
					times: '22:30',
					classify: 2
				}, {
					times: '23:00',
					classify: 2
				}, {
					times: '23:30',
					classify: 2
				}, ],
				artificerDates: '',
				times: [],
				dialogFormVisibleMs: false,
				xufeiData: {},
				dialogFormVisibleXf: false,
				sizeU: 10,
				pageU: 1,
				ordersNo: '',
				dialogFormVisibleBzj: false,
				tablemoneyB: '',
				typeType: 1,
				dialogFormVisibleVip: false,


			}
		},
		methods: {
			// 返回上一级
			prev() {
				this.$router.back()
			},
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
			// 状态
			change(val, id) {
				this.$http({
					url: this.$http.adornUrl(`user/updateState/${id}`),
					method: 'post',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.teamSelect()
							}
						})
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
			// 删除评论
			deletes(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('comment/deleteComment'),
						method: 'post',
						params: this.$http.adornParams({
							'id': row.commentId
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
									this.userDiscuss()
								}
							})
						} else {
							this.$message({
								message: data.msg,
								type: 'warning',
								duration: 1500,
								onClose: () => {}
							})
						}
					})
				}).catch(() => {})
			},
			// 团长跳转详情页
			colonelClick(relationId) {
				this.$http({
					url: this.$http.adornUrl('user/selectUserByInvitationCode'),
					method: 'get',
					params: this.$http.adornParams({
						'invitationCode': relationId,
					})
				}).then(({
					data
				}) => {
					let returnData = data.data
					let id = returnData.userEntity.userId
					this.$router.push({
						path: '/userDetail1',
						query: {
							userId: id
						}
					})
					this.dataSelect()
				})
			},
			// 详情跳转
			updates(userId) {
				this.$router.push({
					path: '/userDetail1',
					query: {
						userId: userId
					}
				})
			},
			// tabs切换
			handleClick(tab, event) {
				if (tab._props.label == '用户详情') {
					this.dataSelect()
				}
				if (tab._props.label == '团队成员') {
					this.teamSelect()
				}
				if (tab._props.label == '用户消息') {
					this.states = 5
					this.userSelect1()
				}
				if (tab._props.label == '订单消息') {
					this.states = 4
					this.userSelect2()
				}
				if (tab._props.label == '消息推送') {
					this.userSelect4()
				}
				if (tab._props.label == '提现记录') {
					this.withdrawSelect()
				}
				if (tab._props.label == '我的积分') {
					this.integralSelect()
				}
				if (tab._props.label == '钱包明细') {
					this.walletSelect()
				}
				if (tab._props.label == '商家收益明细') {
					this.walletSelectShop()
				}
				if (tab._props.label == '分销收益明细') {
					this.walletSelectS()
				}
				if (tab._props.label == '支付明细') {
					this.rechargeSelect()
				}
				if (tab._props.label == '积分明细') {
					this.walletSelectJf()
				}
				if (tab._props.label == '用户评价') {
					this.userDiscuss()
				}
				if (tab._props.label == '我的团购券') {
					this.typeT = 1
					this.dataSelectYhq()
				}
				if (tab._props.label == '我的优惠券') {
					this.typeT = 2
					this.dataSelectYhq()
				}
				if (tab._props.label == '我的新人券') {
					this.typeT = 3
					this.dataSelectYhq()
				}
				if (tab._props.label == '兑换记录') {
					this.dataSelectDhq()
				}
				if (tab._props.label == '我的订单') {
					this.limit = 10
					this.page = 1
					this.dingdanSelect()
				}
				// if (tab._props.label == '兑换记录') {
				// 	this.page = 1
				// 	this.limit = 10
				// 	this.youhuiquanSelect()
				// }
				if (tab._props.label == '我的优惠卡') {
					this.page = 1
					this.limit = 10
					this.youhuikaSelect()
				}
				if (tab._props.label == '我的车辆') {
					this.dataSelectQz()
				}
				if (tab._props.label == '常用电站') {
					this.limit = 10
					this.page = 1
					this.lySelect()
				}
				if (tab._props.label == '我的报名') {
					this.limit = 10
					this.page = 1
					this.yuewanSelect()
				}
				if (tab._props.label == '店铺订单') {
					this.limit = 10
					this.page = 1
					this.takeSelect()
				}
				if (tab._props.label == '聊天记录') {
					this.page = 1
					this.limit = 10
					this.autonymSelect1()
				}
				if (tab._props.label == '信用分明细') {
					this.size1 = 10
					this.page1 = 1
					this.xinyongSelect()
				}
				if (tab._props.label == '发布的约球记录') {
					this.dataSelectYq()
				}
				if (tab._props.label == '参加的约球记录') {
					this.dataSelectYyyq()
				}
				if (tab._props.label == '我的留言') {
					this.limit = 10
					this.page = 1
					this.pinglunjiluSelect()
				}
				if (tab._props.label == '我的地址') {
					this.limit = 10
					this.page = 1
					this.dizhiSelect()
				}
				if (tab._props.label == '会员值明细') {
					this.walletSelectHyz()
				}
				// 教练模块------
				if (tab._props.label == '教练详情') {
					this.autonymSelect()
					this.mangshiSelect(this.artificerDate)
				}
				if (tab._props.label == '我的项目') {
					this.limit = 10
					this.page = 1
					this.state = 0
					this.authentication = 1
					this.takeSelect()
				}
				if (tab._props.label == '我的下单') {
					this.limit = 10
					this.page = 1
					this.state = 0
					this.roderSelect()
				}
				if (tab._props.label == '我的接单') {
					this.limit = 10
					this.page = 1
					this.state = 0
					this.jiedanSelect()
				}

				if (tab._props.label == '我的评价') {
					this.limit = 10
					this.page = 1
					this.state = 0
					this.jianglianPlSelect()
				}
				if (tab._props.label == '项目统计') {
					this.page = 1
					this.limit = 10
					this.flag = 1
					this.taskData()
				}
				if (tab._props.label == '教练信用分明细') {
					this.size1 = 10
					this.page1 = 1
					this.xinyongSelectJl()
				}
				if (tab._props.label == '收到的评价') {
					this.limit = 10
					this.page = 1
					this.pinglunSelect()
				}
				if (tab._props.label == '教练位置记录') {
					this.limit = 10
					this.page = 1
					this.dataSelectWz()
				}
				if (tab._props.label == '我的点歌') {
					this.limit = 10
					this.page = 1
					this.dataSelectDg()
				}
				if (tab._props.label == '收藏资讯列表') {
					this.limit = 10
					this.page = 1
					this.dataSelectZx()
				}
				
				

			},
			handleSizeChangeYw(val) {
				this.limit = val
				this.yuewanSelect()
			},
			handleCurrentChangeYw(val) {
				this.page = val
				this.yuewanSelect()
			},
			handleSizeChangeLw(val) {
				this.limit = val
				this.lySelect()
			},
			handleCurrentChangeLw(val) {
				this.page = val
				this.lySelect()
			},
			handleSizeChangeQz(val) {
				this.limit = val;
				this.dataSelectQz()
			},
			handleCurrentChangeQz(val) {
				this.page = val;
				this.dataSelectQz()
			},
			handleSizeChange1(val) {
				this.size = val
				this.userSelect1()
			},
			handleCurrentChange1(val) {
				this.page = val
				this.userSelect1()
			},
			handleSizeChange2(val) {
				this.size = val
				this.walletSelect()
			},
			handleCurrentChange2(val) {
				this.page = val
				this.walletSelect()
			},
			handleSizeChangeShop(val) {
				this.size = val
				this.walletSelectShop()
			},
			handleCurrentChangeShop(val) {
				this.page = val
				this.walletSelectShop()
			},
			handleSizeChangeSy(val) {
				this.size = val
				this.walletSelectS()
			},
			handleCurrentChangeSy(val) {
				this.page = val
				this.walletSelectS()
			},
			handleSizeChangeJf(val) {
				this.size = val
				this.walletSelectJf()
			},
			handleCurrentChangeJf(val) {
				this.page = val
				this.walletSelectJf()
			},
			handleSizeChange3(val) {
				this.size = val
				this.rechargeSelect()
			},
			handleCurrentChange3(val) {
				this.page = val
				this.rechargeSelect()
			},

			handleSizeChange11(val) {
				this.size = val
				this.withdrawSelect()
			},
			handleCurrentChange11(val) {
				this.page = val
				this.withdrawSelect()
			},

			handleSizeChangeYhq(val) {
				this.size = val;
				this.dataSelectYhq()
			},
			handleCurrentChangeYhq(val) {
				this.page = val;
				this.dataSelectYhq()
			},
			handleSizeChangeDhq(val) {
				this.size = val;
				this.dataSelectDhq()
			},
			handleCurrentChangeDhq(val) {
				this.page = val;
				this.dataSelectDhq()
			},
			handleSizeChangeOr(val) {
				this.limit = val
				this.dingdanSelect()
			},
			handleCurrentChangeOr(val) {
				this.page = val
				this.dingdanSelect()
			},
			handleSizeChangeYhk(val) {
				this.limit = val
				this.youhuikaSelect()
			},
			handleCurrentChangeYhk(val) {
				this.page = val
				this.youhuikaSelect()
			},
			handleSizeChangeTd(val) {
				this.size = val
				this.teamSelect()
			},
			handleCurrentChangeTd(val) {
				this.page = val
				this.teamSelect()
			},
			handleSizeChangeBm(val) {
				this.limitB = val
				this.baomingSelect()
			},
			handleCurrentChangeBm(val) {
				this.pageB = val
				this.baomingSelect()
			},
			handleSizeChangeQd(val) {
				this.limitB = val
				this.qiandaoSelect()
			},
			handleCurrentChangeQd(val) {
				this.pageB = val
				this.qiandaoSelect()
			},
			handleSizeChangeXc(val) {
				this.limitB = val
				this.xiangceSelect()
			},
			handleCurrentChangeXc(val) {
				this.pageB = val
				this.xiangceSelect()
			},
			handleSizeChange6(val) {
				this.limit = val
				this.takeSelect()
			},
			handleCurrentChange6(val) {
				this.page = val
				this.takeSelect()
			},
			handleSizeChange9(val) {
				this.limit = val
				this.autonymSelect1()
			},
			handleCurrentChange9(val) {
				this.page = val
				this.autonymSelect1()
			},
			handleSizeChangeXyf(val) {
				this.size1 = val
				this.xinyongSelect()
			},
			handleCurrentChangeXyf(val) {
				this.page1 = val
				this.xinyongSelect()
			},
			handleSizeChange(val) {
				this.limit = val;
				this.dataSelectyq()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.dataSelectyq()
			},
			handleSizeChangeYy(val) {
				this.limitU = val;
				this.dataSelectYy()
			},
			handleCurrentChangeYy(val) {
				this.pageU = val;
				this.dataSelectYy()
			},
			handleSizeChangeYyyq(val) {
				this.limit = val;
				this.dataSelectYyyq()
			},
			handleCurrentChangeYyyq(val) {
				this.page = val;
				this.dataSelectYyyq()
			},
			handleSizeChangePlLy(val) {
				this.limit = val
				this.pinglunjiluSelect()
			},
			handleCurrentChangePlLy(val) {
				this.page = val
				this.pinglunjiluSelect()
			},
			handleSizeChangeHyz(val) {
				this.size = val
				this.walletSelectHyz()
			},
			handleCurrentChangeHyz(val) {
				this.page = val
				this.walletSelectHyz()
			},
			timeDate2() {
				this.rechargeSelect()
			},
			selectchaguan() {
				this.shopSelectchaguan()
			},
			//授权
			accredit(id, val) {
				this.$confirm(`确定要授权会员权限吗?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/user/user_vip_grant'),
						method: 'post',
						params: this.$http.adornParams({
							user_id: id,
							type: val == 0 ? 1 : 0
						})
					}).then(({
						data
					}) => {
						this.$message({
							message: '修改成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					})
				}).catch(() => {})
			},
			// 更改状态
			stateChange(userId, type) {
				this.$confirm(`确定要更改用户状态吗?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`user/updateUserStatusByUserId?userId=${userId}`),
						method: 'get',
						data: this.$http.adornData({})
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
						} else {
							this.$message({
								message: data.msg,
								type: 'success',
								duration: 1500,
								onClose: () => {}
							})
						}
					})
				}).catch(() => {})
			},
			//获取台球馆
			shopSelectchaguan() {
				this.$http({
					url: this.$http.adornUrl('tearoom/selectTearoomList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': 100,
						'tearoomName': this.titleqqq,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableDataqqq = returnData
				})
			},
			//修改用户身份
			Changeuser(userId, type) {
				console.log(userId, type)
				if (this.tableData.tearoomId) {
					this.store_idT = this.tableData.tearoomId
					this.typeuser = 3
				} else {
					this.store_idT = ''
					this.typeuser = 1
				}
				this.store_id = ''
				this.useryonghuid = userId
				if (type == 2) {
					this.$notify({
						title: '警告',
						message: '改用户为商家无须更改身份',
						type: 'warning',
						duration: 1500,
					});
					this.dialogFormVisibleuser = false
				} else {
					this.dialogFormVisibleuser = true
					this.shopSelectchaguan()
				}
			},
			addbaojie(store_id) {
				this.store_id = store_id
				// this.btnisShow = true
				//调用倒计时方法
				this.StairNoticeTo1user();
			},
			StairNoticeTo1user() {
				if (this.typeuser == 3) {
					if (this.store_id == '') {
						this.$notify({
							title: '提醒',
							message: '请选择绑定的商户',
							type: 'warning',
							duration: 1500,
						});
						return
					}
				} else {
					this.store_id = 0
				}
				this.$http({
					url: this.$http.adornUrl('user/updateUserByUserId'),
					method: 'post',
					data: this.$http.adornData({
						'userId': this.useryonghuid,
						'tearoomId': this.store_id,
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
								this.dialogFormVisibleuser = false
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
			// 获取数据列表
			dataSelect() {
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('user/' + userId),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					if (data.code === 0) {
						let returnData = data.data;
						if (data.data.userEntity.vipIndex) {
							for (var j in this.tableDataVip) {
								if (data.data.userEntity.vipIndex == this.tableDataVip[j].vipIndex) {
									data.data.userEntity.vipName = this.tableDataVip[j].vipName
								}
							}
						}
						this.userEntity = returnData;
						this.tableData = returnData.userEntity
						this.phone = returnData.mobile
						if (returnData.userCertification) {
							this.userCertification = returnData.userCertification
						}
					}
				})
				// 获取积分
				this.$http({
					url: this.$http.adornUrl('integral/selectByUserId'),
					method: 'get',
					params: this.$http.adornParams({
						'userId': userId,
					})
				}).then(({
					data
				}) => {
					if (data.code === 0) {
						let returnData = data.data;
						this.userEntity = returnData;
						this.jifenData = returnData
					}
				})
				// // 获取 会员值
				// this.$http({
				// 	url: this.$http.adornUrl('userVipDetails/selectUserVipDetailsByUserId'),
				// 	method: 'get',
				// 	params: this.$http.adornParams({
				// 		'userId': userId,
				// 	})
				// }).then(({
				// 	data
				// }) => {
				// 	if (data.code === 0) {
				// 		let returnData = data.data;
				// 		this.huiyuanzhiData = returnData
				// 	}
				// })
			},
			// 获取团队成员
			teamSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('orders/selectTeamUserList'),
					method: 'get',
					params: this.$http.adornParams({
						'userId': userId,
						'page': this.page,
						'limit': this.size,
						'type': this.classify
					})
				}).then(({
					data
				}) => {
					if (data.code === 0) {
						this.tableDataLoading = false
						let returnData = data.data;
						this.tableDatamin = returnData.list;
						this.totalnum = returnData.totalCount
					}
				})
			},
			// 获取用户消息
			userSelect1() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('message/selectMessageByUserId'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.size,
						'state': this.states,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.userData = returnData;
				})
			},
			// 获取用户订单消息
			userSelect2() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('message/selectMessageByUserId'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.size,
						'state': this.states,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.userData = returnData;
				})
			},
			// 获取消息推送
			userSelect4() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('admin/user/user_msg_push'),
					method: 'get',
					params: this.$http.adornParams({
						user_id: userId,
						page: this.page,
						num: this.size
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.userData = returnData;
				})
			},
			// 获取用户提现记录
			withdrawSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('/cash/selectPayDetails'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.size,
						'userId': userId,
					})
				}).then(({
					data
				}) => {
					if (data.code === 0) {
						this.tableDataLoading = false
						let returnData = data.data;
						this.withdrawData = returnData;
					}
				})
			},
			// 获取我的积分
			integralSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('integral/details'),
					method: 'get',
					params: this.$http.adornParams({
						'userId': userId,
						'page': this.page,
						'limit': this.size
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.integralData = returnData;
				})
			},
			// 获取钱包明细
			walletSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('Details/balanceDetailed'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.size,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.tableDataLoading = false
						let returnData = data.data;
						this.walletData = returnData;
					}
				})
			},
			walletSelectShop() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('Details/selectShopUserMoneyDetailsList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.size,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.tableDataLoading = false
						let returnData = data.data;
						this.walletData = returnData;
					}
				})
			},
			// 分销收益明细
			walletSelectS() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('Details/selectInviteMoneyDetailsUserId'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.size,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.tableDataLoading = false
						let returnData = data.data;
						this.walletData = returnData;
					}
				})
			},
			// 积分明细
			walletSelectJf() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('integral/details'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.size,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.tableDataLoading = false
						let returnData = data.data;
						this.walletData = returnData;
					}
				})
			},
			// 获取支付明细
			rechargeSelect() {
				if (this.endTime == '') {
					this.endTime = this.info.stockDate
				}
				if (this.startTime == '') {
					this.startTime = this.info2.stockDate2
				}
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('cash/selectUserRechargeByUserId'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.size,
						'endTime': this.endTime,
						'startTime': this.startTime,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.tableDataLoading = false
						let returnData = data.data
						this.rechargeData = returnData
					}
				})
				this.tableDataLoading = true
			},
			// 获取用户评价
			userDiscuss() {
				let userId = this.$route.query.userId
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('comment/selectCommentList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.size,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.tableDataLoading = false
						let returnData = data.data;
						this.DiscussData = returnData
					}
				})
			},
			// 不可提现金额支付
			rechargenone(index) {
				this.indexType = index
				this.type = ''
				this.money = ''
				this.isVip = 2
				this.dialogFormVisible1 = true
				this.tableDataLoadingje = false
			},
			StairNoticeToYe() {
				let userId = this.$route.query.userId
				if (this.type == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择修改类型',
						type: 'warning'
					})
					return
				}
				if (this.money == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入金额',
						type: 'warning'
					})
					return
				}
				if (this.indexType == 1) {
					if (Number(this.userEntity.money) <= 0) {
						if (this.type == 2) {
							this.$notify({
								title: '提示',
								duration: 1800,
								message: '当前会员卡余额已为0，不可在减少！',
								type: 'warning'
							})
							return
						}
					}
					if (this.type == 2) {
						if (Number(this.userEntity.money) < Number(this.money)) {
							this.$notify({
								title: '提示',
								duration: 1800,
								message: '减少金额不能多于：' + this.userEntity.money,
								type: 'warning'
							})
							return
						}
					}
					var url = `user/addCannotMoney/${userId}/${this.money}/${this.type}`

					var datas = {
						'isVip': this.isVip
					}
				} else {

					if (Number(this.userEntity.inviteMoney) <= 0) {
						if (this.type == 2) {
							this.$notify({
								title: '提示',
								duration: 1800,
								message: '当前收益金额已为0，不可在减少！',
								type: 'warning'
							})
							return
						}
					}
					if (this.type == 2) {
						if (Number(this.userEntity.inviteMoney) < Number(this.money)) {
							this.$notify({
								title: '提示',
								duration: 1800,
								message: '减少金额不能多于：' + this.userEntity.inviteMoney,
								type: 'warning'
							})
							return
						}
					}
					var url = `user/updateInviteMoney/${userId}/${this.money}/${this.type}`
					var datas = {}
				}
				this.tableDataLoadingje = true
				this.$http({
					url: this.$http.adornUrl(url),
					method: 'post',
					data: this.$http.adornData(datas)
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							message: '修改成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.type = ''
								this.money = ''
								this.tableDataLoadingje = false
								this.dataSelect()
							}
						})
						this.dialogFormVisible1 = false
					} else {
						this.$message.error(data.msg)
					}
				})
			},
			// 优惠券
			dataSelectYhq() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('selfCouponUser/list'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page - 1,
						'size': this.size,
						'couponName': '',
						'type': '',
						'nickName': '',
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableDataYhq = returnData
				})
			},
			// 兑换券
			dataSelectDhq() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('message/selectMessageByType'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.size,
						'state': 100,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableDataYhq = returnData
				})
			},
			// 修改信息弹框
			ChangeuserUpdate(row, index) {
				this.typeT = index
				this.type = ''
				if (index == 3) {
					this.money = row.userName
				} else if (index == 4) {
					this.money = row.phone
				} else if (index == 5) {
					this.money = row.avatar
				} else if (index == 6) {
					this.money = row.inviterCode
				} else if (index == 7) {
					this.type = 1
					this.money = ''
				} else if (index == 8) {
					this.money = row.vipNo

				} else if (index == 9) {
					if (row.birthday) {
						this.money = row.birthday
					} else {
						this.money = ''
					}

				} else if (index == 1) {
					this.money = row.zhiRate
				} else if (index == 2) {
					this.money = row.feiRate

				} else {
					this.type = 1
					this.money = ''

				}
				this.dialogFormVisibleXx = true
			},
			handleAvatarSuccess1(file) {
				this.money = file.data;
			},
			// 修改信息
			StairNoticeToXx() {
				let userId = this.$route.query.userId
				var datas = {
					userId: userId
				}
				if (this.typeT == 1) {
					var texts = '请输入佣金比例'
					datas.zhiRate = this.money
				}
				if (this.typeT == 2) {
					var texts = '请输入佣金比例'
					datas.feiRate = this.money
				}
				if (this.typeT == 3) {
					var texts = '请输入用户昵称'
					datas.userName = this.money
				}
				if (this.typeT == 4) {
					var texts = '请输入手机号'
					datas.phone = this.money
				}
				if (this.typeT == 5) {
					var texts = '请上传头像'
					datas.avatar = this.money
				}
				if (this.typeT == 6) {
					var texts = '请输入邀请人邀请码'
					datas.inviterCode = this.money
				}
				if (this.typeT == 8) {
					var texts = '请输入会员编号'
					datas.vipNo = this.money
				}
				if (this.typeT == 9) {
					var texts = '请选择日期'
					datas.birthday = this.money
				}

				if (this.money === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: texts,
						type: 'warning'
					})
					return
				}
				if (this.typeT == 7) {
					var texts = '请输入积分'
					datas.integral = this.money
					datas.type = this.type
					this.$http({
						url: this.$http.adornUrl('integral/updateUserIntegral'),
						method: 'post',
						params: this.$http.adornParams(datas)
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
							this.dialogFormVisibleXx = false
						} else {
							this.$message.error(data.msg)
						}
					})
				} else {
					this.$http({
						url: this.$http.adornUrl('user/updateUserByUserId'),
						method: 'post',
						data: this.$http.adornData(datas)
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
							this.dialogFormVisibleXx = false
						} else {
							this.$message.error(data.msg)
						}
					})
				}
			},
			// 我的订单
			dingdanSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('orders/selectOrdersList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId,
						'phone': '',
						'userName': '',
						'goodsName': '',
						'ordersNo': '',
						'status': '',
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.tableData = returnData
				})
			},
			// 兑换记录
			youhuiquanSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('admin/sdkInfo/getSdkList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'sdkRemarks': '',
						'status': '',
						'typeId': '',
						'nickName': '',
						'sdkContent': '',
						'userId': userId
					})
				}).then(({
					data
				}) => {
					console.log(data)
					this.tableDataLoading = false
					let returnData = data.data
					this.kamiData = returnData
				})
			},
			// 我的优惠卡
			youhuikaSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('myCard/selectMyCardList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'cardType': '',
						'cardName': '',
						'userId': userId,
						'userName': '',
						'phone': '',
						'orderTakingId': '',
						'status': ''
					})
				}).then(({
					data
				}) => {
					console.log(data)
					this.tableDataLoading = false
					let returnData = data.data
					this.tableData = returnData
				})
			},
			ageChange(userId, age, index) {
				if (index == 5) {
					this.age = ''
					this.type = 1
					this.titles = '次数'
				}
				this.dialogFormVisible2 = true
			},
			StairNoticeTo4() {
				let userId = this.$route.query.userId
				let datas = {}
				datas.userId = userId
				if (this.titles == '次数') {
					datas.num = this.age
					datas.type = this.type
					this.$http({
						url: this.$http.adornUrl('admin/createCountRecord/giveUserCreateCount'),
						method: 'post',
						params: this.$http.adornParams(datas)
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '修改成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dialogFormVisible2 = false
									this.dataSelect()
								}
							})
						} else {
							this.$message({
								message: data.msg,
								type: 'warning',
								duration: 1500,
								onClose: () => {}
							})
						}
					})
					return
				}
			},
			// 我的报名
			yuewanSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('activityApply/selectActivityApplyList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId,
						'status': '',
						'activityId': '',
						'activityName': '',
						'userName': '',
						'phone': '',
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					if (data.data.list.length > 0) {
						for (var i in data.data.list) {
							let payMoney = ''
							let zhiMoney = ''
							let feiMoney = ''
							if (data.data.list[i].payMoney) {
								payMoney = Number(data.data.list[i].payMoney)
							} else {
								payMoney = 0
							}
							if (data.data.list[i].zhiMoney) {
								zhiMoney = Number(data.data.list[i].zhiMoney)
							} else {
								zhiMoney = 0
							}
							if (data.data.list[i].feiMoney) {
								feiMoney = Number(data.data.list[i].feiMoney)
							} else {
								feiMoney = 0
							}
							data.data.list[i].pingMoney = (payMoney - zhiMoney - feiMoney).toFixed(2)
						}
					}
					let returnData = data.data;
					this.yuewanData = returnData;
				})
			},
			// 常用电站
			lySelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('tearoom/selectTearoomByOrdersUserId'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'tearoomType': '',
						'userId': userId
					})

				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.liwuData = returnData;
				})
			},
			// 我的车辆
			dataSelectQz() {
				let userId = this.$route.query.userId
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('car/selectCarList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableDataQz = returnData
				})
			},
			// 报名列表弹框
			shenhe(row) {
				this.activityId = row.activityId
				this.pageB = 1
				this.dialogFormVisibleBm = true
				this.baomingSelect()
			},
			// 获取报名列表
			baomingSelect() {
				this.$http({
					url: this.$http.adornUrl('activityApply/selectActivityApplyList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.pageB,
						'limit': this.limitB,
						'activityId': this.activityId,
						'activityName': '',
						'userName': '',
						'phone': '',
						'status': '',
					})
				}).then(({
					data
				}) => {
					console.log('', data)
					if (data.code == 0) {
						this.tableDataLoading = false
						if (data.data.list.length > 0) {
							for (var i in data.data.list) {
								let payMoney = ''
								let zhiMoney = ''
								let feiMoney = ''
								if (data.data.list[i].payMoney) {
									payMoney = Number(data.data.list[i].payMoney)
								} else {
									payMoney = 0
								}
								if (data.data.list[i].zhiMoney) {
									zhiMoney = Number(data.data.list[i].zhiMoney)
								} else {
									zhiMoney = 0
								}
								if (data.data.list[i].feiMoney) {
									feiMoney = Number(data.data.list[i].feiMoney)
								} else {
									feiMoney = 0
								}
								data.data.list[i].pingMoney = (payMoney - zhiMoney - feiMoney).toFixed(2)
							}
						}
						let returnData = data.data
						this.baomingData = returnData
					} else {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: data.msg,
							type: 'warning'
						});
					}
				})
			},
			// 签到列表弹框
			qiandao(row) {
				this.activityId = row.activityId
				this.pageB = 1
				this.dialogFormVisibleQd = true
				this.qiandaoSelect()
			},
			// 获取签到列表
			qiandaoSelect() {
				this.$http({
					url: this.$http.adornUrl(`message/selectActivitySignList`),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.pageB,
						'limit': this.limitB,
						'state': 110,
						'platform': '',
						'activityId': this.activityId
					})
				}).then(({
					data
				}) => {
					console.log('', data)
					if (data.code == 0) {
						this.tableDataLoading = false
						let returnData = data.data
						this.baomingData = returnData
					} else {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: data.msg,
							type: 'warning'
						});
					}
				})
			},
			// 活动相册弹框
			xiangce(row) {
				this.activityId = row.activityId
				this.pageB = 1
				this.dialogFormVisibleXc = true
				this.xiangceSelect()
			},
			xiangceSelect() {
				this.tableDataLoadingB = true
				this.$http({
					url: this.$http.adornUrl('activityPhoto/selectActivityPhotoList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.pageB,
						'limit': this.limitB,
						'userName': '',
						'activityName': '',
						'phone': '',
						'activityId': this.activityId
					})
				}).then(({
					data
				}) => {
					console.log('', data)
					this.tableDataLoadingB = false
					if (data.code == 0) {
						let returnData = data.data
						this.baomingData = returnData
					} else {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: data.msg,
							type: 'warning'
						});
					}
				})
			},
			// 视频列表弹框
			vidoClick(row) {
				this.ordersId = row.ordersId
				this.pageF = 1
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
			updatesVip(row) {
				this.userIdss = row.userId
				if (row.vipIndex) {
					this.vipIndex = row.vipIndex
				} else {
					this.vipIndex = ''
				}
				this.dataSelectVip()
				this.dialogFormVisible3 = true
			},
			dataSelectVip() {
				this.$http({
					url: this.$http.adornUrl('vip/selectVipList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': 1,
						'limit': 20,
						'vipName': '',
						'isEnable': '',
					})
				}).then(({
					data
				}) => {
					let returnData = data.data
					this.tableDataVip = returnData.records
					this.dataSelect()
					// var datas = {
					// 	vipName:'非会员用户',
					// 	vipIndex:0,
					// }
					// this.tableDataVip.push(datas)
				})
			},
			StairNoticeTo3() {
				var datas = {}
				datas.vipIndex = this.vipIndex
				datas.userId = this.userIdss
				this.$http({
					url: this.$http.adornUrl('user/updateUserByUserId'),
					method: 'post',
					data: this.$http.adornData(datas)
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						console.log('data', data)
						this.$message({
							message: '修改成功',
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
							type: 'warning',
							duration: 1500,
							onClose: () => {}
						})
					}
				})
			},
			// 店铺订单
			takeSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('admin/order/selectAllOrder'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						// 'status': this.state,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					console.log(returnData)
					this.takeData = returnData
				})
			},
			// 聊天记录
			autonymSelect1() {
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('admin/question/getChatList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId,
						'prompt': '',
						'answer': '',
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.autonymData = returnData
				})
			},
			// 信用分明细数据
			xinyongSelect() {
				let userId = this.$route.query.userId
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/creditRecord/getCreditRecordList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'riderUserName': '',
						'riderUserId': userId,
						'userId': userId,
						'riderPhone': ''
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.tableDataLoading = false
						let returnData = data.data
						this.tableDataXyf = returnData
					} else {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: data.msg,
							type: 'warning'
						});
					}
				})
			},
			// 修改信用分弹框
			creditScoreChange(row) {
				this.type = ''
				this.money = ''
				this.content = ''
				this.dialogFormVisibleXyf = true
			},
			StairNoticeToXyf() {
				let userId = this.$route.query.userId
				if (this.type == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择修改类型',
						type: 'warning'
					})
					return
				}
				var jia = this.tableData.creditScore + Number(this.money)
				var jian = this.tableData.creditScore - Number(this.money)
				if (this.money == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入分数',
						type: 'warning'
					});
					return
				}
				console.log(jia)
				if (this.type == 1) {
					if (jia > 10) {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '信用分最高为10',
							type: 'warning'
						});
						return
					}
				}
				if (this.type == 2) {
					if (jian < 0) {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '信用分最低为0',
							type: 'warning'
						});
						return
					}
				}
				if (this.content == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入理由',
						type: 'warning'
					})
					return
				}
				if (this.content.length > 100) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '修改原因字数不能超过100字',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl('admin/creditRecord/updateUserCredit'),
					method: 'post',
					params: this.$http.adornParams({
						'userId': userId,
						'type': this.type,
						'score': this.money,
						'remark': this.content
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
						this.dialogFormVisibleXyf = false
					} else {
						this.$message.error(data.msg)
					}
				})
			},
			// 发布的约球记录
			dataSelectYq() {
				let userId = this.$route.query.userId
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('dateBall/selectDateBallList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'status': '',
						'ballType': '',
						'tearoomName': '',
						'userName': '',
						'phone': '',
						'dateBallClassify': '',
						'userId': userId,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData
				})
			},
			// 约球成员 弹框
			chengyuanBtn(row) {
				this.dateBallId = row.dateBallId
				this.pageU = 1
				this.userNameU = ''
				this.phoneU = ''
				this.dataSelectYy()
				this.dialogFormVisibleYq = true
			},
			// 获取约球成员
			dataSelectYy() {
				this.tableDataLoadingPt = true
				this.$http({
					url: this.$http.adornUrl('dateBall/selectDateBallPeopleList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.pageU,
						'limit': this.limitU,
						'dateBallId': this.dateBallId,
						'userName': this.userNameU,
						'phone': this.phoneU,
						'userId': '',
					})
				}).then(({
					data
				}) => {
					let returnData = data.data
					this.tableDataUder = returnData
					this.tableDataLoadingPt = false
				})
			},
			selectYy() {
				this.pageU = 1
				this.dataSelectYy()
			},
			dataSelectYyyq() {
				let userId = this.$route.query.userId
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('dateBall/selectDateBallPeopleList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'dateBallId': '',
						'userName': '',
						'phone': '',
						'userId': userId,
					})
				}).then(({
					data
				}) => {
					let returnData = data.data
					this.tableData = returnData
					this.tableDataLoading = false
				})
			},
			// 我的留言
			pinglunjiluSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('leaveMessage/selectLeaveMessageList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userName': '',
						'phone': '',
						'content': '',
						'startTime': '',
						'endTime': '',
						'isDelete': '',
						'userId': userId,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.liuyanData = returnData;
				})
			},
			// 我的地址
			dizhiSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('address/findByUserId'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.dizhiData = returnData;
				})
			},
			// 会员值明细
			walletSelectHyz() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('userVipDetails/selectUserVipDetailsList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.size,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.tableDataLoading = false
						let returnData = data.data;
						if (this.page > 1) {
							if (data.data.records.length == 0) {
								this.page = this.page - 1
								this.walletSelectHyz()
							}
						}
						this.walletDataHyz = returnData;
					}
				})
			},
			// 添加 会员值
			confirmAdd() {
				this.type = ''
				this.money = ''
				this.dialogFormVisibleHyz = true
				this.Loading = false
			},
			StairNoticeToHyz() {
				this.Loading = true
				let userId = this.$route.query.userId
				if (this.type == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择类型',
						type: 'warning'
					})
					return this.Loading = false
				}
				if (this.money === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入会员值',
						type: 'warning'
					})
					return this.Loading = false
				}

				this.$http({
					url: this.$http.adornUrl('userVipDetails/insertUserVipDetails'),
					method: 'post',
					data: this.$http.adornData({
						'userId': userId,
						'classify': this.type,
						'money': this.money,
						'ordersNo': '系统添加'

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
								this.walletSelectHyz()
							}
						})
						this.Loading = false
						this.dialogFormVisibleHyz = false
					} else {
						this.$message.error(data.msg)
					}
				})
			},
			confirmDelete(row) {
				this.$confirm(`确定要删除数据?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.Loading = true
					this.$http({
						url: this.$http.adornUrl('userVipDetails/deleteUserVipDetails'),
						method: 'post',
						params: this.$http.adornParams({
							'userVipDetailsId': row.userVipDetailsId
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
									this.Loading = false
									this.walletSelectHyz()
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
			// 教练模块 ----------------
			handleSizeChangePlJl(val) {
				this.limit = val
				this.jianglianPlSelect()
			},
			handleCurrentChangePlJl(val) {
				this.page = val
				this.jianglianPlSelect()
			},
			handleSizeChangeXd(val) {
				this.limit = val
				this.roderSelect()
			},
			handleCurrentChangeXd(val) {
				this.page = val
				this.roderSelect()
			},
			handleSizeChangeJd(val) {
				this.limit = val
				this.jiedanSelect()
			},
			handleCurrentChangeJd(val) {
				this.page = val
				this.jiedanSelect()
			},

			handleSizeChangeXyfJl(val) {
				this.size1 = val
				this.xinyongSelectJl()
			},
			handleCurrentChangeXyfJl(val) {
				this.page1 = val
				this.xinyongSelectJl()
			},
			handleSizeChangebPl(val) {
				this.limit = val
				this.pinglunSelect()
			},
			handleCurrentChangebPl(val) {
				this.page = val
				this.pinglunSelect()
			},
			handleSizeChangeWz(val) {
				this.limit = val
				this.dataSelectWz()
			},
			handleCurrentChangeWz(val) {
				this.page = val
				this.dataSelectWz()
			},
			handleSizeChangeDg(val) {
				this.limit = val
				this.dataSelectDg()
			},
			handleCurrentChangeDg(val) {
				this.page = val
				this.dataSelectDg()
			},
			handleSizeChangeZx(val) {
				this.limit = val
				this.dataSelectZx()
			},
			handleCurrentChangeZx(val) {
				this.page = val
				this.dataSelectZx()
			},
			
			// 获取教练详情
			autonymSelect() {
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('artificer/selectArtificerById'),
					method: 'get',
					params: this.$http.adornParams({
						'artificerId': this.tablemoney.artificer.artificerId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.artificer = returnData
					if (this.artificer.certificate && this.artificer.certificate != '') {
						this.artificer.certificate = this.artificer.certificate.split(",")
					} else {
						this.artificer.certificate = []
					}
					if (this.artificer.lifePhoto && this.artificer.lifePhoto != '') {
						this.artificer.lifePhoto = this.artificer.lifePhoto.split(",")

					} else {
						this.artificer.lifePhoto = []
					}
				})
			},
			// 修改教练信息
			xiugaiJsxx(row) {

				this.artificerXg = this.artificer
				this.artificerId = this.artificerXg.artificerId
				this.userId = this.artificerXg.userId
				this.artificerName = this.artificerXg.artificerName
				this.content = this.artificerXg.content
				this.beginTime = this.artificerXg.beginTime
				this.finishTime = this.artificerXg.finishTime
				this.longitude = this.artificerXg.longitude
				this.latitude = this.artificerXg.latitude
				this.artificerImg = this.artificerXg.artificerImg
				this.certificate = this.artificerXg.certificate
				this.lifePhoto = this.artificerXg.lifePhoto
				this.classifyId = this.artificerXg.classifyId
				this.isGoods = this.artificerXg.isGoods
				this.isHot = this.artificerXg.isHot
				this.tripWay = this.artificerXg.tripWay
				this.city = this.artificerXg.city
				this.isShop = this.artificerXg.isShop
				this.isHome = this.artificerXg.isHome

				this.dialogFormVisible10 = true
			},
			handleAvatarSuccessT(file, fileList) {
				this.avatar = file.data
			},
			handleAvatarSuccess1Jl(file, fileList) {
				this.artificerImg = file.data
			},
			handleAvatarSuccess2(file, fileList) {
				this.back = file.data
			},
			//上传成功
			handleUploadSuccess3(file, fileList) {
				this.certificate.push(file.data)
				console.log('this.contentImg', this.certificate)
			},
			onprogress1(event, file, fileList) {
				console.log('详情图上传进度', parseInt(event.percent))
				this.percentage1 = parseInt(event.percent)
			},
			//上传成功
			handleUploadSuccess4(file, fileList) {
				this.lifePhoto.push(file.data)
				console.log('this.contentImg', this.lifePhoto)
			},
			// 删除
			clear1(index) {
				this.certificate.splice(index, 1);
			},
			clear2(index) {
				this.lifePhoto.splice(index, 1);
			},
			refusetoUp() {
				this.$http({
					url: this.$http.adornUrl('artificer/updateArtificers'),
					method: 'post',
					data: this.$http.adornData({
						'userId': this.userId,
						'artificerId': this.artificerId,
						'artificerName': this.artificerName,
						'content': this.content,
						// 'beginTime': this.beginTime,
						// 'finishTime': this.finishTime,
						'longitude': this.longitude,
						'latitude': this.latitude,
						'artificerImg': this.artificerImg,
						'certificate': this.certificate.toString(),
						'lifePhoto': this.lifePhoto.toString(),
						'classifyId': this.classifyId,
						'isGoods': this.isGoods,
						'isHot': this.isHot,
						'tripWay': this.tripWay,
						'city': this.city,
						'isShop': this.isShop,
						'isHome': this.isHome,

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
								this.dialogFormVisible10 = false
								this.autonymSelect()
							}
						})
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
			onprogress2(event, file, fileList) {
				console.log('event, file, fileList', parseInt(event.percent))
				this.percentage2 = parseInt(event.percent)
			},
			// 我的评价
			jianglianPlSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('takingComment/queryMyComment'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.pingluData = returnData
				})
			},
			// 收到的评价列表
			pinglunSelect() {

				this.userId = this.$store.state.user.id
				this.$http({
					url: this.$http.adornUrl('takingComment/selectOrderTakingComment'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'id': this.tablemoney.artificer.artificerId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.pinglunData = returnData
				})
			},
			// 我的下单
			roderSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('artificer/selectOrdersList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId,
						'status': ''
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.dingdanData = returnData
				})
			},
			// 我的接单
			jiedanSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('artificer/selectOrdersList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'artificerId': this.tablenum.artificer.artificerId,
						'status': ''
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.jiedanData = returnData
				})
			},
			// 我的项目
			takeSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('artificer/selectArtificerMassageList'),
					method: 'get',
					params: this.$http.adornParams({
						'artificerId': this.tablenum.artificer.artificerId,
						'status': ''
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.takeDataXm = returnData
					if (this.takeDataXm.length > 0) {
						for (var i in this.takeDataXm) {
							if (this.takeDataXm[i].contentImg) {
								this.takeDataXm[i].contentImg = this.takeDataXm[i].contentImg
									.split(',')
							}
							if (this.takeDataXm[i].workImg) {
								this.takeDataXm[i].workImg = this.takeDataXm[i].workImg.split(',')
							}
							if (this.takeDataXm[i].detailsImg) {
								this.takeDataXm[i].detailsImg = this.takeDataXm[i].detailsImg.split(',')
							}
						}
					}
				})
			},
			// 项目统计选择日期
			animeOrder() {
				console.log('info', this.info)
				this.taskData()
				// this.colonel()
			},
			// 项目统计年月日
			orderfenxi(value) {
				this.page = 1
				let vanumber = value
				this.flag = value
				this.taskData()

			},
			// 获取项目统计
			taskData() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('artificer/selectArtificerIndex'),
					method: 'get',
					params: this.$http.adornParams({
						'startTime': this.startTime,
						'endTime': this.endTime,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.guanzhuData = returnData
				})
			},
			// 教练信用分明细数据
			xinyongSelectJl() {
				let userId = this.$route.query.userId
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('message/selectMessageByUserId'),
					method: 'get',
					params: this.$http.adornParams({
						'userId': userId,
						'page': this.page1,
						'limit': this.size1,
						'state': 8
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.tableDataLoading = false
						let returnData = data.data
						this.tableDataXyf = returnData
					} else {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: data.msg,
							type: 'warning'
						});
					}
				})
			},
			// 教练位置记录
			dataSelectWz() {
				let userId = this.$route.query.userId
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('message/selectArtificerLocation'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'artificerName': '',
						'phone': '',
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableDataWz = returnData
				})
			},
			// 获取忙时
			mangshi(artificerDate) {
				this.mangshiSelect(artificerDate)
			},
			// 获取忙时数据
			mangshiSelect(artificerDate) {
				this.$http({
					url: this.$http.adornUrl('artificerTime/selectArtificerTimeByArtificerId'),
					method: 'get',
					params: this.$http.adornParams({
						'artificerId': this.tablemoney.artificer.artificerId,
						'artificerDate': artificerDate,
					})
				}).then(({
					data
				}) => {
					this.times = []
					let returnData = data.data
					if (data.data.length > 0) {
						for (var i in data.data) {
							// 	var datas = {}
							// 	datas.times = data.data[i].artificerTime
							// 	datas.classify = data.data[i].classify
							// 	this.times.push(datas)
							this.times.push(data.data[i].artificerTime)
							if (data.data[i].classify == 1) {
								for (var j in this.timeLis) {
									if (this.timeLis[j].times == data.data[i].artificerTime) {
										this.timeLis[j].classify = 1
									}
								}
							} else {
								for (var j in this.timeLis) {
									if (this.timeLis[j].times == data.data[i].artificerTime) {
										this.timeLis[j].classify = 2
									}
								}
							}
						}
					}
					this.timeList = returnData
					console.log('`````````````', this.timeLis)
				})
			},
			// 修改忙时弹框
			updateMs() {
				this.artificerDates = this.artificerDate
				this.dialogFormVisibleMs = true

			},
			// 修改忙时
			timesUp() {
				var arrs = []

				for (var i in this.times) {
					for (var j in this.timeLis) {
						if (this.timeLis[j].classify == 2) {
							console.log('this.timeLis[j].classify', this.timeLis[j].classify)
							if (this.timeLis[j].times == this.times[i]) {
								arrs.push(this.times[i])
							}
						}

					}

				}
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('artificerTime/updateArtificerTime'),
					method: 'post',
					params: this.$http.adornParams({
						'artificerId': this.tablemoney.artificer.artificerId,
						'artificerDate': this.artificerDates,
						'times': arrs.toString(),
						'userId': userId
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
								this.dialogFormVisibleMs = false
								this.autonymSelect()
								this.mangshiSelect(this.artificerDate)
							}
						})
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
			xufeiClick(row) {
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
						'page': this.pageU,
						'limit': this.sizeU,
						'content': this.ordersNo,
						'state': 400,
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
			// 修改保证金
			rechargenoneB(safetyMoney, index) {
				// if (index == 2) {
				this.typeType = index
				this.type = ''
				this.money = ''
				this.tablemoneyB = safetyMoney
				this.dialogFormVisibleBzj = true
				// }


			},
			StairNoticeTo1() {
				let userId = this.$route.query.userId
				if (this.type == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择修改类型',
						type: 'warning'
					})
					return
				}

				if (this.money === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入金额',
						type: 'warning'
					})
					return
				}
				if (this.type == 2) {
					if (this.money > this.tablemoneyB) {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '最高金额为' + this.tablemoneyB,
							type: 'warning'
						})
						return
					}
				}

				this.$http({
					url: this.$http.adornUrl('user/updateSafetyMoney'),
					method: 'post',
					params: this.$http.adornParams({
						'userId': userId,
						'type': this.type,
						'money': this.money
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
						this.dialogFormVisibleBzj = false
					} else {
						this.$message.error(data.msg)
					}

				})

			},
			timeDate2() {
				this.rechargeSelect()
			},
			timeDate() {
				this.taskData()
			},
			// 修改是否自动开台
			ageChangeKt(row) {
				if(row.isOpenGoods==1){
					var isOpenGoods = 2
				}else{
					var isOpenGoods = 1
				}
				let userId = this.$route.query.userId
				this.$confirm(`确定要更改自动开台状态?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('user/updateUserByUserId'),
						method: 'post',
						data: this.$http.adornData({
							'isOpenGoods':isOpenGoods,
							'userId':userId,
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
						} else {
							this.$message({
								message: data.msg,
								type: 'success',
								duration: 1500,
								onClose: () => {}
							})
						}
					})
				}).catch(() => {})
			},
			// 我的点歌
			dataSelectDg() {
				let userId = this.$route.query.userId
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('musicPlay/selectMusicPlayList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableDataDg = returnData
				})
			},
			// 收藏的资讯
			dataSelectZx() {
				let userId = this.$route.query.userId
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('informationCollect/selectInformationCollectByList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableDataZx = returnData
				})
			},
		},
		activated() {
			// this.dataSelectVip()
			this.dataSelect()
		}
	}
</script>

<style scoped>
.user-detail-page__back {
	display: inline-flex;
	align-items: center;
	margin-bottom: 12px;
	font-size: 14px;
	color: #606266;
	text-decoration: none;
}

.user-detail-page__back:hover {
	color: #5cc477;
}

.user-detail-page__back-icon {
	width: 1.2em;
	height: 1.2em;
	margin-right: 4px;
}

.user-detail-page__tabs {
	margin-bottom: 0;
}

.user-detail-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.user-detail-page__tabs >>> .el-tabs__item.is-active {
	color: #5cc477;
}

.user-detail-page__tabs >>> .el-tabs__active-bar {
	background-color: #5cc477;
}

.user-detail-page__tabs .nf-table-card {
	margin-bottom: 0;
}

.user-detail-page__filter-date {
	width: 160px;
}

.user-detail-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.user-detail-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

.user-detail-page__link {
	color: #5cc477;
	cursor: pointer;
}

.user-detail-page__detail-card {
	padding: 0;
	overflow: hidden;
}

.detailtabel h2 {
	margin-top: 0;
}

.detailtabel .table_main table {
	width: 100%;
}

.detailtabel .table_main {
	border: none;
}

.detailtabel table tr {
	border-bottom: 1px solid #e8eaed;
}

.detailtabel table tr th {
	background-color: #f7f8fa;
	padding: 12px 16px;
	border-right: 1px solid #e8eaed;
	border-bottom: 1px solid #e8eaed;
}

.detailtabel table tr td {
	padding: 12px 16px;
	border-right: 1px solid #e8eaed;
	border-bottom: 1px solid #e8eaed;
	background-color: #ffffff;
}

.detailtabel table th {
	color: rgba(0, 0, 0, .85);
	font-weight: 400;
	font-size: 14px;
	line-height: 1.5;
}

.detailtabel .el-button--mini {
	color: #5cc477;
	background-color: transparent;
	border: 1px solid #5cc477;
	padding: 5px 10px;
}

.detailtabel .el-button--mini:hover,
.detailtabel .el-button--mini:focus {
	color: #3fae5c;
	border-color: #3fae5c;
	background-color: #ebf8ef;
}

.border-rt {
	border-right: none !important;
}

.border-bt {
	border-bottom: none !important;
}

.table_main tr td.border-rt {
	width: 20%;
}

.nf-table-card .el-button + .el-button {
	margin-left: 0 !important;
	margin-top: 5px !important;
}
</style>