<template>
	<div class="nf-page merchant-list-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">商家管理</span>
			<h2 class="nf-page-hero__title">店铺列表</h2>
			<p class="nf-page-hero__desc">查看与管理充电站店铺信息，支持按店铺类型、充电站名称、电话、用户昵称筛选，可添加店铺与状态管理。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">
				<i class="el-icon-search nf-filter-card__head-icon"></i>
				<span>筛选条件</span>
			</div>
			<el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
				<el-form-item label="店铺类型">
					<el-select
						class="merchant-list-page__filter-select"
						v-model="shopTypeT"
						@change="selectList()">
						<el-option v-for="item in statesnum" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="充电站名称">
					<el-input
						class="merchant-list-page__filter-input"
						@keydown.enter.native="selectList"
						clearable
						placeholder="请输入充电站名称"
						v-model="storeNameT">
					</el-input>
				</el-form-item>
				<el-form-item label="充电站电话">
					<el-input
						class="merchant-list-page__filter-input"
						@keydown.enter.native="selectList"
						clearable
						placeholder="请输入充电站电话"
						v-model="tearoomPhoneT">
					</el-input>
				</el-form-item>
				<el-form-item label="用户昵称">
					<el-input
						class="merchant-list-page__filter-input"
						@keydown.enter.native="selectList"
						clearable
						placeholder="请输入用户昵称"
						v-model="userNameT">
					</el-input>
				</el-form-item>
				<!-- <el-form-item label="是否享受会员权益">
					<el-select
						class="merchant-list-page__filter-select"
						v-model="isVipPrivilegeT"
						@change="selectList()">
						<el-option v-for="item in isVipPrivilegenum" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</el-form-item> -->
				<el-form-item class="merchant-list-page__filter-actions">
					<el-button type="primary" icon="el-icon-search" @click="selectList">查询</el-button>
					<el-button type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
				</el-form-item>
			</el-form>
		</div>

		<div class="nf-table-card">
			<div class="merchant-list-page__toolbar">
				<el-button type="primary" icon="document" @click="addtea">添加</el-button>
			</div>
			<el-table v-loading="tableDataLoading" :data="tableData.list">
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
			<!-- <el-table-column prop="tearoomCoverImage" label="充电站轮播图" width="200">
				<template slot-scope="scope">
					<div v-if="scope.row.tearoomCoverImage">
						<span v-for="(item,index) in scope.row.tearoomCoverImage.split(',')">
							<el-popover placement="top-start" title="" trigger="hover">
								<img style="width: 50px; height: 50px;margin: 3px;" :src="item" alt="" slot="reference">
								<img style="width: 300px; height: 300px" :src="item" alt="">
							</el-popover>
						</span>
					</div>
				</template>
			</el-table-column> -->
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
			<!-- <el-table-column prop="wxCode" label="微信二维码">
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
			</el-table-column> -->


			<!-- <el-table-column prop="shopPoiId" label="快手门店ID" width="200">
			</el-table-column>

			<el-table-column prop="ksAppId" label="快手APPID" width="200">
			</el-table-column>
			<el-table-column prop="ksAppSecret" label="快手AppSecret" width="200">
			</el-table-column>
			<el-table-column prop="ksRefreshToken" label="快手核销优惠券token" width="300">
				<template slot-scope="scope">
					<el-popover placement="top-start" title="" trigger="hover">
						<div style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;"
							slot="reference">{{scope.row.ksRefreshToken}}</div>
						<div
							style="width: 300px;word-wrap: break-word; word-break: break-all;overflow-wrap: break-word;">
							{{scope.row.ksRefreshToken}}</div>
					</el-popover>
					<el-button size="mini" type="primary" plain :disabled="!isAuth('merchantList:update')"
						@click="ksRefreshTokenBtn(scope.row)">获取
					</el-button>
				</template>
			</el-table-column>
			<el-table-column prop="ksRefreshTokenTime" label="快手核销优惠券token 失效时间" width="160">
			</el-table-column>
			<el-table-column prop="mtEmail" label="美团Email" width="200">
			</el-table-column>
			<el-table-column prop="mtPassword" label="美团密码" width="200">
			</el-table-column>
			<el-table-column prop="mtStoreId" label="美团门店ID" width="200">
			</el-table-column>
			<el-table-column prop="dyPoiId" label="抖音门店ID" width="200">
			</el-table-column>
			<el-table-column prop="dyAppId" label="抖音AppID" width="200">
			</el-table-column>
			<el-table-column prop="dySecret" label="抖音Secret" width="200">
			</el-table-column>
			<el-table-column prop="luckySort" label="幸运用户序号" width="200">
			</el-table-column>
			<el-table-column prop="luckyCouponIds" label="幸运用户奖励优惠券" width="200">
			</el-table-column>
			<el-table-column prop="luckyContent" label="幸运用户播报内容" width="200">
			</el-table-column> -->
			<!-- <el-table-column prop="isRefund" label="是否允许退款" width="80">
				<template slot-scope="scope">
					<span v-if="scope.row.isRefund == 1">是</span>
					<span v-else>否</span>
				</template>
			</el-table-column>
			<el-table-column prop="isWxPay" label="是否开启微信支付" width="80">
				<template slot-scope="scope">
					<span v-if="scope.row.isWxPay == 1">是</span>
					<span v-else>否</span>
				</template>
			</el-table-column> -->
			
			<!-- <el-table-column prop="isOpen" label="是否线下开台" width="80">
				<template slot-scope="scope">
					<span v-if="scope.row.isOpen == 1">是</span>
					<span v-else>否</span>
				</template>
			</el-table-column>
			<el-table-column prop="isQrCode" label="扫码器是否自动开台" width="80">
				<template slot-scope="scope">
					<span v-if="scope.row.isQrCode == 1">是</span>
					<span v-else>否</span>
				</template>
			</el-table-column>
			
			<el-table-column prop="wifiNo" label="wifi账号">
			</el-table-column>
			<el-table-column prop="wifiPwd" label="wifi密码">
			</el-table-column> -->
			<!-- <el-table-column prop="hourPrice" label="计费(每小时)">
			</el-table-column>
			<el-table-column prop="cashPledge" label="押金">
			</el-table-column>
			<el-table-column prop="minutePrice" label="每分钟价格">
			</el-table-column>
			<el-table-column prop="keyCode" label="设备标识码">
			</el-table-column> -->
			<!-- <el-table-column prop="yyToken" label="语音播报token">
			</el-table-column>
			<el-table-column prop="yyId" label="语音播报ID">
			</el-table-column>
			<el-table-column prop="broadcastContent" label="播报内容">
				<template slot-scope="scope">
					<el-popover placement="top-start" title="" trigger="hover">
						<div style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;"
							slot="reference">{{scope.row.broadcastContent}}</div>
						<div style="width: 300px;">{{scope.row.broadcastContent}}</div>
					</el-popover>
				</template>
			</el-table-column>

			<el-table-column prop="cameraId" label="摄像头配置">
			</el-table-column> -->
			<el-table-column prop="startTime" label="营业时间" width="100">
				<template slot-scope="scope">
					<div>{{scope.row.startTime}}--{{scope.row.endTime}}</div>
				</template>
			</el-table-column>
			<!-- <el-table-column prop="tearoomLabel" label="充电站标签">
			</el-table-column> -->
			<el-table-column prop="sales" label="销量">
				<template slot-scope="scope">
					<span>{{scope.row.sales ? scope.row.sales : '0'}}</span>
				</template>
			</el-table-column>
			<!-- <el-table-column prop="isFaceRecognition" label="是否开启人脸识别" width="80">
				<template slot-scope="scope">
					<span v-if="scope.row.isFaceRecognition == 1">是</span>
					<span v-else>否</span>
				</template>
			</el-table-column>
			<el-table-column prop="isMusic" label="门店是否支持点歌" width="80">
				<template slot-scope="scope">
					<span v-if="scope.row.isMusic == 1">是</span>
					<span v-else>否</span>
				</template>
			</el-table-column> -->

			<!-- <el-table-column prop="score" label="充电站评分">
			</el-table-column> -->
			<!-- <el-table-column prop="isReserve" label="是否开启预定" width="80">
				<template slot-scope="scope">
					<span v-if="scope.row.isReserve == 1">是</span>
					<span v-else>否</span>
				</template>
			</el-table-column>
			<el-table-column prop="isWelfare" label="是否开启爱心活动" width="80">
				<template slot-scope="scope">
					<span v-if="scope.row.isWelfare == 1">是</span>
					<span v-else>否</span>
				</template>
			</el-table-column>
			<el-table-column prop="welfareContent" label="爱心活动介绍" width="300">
				<template slot-scope="scope">
					<el-popover placement="top-start" title="" trigger="hover">
						<div style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;"
							slot="reference">{{scope.row.welfareContent}}</div>
						<div style="width: 300px;">{{scope.row.welfareContent}}</div>
					</el-popover>
				</template>
			</el-table-column>
			<el-table-column prop="welfareImg" label="爱心活动背景图" width="200">
				<template slot-scope="scope">
					<div v-if="scope.row.welfareImg">
						<span v-for="(item,index) in scope.row.welfareImg.split(',')">
							<el-popover placement="top-start" title="" trigger="hover">
								<img style="width: 50px; height: 50px;margin: 3px;" :src="item" alt="" slot="reference">
								<img style="width: 300px; height: 300px" :src="item" alt="">
							</el-popover>
						</span>
					</div>
				</template>
			</el-table-column> -->
			<!-- <el-table-column prop="moneyScope" fixed="right" label="零钱操作权限" width="80">
				<template slot-scope="scope">
					<span v-if="scope.row.moneyScope == 1">有加减</span>
					<span v-if="scope.row.moneyScope == 2">有加</span>
					<span v-if="scope.row.moneyScope == 3">有减</span>
					<span v-if="scope.row.moneyScope == 4">没有</span>
				</template>
			</el-table-column>
			<el-table-column prop="isVipPrivilege" label="是否享受会员权益" width="90">
				<template slot-scope="scope">
					<span style="color: #F56C6C;" v-if="scope.row.isVipPrivilege == 1">是</span>
					<span style="color: #5cc477;" v-if="scope.row.isVipPrivilege == 2">否</span>
				</template>
			</el-table-column> -->
			<el-table-column prop="status" fixed="right" label="状态" width="80">
				<template slot-scope="scope">
					<span style="color: #13ce66;" v-if="scope.row.status == 1">正常</span>
					<span style="color: #F56C6C;" v-if="scope.row.status == 2">打烊</span>
					<span style="color: #5cc477;" v-if="scope.row.status == 3">今日已满</span>
					<!-- <el-switch v-model="scope.row.ban_status" @change="change(scope.row.ban_status,scope.row)"
						:active-value="openValue" :inactive-value="closeValue" active-color="#13ce66"
						inactive-color="#ff4949">
					</el-switch> -->
				</template>
			</el-table-column>
			<!-- <el-table-column prop="margin_type" label="保证金类型" width="90">
        <template slot-scope="scope">
          <span style="color: #F56C6C;" v-if="scope.row.margin_type == 1">永久</span>
          <span style="color: #5cc477;" v-if="scope.row.margin_type == 2">年费</span>
          <span style="color: #5cc477;" v-if="scope.row.margin_type == null">未知</span>
        </template>
      </el-table-column>
      <el-table-column prop="margin_status" label="保证金状态" width="90">
        <template slot-scope="scope">
          <span style="color: #F56C6C;" v-if="scope.row.margin_status == 1">未缴纳</span>
          <span style="color: #5cc477;" v-if="scope.row.margin_status == 2">已缴纳</span>
        </template>
      </el-table-column>
      <el-table-column prop="margin_status" label="商品手续费" width="90">
        <template slot-scope="scope">
          <span>{{scope.row.margin_charge?scope.row.margin_charge:'暂无'}}</span>
        </template>
      </el-table-column>
      <el-table-column prop="margin_money" label="已交保证金" width="90">
        <template slot-scope="scope">
          <span>{{scope.row.margin_money?scope.row.margin_money:'暂无'}}</span>
        </template>
      </el-table-column>
      <el-table-column prop="min_margin_money" label="最低保证金" width="90">
        <template slot-scope="scope">
          <span>{{scope.row.min_margin_money?scope.row.min_margin_money:'暂无'}}</span>
        </template>
      </el-table-column> -->
			<el-table-column prop="createTime" label="创建时间" width="160">
			</el-table-column>
			<el-table-column label="操作" width="200" fixed="right">
				<template slot-scope="scope">
					<el-button size="mini" type="primary" @click="merchDetails(scope.row)">详情
					</el-button>
					<!-- <el-button size="mini" type="warning" @click="merchLook(scope.row)">设备管理
					</el-button> -->
					<el-button size="mini" type="primary" plain :disabled="!isAuth('merchantList:update')"
						v-if="scope.row.status == 1" @click="deblocking(scope.row,1)">打烊
					</el-button>
					<el-button size="mini" type="danger" v-if="scope.row.status ==1"
						:disabled="!isAuth('merchantList:update')" @click="deblocking(scope.row,2)">今日已满
					</el-button>
					<el-button size="mini" type="danger" :disabled="!isAuth('merchantList:update')"
						v-if="scope.row.status == 3||scope.row.status == 2" @click="deblocking(scope.row,3)">正常营业
					</el-button>

				</template>
			</el-table-column>
		</el-table>
			<div class="merchant-list-page__pagination">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
					layout="total,sizes, prev, pager, next" :total="tableData.totalCount">
				</el-pagination>
			</div>
		</div>

		<!-- 添加弹框 -->
		<el-dialog title="添加充电站" :visible.sync="dialogFormVisible1" center width="80%">
			<el-form>
				<div class="shop_item">
					<div class="shop_content" style="max-width: 800px;margin: auto;">
						<div class="content_item" style="margin-bottom: 10px;">
							<div style="display: flex;align-items: center;margin: 2% 0;">
								<span style="display: inline-block;text-align: left;width: 120px;">充电站logo：</span>
								<div
									style="width:148px;height:148px;background-color: #fbfdff; border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
									<el-upload class="avatar-uploader" v-model="tearoomTitleImage"
										:action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
										:on-success="handleAvatarSuccess3">
										<img v-if="tearoomTitleImage" :src="tearoomTitleImage" class="avatar"
											style="width: 148px;height: 148px;border-radius:6px;" />
										<i v-else class="el-icon-plus avatar-uploader-icon"
											style="font-size: 28px;color: #8c939d"></i>
									</el-upload>
								</div>
							</div>
						</div>
						<!-- <div class="shop_content" style="max-width: 800px;margin: auto;">
							<div class="content_item" style="margin-bottom: 10px;">
								<div style="display: flex;align-items: center;margin: 2% 0;">
									<span style="display: inline-block;text-align: left;width: 120px;">群二维码：</span>
									<div
										style="width:148px;height:148px;background-color: #fbfdff; border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
										<el-upload class="avatar-uploader" v-model="qunImg"
											:action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
											:on-success="handleAvatarSuccess4">
											<img v-if="qunImg" :src="qunImg" class="avatar"
												style="width: 148px;height: 148px;border-radius:6px;" />
											<i v-else class="el-icon-plus avatar-uploader-icon"
												style="font-size: 28px;color: #8c939d"></i>
										</el-upload>
									</div>
								</div>
							</div>
						</div>
						<div class="shop_content" style="max-width: 800px;margin: auto;">
							<div class="content_item" style="margin-bottom: 10px;">
								<div style="display: flex;align-items: center;margin: 2% 0;">
									<span style="display: inline-block;text-align: left;width: 120px;">微信二维码：</span>
									<div
										style="width:148px;height:148px;background-color: #fbfdff; border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
										<el-upload class="avatar-uploader" v-model="wxCode"
											:action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
											:on-success="handleAvatarSuccess5">
											<img v-if="wxCode" :src="wxCode" class="avatar"
												style="width: 148px;height: 148px;border-radius:6px;" />
											<i v-else class="el-icon-plus avatar-uploader-icon"
												style="font-size: 28px;color: #8c939d"></i>
										</el-upload>
									</div>
								</div>
							</div>
						</div> -->

						<!-- <div class="content_item" style="margin-bottom: 10px;">
							<div style="display: flex;align-items: center;margin: 2% 0;">
								<span style="display: inline-block;text-align: left;width: 120px;">充电站轮播图：</span>
								<div style="display: flex;">
									<div v-for="(item,index) of tearoomCoverImage" :key="index"
										style="display: inline-block;">
										<div style="position: relative;margin: 5px;">
											<img :src="item" class="avatar"
												style="width:148px;height: 148px;border-radius: 6px;" />
											<div @click="clear(index)" class="divhove" v-if="tearoomCoverImage">
												<i class="el-icon-delete"
													style="margin: 0;font-size: 18px;color: #fff;"></i>
											</div>
										</div>
									</div>
									<el-upload class="avatar-uploader"
										style="width:148px;height:148px;background-color: #fbfdff; border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;"
										:action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
										:on-success="handleUploadSuccess" :on-change="handleChange">
										<i class="el-icon-plus"></i>
									</el-upload>
								</div>
							</div>
						</div> -->
						<div class="content_item" style="margin-bottom: 10px;">
							<span style="width: 120px;display: inline-block;text-align: left;">绑定用户：</span>
							<el-input v-model="nickName1" @focus="userselect()" style="width:50%;" class="margin15"
								placeholder="请选择绑定用户">
							</el-input>
						</div>
						<div class="content_item" style="position: relative;margin: 10px 0;">
							<span style="display: inline-block;text-align: left;width: 120px;">店铺类型:</span>
							<div class="margin15" style="position: absolute;top: 0;">
								<el-radio-group v-model="shopType" style="margin-left: 123px;">
									<el-radio :label="1">自营</el-radio>
									<el-radio :label="2">三方</el-radio>
								</el-radio-group>
							</div>
						</div>
						<div class="content_item" style="position: relative;margin: 10px 0;">
							<span style="display: inline-block;text-align: left;width: 120px;">充电类型:</span>
							<div class="margin15" style="position: absolute;top: 0;">
								<el-radio-group v-model="tearoomType" style="margin-left: 123px;">
									<el-radio :label="1">电瓶车</el-radio>
									<el-radio :label="2">汽车桩</el-radio>
								</el-radio-group>
							</div>
						</div>
						
						<div class="content_item" style="margin-bottom: 10px;">
							<span style="width: 120px;text-align: left;display:inline-block;">充电站名称:</span>
							<el-input style="width:50%;" class="margin15" placeholder="请输入充电站名称" v-model="tearoomName"
								autosize></el-input>
						</div>
						<div class="content_item" style="margin-bottom: 10px;">
							<span style="width: 120px;text-align: left;display:inline-block;">充电站电话:</span>
							<el-input style="width:50%;" class="margin15" placeholder="请输入充电站电话" v-model="storephone"
								autosize></el-input>
						</div>

						<div class="content_item" style="margin-bottom: 10px;">
							<span style="width: 120px;text-align: left;display:inline-block;">佣金比例:</span>
							<el-input style="width:50%;" class="margin15" placeholder="请输入佣金比例" v-model="rate"
								autosize></el-input>
						</div>
						<!-- <div class="content_item" style="margin-bottom: 10px;">
							<span style="display: inline-block;text-align: left;width: 120px;">语音播报token:</span>
							<el-input style="width:50%;" class="margin15" placeholder="请输入语音播报token" v-model="yyToken"
								autosize></el-input>
						</div>
						<div class="content_item" style="margin-bottom: 10px;">
							<span style="display: inline-block;text-align: left;width: 120px;">语音播报ID:</span>
							<el-input style="width:50%;" class="margin15" placeholder="请输入语音播报ID" v-model="yyId"
								autosize></el-input>
						</div>

						<div class="content_item" style="margin-bottom: 10px;">
							<span style="display: inline-block;text-align: left;width: 120px;">播报内容:</span>
							<el-input style="width:50%;" class="margin15" placeholder="请输入播报内容"
								v-model="broadcastContent" autosize></el-input>
						</div>
						<div class="content_item" style="margin-bottom: 10px;">
							<span style="display: inline-block;text-align: left;width: 120px;">摄像头配置:</span>
							<el-input style="width:50%;" class="margin15" placeholder="请输入摄像头配置" v-model="cameraId"
								autosize></el-input>
						</div> -->

						<!-- <div class="content_item" style="margin-bottom: 10px;">
							<span style="display: inline-block;text-align: left;width: 120px;">平台收费手续费:</span>
							<el-input style="width:50%;" class="margin15" placeholder="请输入平台收费手续费"
								v-model="margin_charge" autosize></el-input>
						</div> -->

						<!-- <div class="content_item" style="margin-bottom: 10px;">
							<span style="display: inline-block;text-align: left;width: 120px;">设备标识码:</span>
							<el-input style="width:50%;" class="margin15" placeholder="请输入设备标识码"
								v-model="keyCode" autosize></el-input>
						</div>
						
						<div class="content_item" style="margin-bottom: 10px;">
							<span style="display: inline-block;text-align: left;width: 120px;">押金:</span>
							<el-input style="width:50%;" class="margin15" placeholder="请输入押金"
								v-model="cashPledge" autosize></el-input>
						</div>
						<div class="content_item" style="margin-bottom: 10px;">
							<span style="display: inline-block;text-align: left;width: 120px;">每小时价格:</span>
							<el-input style="width:50%;" class="margin15" placeholder="请输入每小时价格"
								v-model="hourPrice" autosize></el-input>
						</div>
						<div class="content_item" style="margin-bottom: 10px;">
							<span style="display: inline-block;text-align: left;width: 120px;">每分钟价格:</span>
							<el-input style="width:50%;" class="margin15" placeholder="请输入每分钟价格"
								v-model="minutePrice" autosize></el-input>
						</div>
						<div class="content_item" style="margin-bottom: 10px;">
							<span style="display: inline-block;text-align: left;width: 120px;">预约价格:</span>
							<el-input style="width:50%;" class="margin15" placeholder="请输入预约价格"
								v-model="subscription" autosize></el-input>
						</div> -->

						<!-- <div class="content_item" style="margin-bottom: 10px;">
							<span style="display: inline-block;text-align: left;width: 120px;">快手门店ID:</span>
							<el-input style="width:50%;" class="margin15" placeholder="请输入快手门店ID" v-model="shopPoiId"
								autosize></el-input>
						</div>

						<div class="content_item" style="margin-bottom: 10px;">
							<span style="display: inline-block;text-align: left;width: 120px;">快手APPID:</span>
							<el-input style="width:50%;" class="margin15" placeholder="请输入快手APPID" v-model="ksAppId"
								autosize></el-input>
						</div>
						<div class="content_item" style="margin-bottom: 10px;">
							<span style="display: inline-block;text-align: left;width: 120px;">快手AppSecret:</span>
							<el-input style="width:50%;" class="margin15" placeholder="请输入快手AppSecret"
								v-model="ksAppSecret" autosize></el-input>
						</div>
						<div class="content_item" style="margin-bottom: 10px;">
							<span style="display: inline-block;text-align: left;width: 120px;">美团Email:</span>
							<el-input style="width:50%;" class="margin15" placeholder="请输入美团Email" v-model="mtEmail"
								autosize></el-input>
						</div>
						<div class="content_item" style="margin-bottom: 10px;">
							<span style="display: inline-block;text-align: left;width: 120px;">美团密码:</span>
							<el-input style="width:50%;" class="margin15" placeholder="请输入美团密码" v-model="mtPassword"
								autosize></el-input>
						</div>
						<div class="content_item" style="margin-bottom: 10px;">
							<span style="display: inline-block;text-align: left;width: 120px;">美团门店ID:</span>
							<el-input style="width:50%;" class="margin15" placeholder="请输入美团门店ID" v-model="mtStoreId"
								autosize></el-input>
						</div>
						<div class="content_item" style="margin-bottom: 10px;">
							<span style="display: inline-block;text-align: left;width: 120px;">抖音门店ID:</span>
							<el-input style="width:50%;" class="margin15" placeholder="请输入抖音门店ID" v-model="dyPoiId"
								autosize></el-input>
						</div>
						<div class="content_item" style="margin-bottom: 10px;">
							<span style="display: inline-block;text-align: left;width: 120px;">抖音AppID:</span>
							<el-input style="width:50%;" class="margin15" placeholder="请输入抖音AppID" v-model="dyAppId"
								autosize></el-input>
						</div>
						<div class="content_item" style="margin-bottom: 10px;">
							<span style="display: inline-block;text-align: left;width: 120px;">抖音Secret:</span>
							<el-input style="width:50%;" class="margin15" placeholder="请输入抖音Secret" v-model="dySecret"
								autosize></el-input>
						</div>

						<div class="content_item" style="margin-bottom: 10px;">
							<span style="display: inline-block;text-align: left;width: 120px;">幸运用户序号:</span>
							<el-input style="width:50%;" class="margin15" placeholder="请输入幸运用户序号" v-model="luckySort"
								autosize></el-input>
						</div> -->
						<!-- <div class="content_item" style="margin-bottom: 10px;">
							<span style="display: inline-block;text-align: left;width: 120px;">幸运用户奖励优惠券:</span>
							<el-input style="width:50%;" class="margin15" placeholder="请输入幸运用户奖励优惠券"
								v-model="luckyCouponIds" autosize></el-input>
						</div> -->
						<!-- <div class="content_item" style="margin-bottom: 10px;">
							<span style="display: inline-block;text-align: left;width: 120px;">幸运用户播报内容:</span>
							<el-input style="width:50%;" class="margin15" placeholder="请输入幸运用户播报内容"
								v-model="luckyContent" autosize></el-input>
						</div> -->
						<!-- <div class="content_item" style="margin-bottom: 10px;">
							<span style="display: inline-block;text-align: left;width: 120px;">是否允许操作退款:</span>
							<el-radio-group v-model="isRefund" style="margin-left: 123px;">
								<el-radio :label="1">是</el-radio>
								<el-radio :label="2">否</el-radio>
							</el-radio-group>
						</div>
						<div class="content_item" style="margin-bottom: 10px;">
							<span style="display: inline-block;text-align: left;width: 120px;">是否开启微信支付:</span>
							<el-radio-group v-model="isWxPay" style="margin-left: 123px;">
								<el-radio :label="1">是</el-radio>
								<el-radio :label="2">否</el-radio>
							</el-radio-group>
						</div> -->
						



						<!-- <div class="content_item" style="margin-bottom: 10px;">
							<span style="display: inline-block;text-align: left;width: 120px;">wifi账号:</span>
							<el-input style="width:50%;" class="margin15" placeholder="请输入wifi账号" v-model="wifiNo"
								autosize></el-input>
						</div>
						<div class="content_item" style="margin-bottom: 10px;">
							<span style="display: inline-block;text-align: left;width: 120px;">wifi密码:</span>
							<el-input style="width:50%;" class="margin15" placeholder="请输入wifi密码" v-model="wifiPwd"
								autosize></el-input>
						</div> -->
						<div class="content_item" style="position: relative;margin: 10px 0;">
							<span style="display: inline-block;text-align: left;width: 120px;">店铺状态:</span>
							<div class="margin15" style="position: absolute;top: 0;">
								<el-radio-group v-model="banstatus" style="margin-left: 123px;">
									<el-radio :label="1">正常</el-radio>
									<el-radio :label="2">打烊</el-radio>
									<el-radio :label="3">今日已满</el-radio>
								</el-radio-group>
							</div>
						</div>
						<!-- <div class="content_item" style="position: relative;margin: 10px 0;">
							<span style="display: inline-block;text-align: left;width: 120px;">零钱操作权限:</span>
							<div class="margin15" style="position: absolute;top: 0;">
								<el-radio-group v-model="moneyScope" style="margin-left: 123px;">
									<el-radio :label="1">有加减</el-radio>
									<el-radio :label="2">有加</el-radio>
									<el-radio :label="3">有减</el-radio>
									<el-radio :label="4">没有</el-radio>
								</el-radio-group>
							</div>
						</div>
						<div class="content_item" style="position: relative;margin: 10px 0;">
							<span style="display: inline-block;text-align: left;width: 120px;">是否享受会员权益:</span>
							<div class="margin15" style="position: absolute;top: 0;">
								<el-radio-group v-model="isVipPrivilege" style="margin-left: 123px;">
									<el-radio :label="1">是</el-radio>
									<el-radio :label="2">否</el-radio>
								</el-radio-group>
							</div>
						</div>
						<div class="content_item" style="position: relative;margin: 10px 0;">
							<span style="display: inline-block;text-align: left;width: 120px;">是否开启预定:</span>
							<div class="margin15" style="position: absolute;top: 0;">
								<el-radio-group v-model="isReserve" style="margin-left: 123px;">
									<el-radio :label="1">是</el-radio>
									<el-radio :label="2">否</el-radio>
								</el-radio-group>
							</div>
						</div> -->
						<!-- <div class="content_item" style="position: relative;margin: 10px 0;">
							<span style="display: inline-block;text-align: left;width: 120px;">是否开启人脸识别:</span>
							<div class="margin15" style="position: absolute;top: 0;">
								<el-radio-group v-model="isFaceRecognition" style="margin-left: 123px;">
									<el-radio :label="1">是</el-radio>
									<el-radio :label="2">否</el-radio>
								</el-radio-group>
							</div>
						</div>
						<div class="content_item" style="position: relative;margin: 10px 0;">
							<span style="display: inline-block;text-align: left;width: 120px;">门店是否支持点歌:</span>
							<div class="margin15" style="position: absolute;top: 0;">
								<el-radio-group v-model="isMusic" style="margin-left: 123px;">
									<el-radio :label="1">是</el-radio>
									<el-radio :label="2">否</el-radio>
								</el-radio-group>
							</div>
						</div> -->

						<!-- <div class="content_item" style="position: relative;margin: 10px 0;">
							<span style="display: inline-block;text-align: left;width: 120px;">是否线下开台:</span>
							<div class="margin15" style="position: absolute;top: 0;">
								<el-radio-group v-model="isOpen" style="margin-left: 123px;">
									<el-radio :label="1">是</el-radio>
									<el-radio :label="2">否</el-radio>
								</el-radio-group>
							</div>
						</div> -->
						<!-- <div class="content_item" style="position: relative;margin: 10px 0;">
							<span style="display: inline-block;text-align: left;width: 120px;">扫码器是否自动开台:</span>
							<div class="margin15" style="position: absolute;top: 0;">
								<el-radio-group v-model="isQrCode" style="margin-left: 123px;">
									<el-radio :label="1">是</el-radio>
									<el-radio :label="2">否</el-radio>
								</el-radio-group>
							</div>
						</div>
						
						<div class="content_item" style="position: relative;margin: 10px 0;">
							<span style="display: inline-block;text-align: left;width: 120px;">是否开启爱心活动:</span>
							<div class="margin15" style="position: absolute;top: 0;">
								<el-radio-group v-model="isWelfare" style="margin-left: 123px;">
									<el-radio :label="1">是</el-radio>
									<el-radio :label="2">否</el-radio>
								</el-radio-group>
							</div>
						</div>
						<div class="content_item" style="margin-bottom: 10px;">
							<span style="display: inline-block;text-align: left;width: 120px;">爱心活动介绍:</span>
							<el-input style="width:50%;" class="margin15" placeholder="请输入爱心活动介绍"
								v-model="welfareContent" autosize></el-input>
						</div>
						<div class="shop_content" style="max-width: 800px;margin: auto;">
							<div class="content_item" style="margin-bottom: 10px;">
								<div style="display: flex;align-items: center;margin: 2% 0;">
									<span style="display: inline-block;text-align: left;width: 120px;">爱心活动背景图：</span>
									<div
										style="width:148px;height:148px;background-color: #fbfdff; border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
										<el-upload class="avatar-uploader" v-model="welfareImg"
											:action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
											:on-success="handleAvatarSuccess6">
											<img v-if="welfareImg" :src="welfareImg" class="avatar"
												style="width: 148px;height: 148px;border-radius:6px;" />
											<i v-else class="el-icon-plus avatar-uploader-icon"
												style="font-size: 28px;color: #8c939d"></i>
										</el-upload>
									</div>
								</div>
							</div>
						</div> -->
						<!-- <div class="content_item" style="margin-bottom: 10px;">
							<span style="display: inline-block;text-align: left;width: 120px;">充电站标签:</span>
							<div style="display: inline-block;width: auto;">
								<span v-if="tearoomLabel" style="display: inline-block;width: auto;">
									<el-tag v-for="(item,index) of tearoomLabel" :key="index" type="info" closable
										@close="bqClose(index)">{{item}}</el-tag>
								</span>
								<el-input style="width:133px;" class="margin15" placeholder="请输入标签" v-model="bq"
									autosize @keydown.enter.native="addBq"></el-input>
								<el-button type="primary" plain @click="addBq()">确定</el-button>
							</div>
						</div> -->
						<div class="content_item" style="margin-bottom: 10px;">
							<span span style="display: inline-block;text-align: left;width: 120px;">城市：</span>
							<el-cascader style="width:50%" size="large" :options="options" ref="cascaderAddr"
								v-model="storeAddress" :placeholder="storeAddre" @change="handleChange55"
								:value="storeAddre">
							</el-cascader>
						</div>
						<div class="content_item" style="margin-bottom: 10px;">
							<span style="display: inline-block;text-align: left;width: 120px;">充电站地址:</span>
							<el-input style="width:50%;" class="margin15" placeholder="请输入充电站地址"
								v-model="tearoomAddress" autosize @keydown.enter.native="selectDz"></el-input>
							<el-button type="info" plain @click="selectDz()">获取经纬度</el-button>
						</div>
						<div class="content_item" style="margin-bottom: 10px;">
							<span style="display: inline-block;text-align: left;width: 120px;">充电站经度:</span>
							<el-input style="width:50%;" class="margin15" disabled placeholder="请输入充电站经度"
								v-model="tearoomLatitude" autosize></el-input>
						</div>
						<div class="content_item" style="margin-bottom: 10px;">
							<span style="display: inline-block;text-align: left;width: 120px;">充电站纬度:</span>
							<el-input style="width:50%;" class="margin15" disabled placeholder="请输入充电站纬度"
								v-model="tearoomLongitude" autosize></el-input>

						</div>
						<div class="content_item" style="margin-bottom: 10px;">
							<span style="display: inline-block;text-align: left;width: 120px;">开店时间：</span>
							<!-- <el-input v-model="openTime" placeholder="请输入排序号" type="data" min="0" style="width:50%;"></el-input> -->
							<el-time-select v-model="startTime" :picker-options="{
						  start: '00:00',
						  step: '00:15',
						  end: '24:00'
						}" placeholder="选择开店时间" style="width:50%;">
							</el-time-select>
						</div>
						<div class="content_item" style="margin-bottom: 10px;">
							<span style="display: inline-block;text-align: left;width: 120px;">闭店时间：</span>
							<!-- <el-input v-model="closeTime" placeholder="请输入排序号" type="data" min="0" style="width:50%;"></el-input> -->
							<el-time-select v-model="endTime" :picker-options="{
						  start: '00:00',
						  step: '00:15',
						  end: '24:00'
						}" placeholder="选择闭店时间" style="width:50%;">
							</el-time-select>
						</div>
						<!-- <div class="content_item" style="margin-bottom: 10px;">
							<span style="display: inline-block;text-align: left;width: 120px;">营业时间:</span>
							<el-time-picker is-range v-model="store_time" range-separator="至" start-placeholder="开始时间"
								value-format="HH:mm" end-placeholder="结束时间" placeholder="选择时间范围">
							</el-time-picker>
						</div> -->
						<div>
							<div id="container1" style="width:80%;height:500px;margin-left: 10%;"></div>
						</div>
					</div>
				</div>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible1 = false">取 消</el-button>
				<el-button type="primary" @click="addteaclassroom()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 用户列表 -->
		<el-dialog title="用户列表" :visible.sync="dialogFormVisible3" center>
			<div style="margin:2% 0;display: inline-block;">
				<span>手机号：</span>
				<el-input style="width: 150px;" @keydown.enter.native="userclick" clearable placeholder="请输入手机号"
					v-model="phone1"></el-input>&nbsp;&nbsp;&nbsp;&nbsp;
				<span>昵称：</span>
				<el-input style="width: 150px;" @keydown.enter.native="userclick" clearable placeholder="请输入昵称"
					v-model="nickName2"></el-input>
				<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="userclick">
					查询
				</el-button>
				<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="userclose">
					重置
				</el-button>
			</div>
			<el-table width="780px" v-loading="tableDataLoading4" :data="userIds.list">
				<el-table-column prop="userId" fixed label="编号" width="80">
				</el-table-column>
				<el-table-column prop="userName" label="昵称">
					<template slot-scope="scope">
						<span style="color: #f56c6c;">{{scope.row.userName ? scope.row.userName : '未设置'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="phone" label="手机号">
					<template slot-scope="scope">
						<span>{{scope.row.phone ? scope.row.phone : '未绑定'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间">
				</el-table-column>
				<el-table-column label="操作" fixed='right'>
					<template slot-scope="scope">
						<el-button size="mini" type="primary" @click="confirm(scope.row)">确定
						</el-button>

					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange2" @current-change="handleCurrentChange2"
					:page-sizes="[5, 10, 15, 20]" :page-size="size1" :current-page="page1"
					layout="total,sizes, prev, pager, next,jumper" :total="userIds.totalCount">
				</el-pagination>
			</div>
		</el-dialog>
		<!-- 设备列表-->
		<el-dialog title="设备列表" :visible.sync="dialogFormVisible" center width="80%">
			<div style="float: right;margin-right:2%;">
				<el-button style='margin: 10px 0;' :disabled="!isAuth('merchantList:add')" size="mini" type="primary"
					icon="document" @click="addNotice">添加</el-button>
			</div>
			<el-table v-loading="tableDataLoadingKt" :data="tableDataKt.list">

				<el-table-column fixed prop="roomId" label="编号" width="50">
				</el-table-column>
				<!-- <el-table-column prop="roomType" label="设备类型">
					<template slot-scope="scope">
						<span v-if="scope.row.roomType==1">包间门</span>
						<span v-if="scope.row.roomType==2">充电站大门</span>
						<span v-if="scope.row.roomType==3">其他</span>
					</template>
				</el-table-column> -->
				<el-table-column prop="roomName" label="设备名称">
				</el-table-column>
				<el-table-column prop="roomNo" label="设备编号">
				</el-table-column>
				<el-table-column prop="isOpen" label="是否开启">
					<template slot-scope="scope">
						<el-switch v-model="scope.row.isOpen" @change="changeKt(scope.row)" :active-value="openValue"
							:disabled="!isAuth('merchantList:update')" :inactive-value="closeValue"
							active-color="#13ce66" inactive-color="#ff4949">
						</el-switch>
					</template>
				</el-table-column>
				<el-table-column label="操作" width="180">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('merchantList:update')"
							@click="amendBanner(scope.row)">修改
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('merchantList:delete')"
							@click="deletes(scope.row)">删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChangeKt" @current-change="handleCurrentChangeKt"
					:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableDataKt.totalCount">
				</el-pagination>
			</div>
		</el-dialog>
		<el-dialog :title="title" :visible.sync="dialogFormVisibleKt2" center>
			<!-- <div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">设备类型：</span>
				<el-radio-group v-model="roomType">
					<el-radio :label="1">包间门</el-radio>
					<el-radio :label="2">充电站大门</el-radio>
					<el-radio :label="3">其他</el-radio>
				</el-radio-group>
			</div> -->
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">设备名称：</span>
				<el-input style="width:50%;" v-model="roomName" placeholder="请输入设备名称"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">设备编号：</span>
				<el-input style="width:50%;" v-model="roomNo" placeholder="请输入设备编号"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">是否开启：</span>
				<el-radio-group v-model="isOpen">
					<el-radio :label="1">是</el-radio>
					<el-radio :label="2">否</el-radio>
				</el-radio-group>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisibleKt2 = false">取 消</el-button>
				<el-button type="primary" @click="addNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 获取快手核销优惠券token 弹框 -->
		<el-dialog title="获取快手核销优惠券token" :visible.sync="dialogFormVisibleKs" center>

			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">CODE：</span>
				<el-input style="width:50%;" v-model="code" placeholder="请输入CODE"></el-input>
			</div>

			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisibleKs = false">取 消</el-button>
				<el-button type="primary" @click="ksNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	import {
		jsonp
	} from 'vue-jsonp'
	import {
		provinceAndCityData,
		regionData,
		provinceAndCityDataPlus,
		regionDataPlus,
		CodeToText,
		TextToCode
	} from 'element-china-area-data'
	var geocoder, map, markersArray = [];
	export default {
		data() {
			return {
				tearoomAddress: '',
				tearoomLatitude: '',
				tearoomLongitude: '',
				tearoomProvince: '',
				options: regionData,
				tearoomCity: '',
				tearoomDistrict: '',
				storeAddress: [],
				storeAddre: '请选择城市',
				storephone: '',
				tearoomTitleImage: '',
				tearoomCoverImage: [],
				storeNameT: '',
				tearoomPhoneT: '',
				shipAddress: '',
				margin_charge: '',
				banstatus: '',
				tearoomLabel: [],
				bq: '',
				img: '',
				store_time: '',
				startTime: '',
				endTime: '',
				size: 10,
				page: 1,
				tearoomName: '',
				moneyScope: '',
				isReserve: '',
				isFaceRecognition: '',
				isMusic: 1,
				isWelfare: '', //是否开启爱心活动
				welfareContent: '', //爱心活动介绍

				userNameT: '',
				form: {
					storeId: '',
					merchantId: '',
					tearoomName: '',
					tearoomTitleImage: '',
					shipAddress: '',
					refundAddress: '',
					refundContact: '',
					refundMobile: '',
					createTime: ''
				},
				isshop: true,
				formLabelWidth: '200px',
				tableDataLoading: true,
				dialogFormVisible: false,
				dialogFormVisible1: false,
				tableData: [],
				openValue: 1,
				closeValue: 2,

				userId: '',
				phone1: '',
				nickName: '',
				nickName1: '', //门店用户
				nickName2: '',
				nickName3: '', //团长用户
				userIds: {}, //用户列表
				size1: 10,
				page1: 1,
				dialogFormVisible3: false,
				tableDataLoading4: false,

				cashPledge: '',
				hourPrice: '',
				minutePrice: '',
				subscription: '',
				keyCode: '',

				tearoomId: '',
				tableDataKt: {},
				tableDataLoadingKt: false,
				dialogFormVisibleKt2: false,
				openValue: 1,
				closeValue: 2,

				roomId: '',
				roomType: '',
				roomName: '',
				roomNo: '',
				isOpen: 2,
				isQrCode:2,
				token: '',
				id: '',
				title: '',
				limit: 10,
				yyToken: '',
				yyId: '',
				shopPoiId: '', //

				ksAppId: '', // 快手APPID
				ksAppSecret: '', // 快手AppSecret
				mtEmail: '', // 美团Email
				mtPassword: '', // 美团密码
				mtStoreId: '', // 美团门店ID
				dyPoiId: '', // 抖音门店ID
				dyAppId: '', // 抖音AppID
				dySecret: '', // 抖音Secret
				luckySort: '', //幸运用户序号
				luckyCouponIds: '', //幸运用户奖励优惠券
				luckyContent: '', //幸运用户播报内容
				isRefund: 1, //是否允许操作退款 1是 2否
				isWxPay:1,//是否开启微信支付

				wifiNo: '',
				wifiPwd: '',
				rate: '',
				broadcastContent: '',
				cameraId: '',
				qunImg: '',
				wxCode: '',
				welfareImg: '',
				isOpen: '',
				shopType: 1, //店铺类型 1自营 2三方
				tearoomType:1,//充电类型 1电瓶车 2汽车桩
				isVipPrivilege: 1, //是否享受会员权益  1是 2否
				shopTypeT: '',
				isVipPrivilegeT: '',
				statesnum: [{
						value: '',
						label: '全部'
					},
					{
						value: 1,
						label: '自营'
					},
					{
						value: 2,
						label: '三方'
					},
				],
				isVipPrivilegenum: [{
						value: '',
						label: '全部'
					},
					{
						value: 1,
						label: '是'
					},
					{
						value: 2,
						label: '否'
					},
				],
				dialogFormVisibleKs: false,
				code: '',


			}
		},
		methods: {
			//上传成功
			handleAvatarSuccess3(file, fileList) {
				console.log(file)
				this.tearoomTitleImage = file.data
			},
			handleAvatarSuccess4(file, fileList) {
				console.log(file)
				this.qunImg = file.data
			},
			handleAvatarSuccess5(file, fileList) {
				console.log(file)
				this.wxCode = file.data
			},
			handleAvatarSuccess6(file, fileList) {
				console.log(file)
				this.welfareImg = file.data
			},

			// 图标上传
			handleAvatarSuccess1(file) {
				this.img = file.data;
			},
			// 图标上传
			handleAvatarSuccess2(file) {
				this.form.img = file.data;
			},
			//上传成功
			handleUploadSuccess(file, fileList) {
				this.tearoomCoverImage.push(file.data)
			},
			handleChange(e) {
				console.log(e)
				// this.hideUpload = fileList.length >= this.limit;
			},
			handleRemove(file, fileList) {
				this.hideUpload = fileList.length >= this.limit;
			},
			// 删除
			clear(index) {
				this.tearoomCoverImage.splice(index, 1);
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
			handleSizeChange(val) {
				this.size = val
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val
				this.dataSelect()
			},
			handleSizeChange2(val) {
				this.size1 = val
				this.userClass()
			},
			handleCurrentChange2(val) {
				this.page1 = val
				this.userClass()
			},
			selectTrigger() {
				this.dataSelect()
			},
			// 查询
			selectList() {
				this.dataSelect()
			},
			// 重置
			cleans() {
				this.page = 1
				this.storeNameT = ''
				this.tearoomPhoneT = ''
				this.userNameT = ''
				this.shopTypeT = ''
				this.isVipPrivilegeT = ''


				this.dataSelect()
			},
			// 图标上传
			handleAvatarSuccess(file) {
				this.form.tearoomTitleImage = file.data;
			},
			// 商户详情跳转
			merchDetails(rows) {
				this.$router.push({
					path: '/merchIncome',
					query: {
						merchantId: rows.merchant_id,
						storeId: rows.tearoomId,
						storeName: rows.tearoomName
					}
				});
			},
			// 封禁/解封
			deblocking(row, index) {
				if (index == 1) {
					this.banStatus = 2
				} else if (index == 2) {
					this.banStatus = 3
				} else {
					this.banStatus = 1
				}
				this.$confirm(`确定执行此操作?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('tearoom/updateTearoom'),
						method: 'post',
						data: this.$http.adornParams({
							'tearoomId': row.tearoomId,
							'status': this.banStatus
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
						}
					})
				}).catch(() => {})
			},

			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('tearoom/selectTearoomList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.size,
						'tearoomName': this.storeNameT,
						'userName': this.userNameT,
						'city': '',
						'status': '',
						'tearoomPhone': this.tearoomPhoneT,
						'shopType': this.shopTypeT,
						'isVipPrivilege': this.isVipPrivilegeT,

					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.tableData = returnData
				})
			},
			// 修改状态
			change(val, row) {
				this.$http({
					url: this.$http.adornUrl('admin/storecomplain/status_store'),
					method: 'post',
					params: this.$http.adornParams({
						'store_id': row.store_id,
						'status': row.status
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
							type: 'warning',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					}
				})
			},
			// 添加标签
			addBq() {
				if (this.bq == '' || this.bq == ' ') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入标签名',
						type: 'warning'
					});
					return
				} else {
					console.log('this.bq', this.bq)
					this.tearoomLabel.push(this.bq)
					this.bq = ''
				}
			},
			// 删除标签
			bqClose(index) {
				this.tearoomLabel.splice(index, 1);
			},
			// 获取省市区
			handleChange55(value) {
				console.log(this.$refs['cascaderAddr'].currentLabels)
				value = this.$refs['cascaderAddr'].currentLabels
				if (value) {

					this.tearoomProvince = value[0]
					this.tearoomCity = value[1]
					this.tearoomDistrict = value[2]
					// this.form.city = value[1]
					if (this.tearoomCity == '市辖区') {
						this.tearoomCity = this.tearoomProvince
					}
				}
				// return

				// var value1 = this.$refs['cascaderAddr'].getCheckedNodes()
				// if (value1) {
				// 	var val2 = value1[0].pathLabels
				// 	console.log('val2', val2)
				// 	this.tearoomProvince = val2[0]
				// 	this.tearoomCity = val2[1]
				// 	this.tearoomDistrict = val2[2]
				// 	// this.form.city = value[1]
				// 	if (this.tearoomCity == '市辖区') {
				// 		this.tearoomCity = this.tearoomProvince
				// 	}
				// }
			},
			addtea() {
				this.storeAddress = []
				this.storeAddre = '请选择城市'
				this.margin_charge = ''
				this.banstatus = ''
				this.tearoomTitleImage = ''
				this.storephone = ''
				this.tearoomCoverImage = []
				this.tearoomName = ''
				this.tearoomAddress = ''
				this.tearoomLongitude = ''
				this.tearoomLatitude = ''
				this.startTime = ''
				this.endTime = ''
				this.tearoomLabel = []
				this.tearoomProvince = ''
				this.tearoomCity = ''
				this.tearoomDistrict = ''
				this.userId = ''
				this.nickName1 = ''
				this.moneyScope = ''
				this.isReserve = 2
				this.keyCode = ''
				this.cashPledge = ''
				this.hourPrice = ''
				this.minutePrice = ''
				this.subscription = ''
				this.yyToken = ''
				this.yyId = ''
				this.rate = ''
				this.shopPoiId = ''
				this.wifiNo = ''
				this.wifiPwd = ''
				this.broadcastContent = ''
				this.cameraId = ''
				this.qunImg = ''
				this.wxCode = ''
				this.welfareImg = ''
				this.isOpen = 1
				this.isQrCode = 2
				this.shopType = 1
				this.tearoomType = 1
				this.isVipPrivilege = 2
				this.isWelfare = 2
				this.welfareContent = ''
				this.isFaceRecognition = 2
				this.isMusic = 1
				this.ksAppId = ''
				this.ksAppSecret = ''
				this.mtEmail = ''
				this.mtPassword = ''
				this.mtStoreId = ''
				this.dyPoiId = ''
				this.dyAppId = ''
				this.dySecret = ''
				this.luckySort = ''
				this.luckyCouponIds = ''
				this.luckyContent = ''
				this.isRefund = 1
				this.isWxPay = 1




				this.getMyLocation();
				this.dialogFormVisible1 = true
			},
			addteaclassroom() {
				console.log(this.tearoomAddress)
				console.log(this.tearoomDistrict)
				console.log(this.tearoomCity)
				console.log(this.tearoomProvince)
				var time = this.store_time.toString()
				var newtime = time.replace(",", "-")
				console.log(newtime)
				// if (this.margin_charge == '') {
				// 	this.$notify({
				// 		message: '请填写平台抽取手续费',
				// 		type: 'error',
				// 		duration: 1500
				// 	})
				// 	return
				// }
				if (this.tearoomTitleImage == '') {
					this.$notify({
						message: '请添加充电站logo',
						type: 'error',
						duration: 1500
					})
					return
				}
				// if (this.tearoomCoverImage == '') {
				// 	this.$notify({
				// 		message: '请添加充电站轮播图',
				// 		type: 'error',
				// 		duration: 1500
				// 	})
				// 	return
				// }
				if (this.storephone == '') {
					this.$notify({
						message: '请填写充电站电话',
						type: 'error',
						duration: 1500
					})
					return
				}
				if (this.tearoomName == '') {
					this.$notify({
						message: '请填写充电站名称',
						type: 'error',
						duration: 1500
					})
					return
				}
				if (this.tearoomAddress == '') {
					this.$notify({
						message: '请完善充电站位置,经纬度进行选择',
						type: 'error',
						duration: 1500
					})
					return
				}
				if (this.startTime == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择开店时间',
						type: 'warning'
					})
					return
				}
				if (this.endTime == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择闭店时间',
						type: 'warning'
					})
					return
				}
				// if (this.tearoomLabel == '') {
				// 	this.$notify({
				// 		message: '请填写充电站标签',
				// 		type: 'warning',
				// 		duration: 1500
				// 	})
				// 	return
				// }
				// if (this.keyCode == '') {
				// 	this.$notify({
				// 		message: '请填写设备标识码',
				// 		type: 'warning',
				// 		duration: 1500
				// 	})
				// 	return
				// }
				// if (this.yyToken == '') {
				// 	this.$notify({
				// 		message: '请填写语音播报token',
				// 		type: 'warning',
				// 		duration: 1500
				// 	})
				// 	return
				// }
				// if (this.yyId == '') {
				// 	this.$notify({
				// 		message: '请填写语音播报ID',
				// 		type: 'warning',
				// 		duration: 1500
				// 	})
				// 	return
				// }

				// if (this.cashPledge === '') {
				// 	this.$notify({
				// 		message: '押金不能为空',
				// 		type: 'warning',
				// 		duration: 1500
				// 	})
				// 	return
				// }
				// if (this.hourPrice === '') {
				// 	this.$notify({
				// 		message: '每小时价格不能为空',
				// 		type: 'warning',
				// 		duration: 1500
				// 	})
				// 	return
				// }
				// if (this.minutePrice === '') {
				// 	this.$notify({
				// 		message: '每分钟价格不能为空',
				// 		type: 'warning',
				// 		duration: 1500
				// 	})
				// 	return
				// }
				if (this.rate === '') {
					this.$notify({
						message: ' 佣金比例不能为空',
						type: 'warning',
						duration: 1500
					})
					return
				}
				if (this.banstatus === '') {
					this.$notify({
						message: '请选择店铺状态',
						type: 'warning',
						duration: 1500
					})
					return
				}
				// if (this.moneyScope === '') {
				// 	this.$notify({
				// 		message: '请选择零钱操作权限',
				// 		type: 'warning',
				// 		duration: 1500
				// 	})
				// 	return
				// }

				// var titmes = this.openTime + '-' + this.closeTime
				let userid = this.$cookie.get('userId')
				this.$http({
					url: this.$http.adornUrl('tearoom/insertTearoom'),
					method: 'post',
					data: this.$http.adornParams({
						'userid': userid,
						'status': this.banstatus,
						'tearoomTitleImage': this.tearoomTitleImage,
						'tearoomPhone': this.storephone,
						'tearoomCoverImage': this.tearoomCoverImage.toString(),
						'tearoomName': this.tearoomName,
						'tearoomAddress': this.tearoomAddress,
						'tearoomLongitude': this.tearoomLongitude,
						'tearoomLatitude': this.tearoomLatitude,
						'startTime': this.startTime,
						'endTime': this.endTime,
						'tearoomLabel': this.tearoomLabel.toString(),
						'tearoomProvince': this.tearoomProvince,
						'tearoomCity': this.tearoomCity,
						'tearoomDistrict': this.tearoomDistrict,
						'userId': this.userId,
						'cashPledge': this.cashPledge,
						'hourPrice': this.hourPrice,
						'minutePrice': this.minutePrice,
						'subscription': this.subscription,
						'keyCode': this.keyCode,
						'yyToken': this.yyToken,
						'yyId': this.yyId,
						'wifiNo': this.wifiNo,
						'wifiPwd': this.wifiPwd,
						'moneyScope': this.moneyScope,
						'isReserve': this.isReserve,
						'rate': this.rate,
						'broadcastContent': this.broadcastContent,
						'cameraId': this.cameraId,
						'qunImg': this.qunImg,
						'wxCode': this.wxCode,
						'isOpen': this.isOpen,
						'isQrCode':this.isQrCode,
						'shopType': this.shopType,
						'isVipPrivilege': this.isVipPrivilege,
						'shopPoiId': this.shopPoiId,
						'isWelfare': this.isWelfare,
						'welfareContent': this.welfareContent,
						'welfareImg': this.welfareImg,
						'isFaceRecognition': this.isFaceRecognition,
						'isMusic': this.isMusic,
						'ksAppId': this.ksAppId,
						'ksAppSecret': this.ksAppSecret,
						'mtEmail': this.mtEmail,
						'mtPassword': this.mtPassword,
						'mtStoreId': this.mtStoreId,
						'dyPoiId': this.dyPoiId,
						'dyAppId': this.dyAppId,
						'dySecret': this.dySecret,
						'tearoomType':this.tearoomType,
						'luckySort': this.luckySort,
						'luckyCouponIds': this.luckyCouponIds,
						'luckyContent': this.luckyContent,
						'isRefund': this.isRefund,
						'isWxPay':this.isWxPay,


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
								this.dialogFormVisible1 = false
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
			//定位获得当前位置信息
			getMyLocation() {
				var geolocation = new qq.maps.Geolocation("G5GBZ-NJ664-5KUUL-FBXE5-PQ5HK-C4BPJ", "充电桩");
				geolocation.getIpLocation(this.showPosition, this.showErr);
				// geolocation.getLocation(this.showPosition, this.showErr);//或者用getLocation精确度比较高
			},
			showPosition(position) {
				console.log(position);
				this.setMap();
			},
			showErr(e) {
				console.log("定位失败", e);
				this.getMyLocation(); //定位失败再请求定位，测试使用
			},
			//位置信息在地图上展示
			setMap() {
				//步骤：定义map变量 调用 qq.maps.Map() 构造函数   获取地图显示容器
				//设置地图中心点
				var myLatlng = new qq.maps.LatLng(this.tearoomLatitude, this.tearoomLongitude);
				//定义工厂模式函数
				var myOptions = {
					zoom: 13, //设置地图缩放级别
					center: myLatlng, //设置中心点样式
					mapTypeId: qq.maps.MapTypeId.ROADMAP //设置地图样式详情参见MapType
				}
				// //获取dom元素添加地图信息
				var map = new qq.maps.Map(document.getElementById("container1"), myOptions);
				//给地图添加点击事件

				//给定位的位置添加图片标注
				var marker = new qq.maps.Marker({
					position: myLatlng,
					map: map
				});
				// `````````````
				var that = this;
				if (that.tearoomLongitude == '') {
					var center = new qq.maps.LatLng(34.34281541842994, 108.93970884382725);
				} else {
					var center = new qq.maps.LatLng(that.tearoomLatitude, that.tearoomLongitude);
				}
				var map = new qq.maps.Map(document.getElementById("container1"), {
					center: center,
					zoom: 13
				});
				var marker = new qq.maps.Marker({
					position: center,
					map: map
				});
				var latlngBounds = new qq.maps.LatLngBounds();
				qq.maps.event.addListener(map, "click", function(event) {
					console.log(event, qq.maps);
					that.tearoomLongitude = event.latLng.getLng(); // 经度
					that.tearoomLatitude = event.latLng.getLat(); // 纬度
					jsonp('https://apis.map.qq.com/ws/geocoder/v1/?location=' + event.latLng.getLat() + ',' + event
						.latLng
						.getLng() + '&key=ZBABZ-ZWECU-UQTVV-4LYDR-COK3F-5SF75&get_poi=1&output=jsonp', {
							myCustomUrlParam: 'veryNice'
						}).then(response => {
						console.log('response', response)
						that.tearoomAddress = response.result.formatted_addresses.recommend
						that.tearoomProvince = response.result.ad_info.province
						that.tearoomCity = response.result.ad_info.city
						that.tearoomDistrict = response.result.ad_info.district

					}).catch(error => {
						// handle error
					}).then(() => {
						// always executed
					});
					if (markersArray) {
						for (let i in markersArray) {
							markersArray[i].setMap(null);
						}
					}
					var marker = new qq.maps.Marker({
						map: map,
						position: event.latLng
					});
					markersArray.push(marker);
				});
				geocoder = new qq.maps.Geocoder({
					complete: function(result) {
						console.log(result);
						that.tearoomLongitude = result.detail.location.lng;
						that.tearoomLatitude = result.detail.location.lat;
						map.setCenter(result.detail.location);
						var marker = new qq.maps.Marker({
							map: map,
							position: result.detail.location
						});
						markersArray.push(marker);
					}
				});
			},
			// 地图定位
			selectDz() {
				console.log('搜索地址：', this.tearoomProvince + this.tearoomCity + this.tearoomDistrict + this.tearoomAddress)
				var addr = this.tearoomProvince + this.tearoomCity + this.tearoomDistrict + this.tearoomAddress //
				let that = this
				jsonp('https://apis.map.qq.com/ws/geocoder/v1/?address==' + addr +
					'&key=ZBABZ-ZWECU-UQTVV-4LYDR-COK3F-5SF75&get_poi=1&output=jsonp', {
						myCustomUrlParam: 'veryNice'
					}).then(response => {
					// handle success
					console.log('response', response)
					that.tearoomLongitude = response.result.location.lng; // 经度
					that.tearoomLatitude = response.result.location.lat; // 纬度
					// that.address = response.result.title
					that.setMap()
				}).catch(error => {
					// handle error
				}).then(() => {
					// always executed
				});
			},
			// 获取用户列表弹框
			userselect() {
				this.dialogFormVisible3 = true
				this.tableDataLoading4 = true
				this.page1 = 1
				this.phone1 = ''
				this.nickName2 = ''
				this.userClass()
			},
			// 获取用户列表
			userClass() {
				this.tableDataLoading4 = true
				this.$http({
					url: this.$http.adornUrl('user/selectUserList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page1,
						'limit': this.size1,
						'userName': this.nickName2,
						'inviterCode': '',
						'startTime': '',
						'endTime': '',
						'isAuthentication': '',
						'phone': this.phone1
					})

				}).then(({
					data
				}) => {
					this.tableDataLoading4 = false
					let returnData = data.data
					this.userIds = returnData
				})
			},
			// 查询用户列表
			userclick() {
				this.page1 = 1
				this.userClass()
			},
			// 重置用户列表数据
			userclose() {
				this.phone1 = ''
				this.nickName2 = ''
				this.userClass()
			},
			// 确定用户
			confirm(row) {
				this.userId = row.userId
				this.nickName1 = row.userName
				this.dialogFormVisible3 = false
			},
			// 空调弹框
			merchLook(row) {
				console.log(row)
				this.tearoomId = row.tearoomId
				this.dataSelectKt()
				this.dialogFormVisible = true

			},
			// 空调 数据列表
			dataSelectKt() {
				this.tableDataLoadingKt = true
				this.$http({
					url: this.$http.adornUrl('room/selectRoomList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.size,
						'roomName': this.storeNameT,
						'goodsId': this.tearoomId,

					})
				}).then(({
					data
				}) => {
					this.tableDataLoadingKt = false
					let returnData = data.data
					this.tableDataKt = returnData
				})
			},
			addNotice() {
				this.title = '添加'
				this.roomId = ''
				this.roomType = ''
				this.roomName = ''
				this.roomNo = ''
				this.isOpen = 2
				this.dialogFormVisibleKt2 = true
			},
			handleCurrentChangeKt(val) {
				this.page = val
				this.dataSelect()
			},
			handleSizeChangeKt(val) {
				this.limit = val
				this.dataSelect()
			},
			// 添加banner图
			addNoticeTo() {

				if (this.roomName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入设备名称',
						type: 'warning'
					});
					return
				}
				// if (this.roomType == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请选择设备类型',
				// 		type: 'warning'
				// 	});
				// 	return
				// }
				if (this.roomNo === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入设备编号',
						type: 'warning'
					});
					return
				}
				if (this.title == '添加') {
					var url = 'room/insertRoom'
				} else {
					var url = 'room/updateRoom'
				}
				this.$http({
					url: this.$http.adornUrl(url),
					method: 'post',
					data: this.$http.adornData({
						'goodsId': this.tearoomId,
						'roomId': this.roomId,
						'roomType': this.roomType,
						'roomName': this.roomName,
						'roomNo': this.roomNo,
						'isOpen': this.isOpen,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisibleKt2 = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelectKt()
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
			// 修改
			amendBanner(rows) {
				this.title = '修改'
				this.roomId = rows.roomId
				this.roomType = rows.roomType
				this.roomName = rows.roomName
				this.roomNo = rows.roomNo
				this.isOpen = rows.isOpen
				this.dialogFormVisibleKt2 = true

			},
			// 状态
			changeKt(row) {
				this.$http({
					url: this.$http.adornUrl('room/updateRoom'),
					method: 'post',
					data: this.$http.adornData({
						'goodsId': row.goodsId,
						'roomId': row.roomId,
						'roomType': row.roomType,
						'roomName': row.roomName,
						'roomNo': row.roomNo,
						'isOpen': row.isOpen,
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
								this.dataSelectKt()
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {
								this.dataSelectKt()
							}
						})
					}

				})
			},
			//删除空调
			deletes(row) {
				let delid = row.roomId
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`room/deleteRoomById?roomId=${delid}`),
						method: 'post',
						data: this.$http.adornData({})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dataSelectKt()
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
			// 获取快手核销token 弹框
			ksRefreshTokenBtn(row) {
				this.dialogFormVisibleKs = true
				this.code = ''
				this.tearoomId = row.tearoomId
			},
			ksNoticeTo() {
				if (this.code === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入CODE',
						type: 'warning'
					});
					return
				}

				this.$http({
					url: this.$http.adornUrl('app/selfCoupon/oauth2Token'),
					method: 'get',
					// data: this.$http.adornData({
					params: this.$http.adornParams({
						'tearoomId': this.tearoomId,
						'code': this.code,
					})
				}).then(({
					data
				}) => {
					if (data.result == 1) {
						this.dialogFormVisibleKs = false
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
							message: data.error_msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {}
						})
					}

				})
			},
		},
		mounted() {
			this.dataSelect()
		}
	}
</script>

<style scoped>
.merchant-list-page__filter-select {
	width: 150px;
}

.merchant-list-page__filter-input {
	width: 160px;
}

.merchant-list-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.merchant-list-page__toolbar {
	display: flex;
	justify-content: flex-end;
	margin-bottom: 12px;
}

.merchant-list-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

.nf-table-card .el-button + .el-button {
	margin-left: 0 !important;
	margin-top: 5px !important;
}

	.divhove {
		position: absolute;
		width: 100%;
		height: 100%;
		left: 0;
		top: 0;
		opacity: 0;
		cursor: default;
		text-align: center;
		padding-top: 50%;
		border-radius: 6px;
		background-color: rgba(0, 0, 0, .5);
		-webkit-transition: opacity .3s;
		transition: opacity .3s;
	}

	.el-tag--medium {
		margin-right: 10px;
	}

	.divhove:hover {
		opacity: 0.8;
	}

	.text_color span {
		margin-right: 5px;
	}

	.shop_item {
		/* margin-top: 60px; */
		padding-left: 20px;
	}

	/* 规格 */
	.customWidth {
		width: 60% !important;
	}

	.ruleitem {
		padding-left: 10px;
		margin-top: 15px;
	}

	.ruleitem .ruleicon {
		position: relative;
		top: 3px;
		left: 2px;
		width: 1.2em;
		height: 1.2em;
	}

	.el-tag+.el-tag {
		margin-left: 10px;
	}

	.button-new-tag {
		height: 32px;
		line-height: 31px;
		padding-top: 0;
		padding-bottom: 0;
		border-radius: 4px;
		border-top-left-radius: 0;
		border-bottom-left-radius: 0;
		background-color: #f5f7fa;
		position: relative;
		left: -5px;
		border-color: #dcdfe6;
		border: 1px solid #dcdfe6;
	}

	.input-new-tag {
		width: 90px;
		margin-left: 10px;
		vertical-align: bottom;
	}

	.el-input--small .el-input__inner {
		border-top-right-radius: 0;
		border-bottom-right-radius: 0;
	}

	/* switch按钮样式 */
	::v-deep .switchClass .el-switch__label {
		position: absolute;
		display: none;
		color: #fff !important;
	}

	/*打开时文字位置设置*/
	::v-deep .switchClass .el-switch__label--right {
		z-index: 1;
	}

	/* 调整打开时文字的显示位子 */
	::v-deep .switchClass .el-switch__label--right span {
		margin-right: 13px;
		font-size: 12px;

	}

	/*关闭时文字位置设置*/
	::v-deep .switchClass .el-switch__label--left {
		z-index: 1;
	}

	/* 调整关闭时文字的显示位子 */
	::v-deep .switchClass .el-switch__label--left span {
		margin-left: 17px;
		font-size: 12px;
	}

	/*显示文字*/
	::v-deep .switchClass .el-switch__label.is-active {
		display: block;
	}

	/* 调整按钮的宽度 */
	::v-deep .switchClass.el-switch .el-switch__core,
	.el-switch .el-switch__label {
		width: 42px !important;
		margin: 0;
	}
</style>