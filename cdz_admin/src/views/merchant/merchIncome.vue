<template>
	<div class="nf-page merch-income-page">
		<a class="merch-income-page__back" href="#" @click.prevent="prev">
			<icon-svg name="jiantou" class="merch-income-page__back-icon"></icon-svg>
			返回
		</a>

		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">商家管理</span>
			<h2 class="nf-page-hero__title">门店详情</h2>
			<p class="nf-page-hero__desc">查看与编辑充电站基本信息、价格配置、充电统计与收入、充电桩管理及订单优惠券等数据，支持返回列表。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<el-tabs v-model="activeName" @tab-click="handleClick" class="merch-income-page__tabs">
			<el-tab-pane label="充电站信息" name="first">
				<div class="nf-table-card merch-income-page__form-card">
					<div class="merch-income-page__section-head">
						<i class="el-icon-office-building merch-income-page__section-icon"></i>
						<span>充电站信息</span>
					</div>
					<div class="shop_item">
						<div class="shop_item" style="text-align:right;margin-right: 10%;">
							<el-button v-if="isbtn" style="margin-bottom: 20px;" size="mini" type="primary"
								:disabled="!isAuth('merchantList:update')" @click="updates">修改
							</el-button>
							<div v-if="!isbtn">
								<el-button style="margin-bottom: 20px;" size="mini" type="info" @click="cancel">取消
								</el-button>
								<el-button style="margin-bottom: 20px;" size="mini" type="primary" @click="saveClick">保存
								</el-button>
							</div>
						</div>
						<div class="shop_content" style="max-width: 800px;margin: auto;">
							<div class="content_item">
								<div style="display: flex;align-items: center;margin: 2% 0;">
									<span style="display: inline-block;text-align: left;width: 200px;">充电站logo：</span>
									<div v-if="isshow"
										style="width:148px;height:148px;background-color: #fbfdff; border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
										<img v-if="logo" :src="logo" class="avatar"
											style="width: 148px;height: 148px;border-radius:6px;" />
									</div>
									<div v-if="!isshow"
										style="width:148px;height:148px;background-color: #fbfdff; border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
										<el-upload class="avatar-uploader" v-model="logo"
											:action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
											:on-success="handleAvatarSuccess">
											<img v-if="logo" :src="logo" class="avatar"
												style="width: 148px;height: 148px;border-radius:6px;" />
											<i v-else class="el-icon-plus avatar-uploader-icon"
												style="font-size: 28px;color: #8c939d"></i>
										</el-upload>
									</div>
								</div>
							</div>
							<!-- <div class="shop_content" style="max-width: 800px;margin: auto;">
								<div class="content_item">
									<div style="display: flex;align-items: center;margin: 2% 0;">
										<span style="display: inline-block;text-align: left;width: 200px;">群二维码：</span>
										<div v-if="isshow"
											style="width:148px;height:148px;background-color: #fbfdff; border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
											<img v-if="qunImg" :src="qunImg" class="avatar"
												style="width: 148px;height: 148px;border-radius:6px;" />
										</div>
										<div v-if="!isshow"
											style="width:148px;height:148px;background-color: #fbfdff; border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;position: relative;">
											<el-upload class="avatar-uploader" v-model="qunImg"
												:action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
												:on-success="handleAvatarSuccess2">
												<img v-if="qunImg" :src="qunImg" class="avatar"
													style="width: 148px;height: 148px;border-radius:6px;" />
												<div @click="selectDz1()" class="divhove" v-if="qunImg">
													<i class="el-icon-delete"
														style="margin: 0;font-size: 18px;color: #fff;"></i>
												</div>
												<i v-else class="el-icon-plus avatar-uploader-icon"
													style="font-size: 28px;color: #8c939d"></i>
											</el-upload>

										</div>
									</div>
								</div>
							</div>
							<div class="shop_content" style="max-width: 800px;margin: auto;">
								<div class="content_item">
									<div style="display: flex;align-items: center;margin: 2% 0;">
										<span style="display: inline-block;text-align: left;width: 200px;">微信二维码：</span>
										<div v-if="isshow"
											style="width:148px;height:148px;background-color: #fbfdff; border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
											<img v-if="wxCode" :src="wxCode" class="avatar"
												style="width: 148px;height: 148px;border-radius:6px;" />
										</div>
										<div v-if="!isshow"
											style="width:148px;height:148px;background-color: #fbfdff; border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;position: relative;">
											<el-upload class="avatar-uploader" v-model="wxCode"
												:action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
												:on-success="handleAvatarSuccess3">
												<img v-if="wxCode" :src="wxCode" class="avatar"
													style="width: 148px;height: 148px;border-radius:6px;" />
												<div @click="selectDz2()" class="divhove" v-if="wxCode">
													<i class="el-icon-delete"
														style="margin: 0;font-size: 18px;color: #fff;"></i>
												</div>
												<i v-else class="el-icon-plus avatar-uploader-icon"
													style="font-size: 28px;color: #8c939d"></i>
											</el-upload>

										</div>
									</div>
								</div>
							</div> -->
							<div class="content_item" style="margin-bottom: 10px;">
								<span style="width: 200px;display: inline-block;">绑定用户:</span>
								<el-input v-model="nickName1" v-if="isshow" disabled style="width:50%;" class="margin15"
									placeholder="请选择绑定用户">
								</el-input>
								<el-input v-model="nickName1" v-if="!isshow" @focus="userselect(1)" style="width:50%;"
									class="margin15" placeholder="请选择绑定用户">
								</el-input>
							</div>
							<!-- <div class="content_item" style="margin-bottom: 10px;">
								<span style="width: 200px;display: inline-block;">员工用户:</span>
								<div v-if="isshow" style="width: auto;margin-left: 200px;">
									<span v-if="userEntityList" style="display: inline-block;width: auto;">
										<el-tag v-for="(item,index) of userEntityList" :key="index" style="margin:5px;"
											type="info">{{item.userName}}</el-tag>
									</span>
								</div>
								<div v-if="!isshow" style="width: auto;margin-left: 200px;">
									<span v-if="userEntityList" style="display: inline-block;width: auto;">
										<el-tag v-for="(item,index) of userEntityList" :key="index" type="info" closable
											style="margin:5px;" @close="ygClose(index)">{{item.userName}}</el-tag>
									</span>
									<el-button icon="el-icon-plus" size="mini" plain circle type="primary"
										@click="userselectYg(1)"></el-button>
								</div>
							</div> -->
							<!-- <div class="content_item" style="margin-bottom: 10px;">
								<span style="width: 200px;display: inline-block;">绑定商铺:</span>
								<div v-if="isshow" style="display: inline-block;width: auto;">
									<span v-if="shopsList" style="display: inline-block;width: auto;">
										<el-tag v-for="(item,index) of shopsList" :key="index"
											type="info">{{item.shopName}}</el-tag>
									</span>
								</div>
								<div v-if="!isshow" style="display: inline-block;width: auto;">
									<span v-if="shopsList" style="display: inline-block;width: auto;">
										<el-tag v-for="(item,index) of shopsList" :key="index" type="info" closable
											@close="spClose(index)">{{item.shopName}}</el-tag>
									</span>
									<el-button icon="el-icon-plus" size="mini" plain circle type="primary"
										@click="shopselectYg()"></el-button>
								</div>
							</div> -->
							<div class="content_item">
								<span style="display: inline-block;text-align: left;width: 200px;">店铺类型:</span>
								<div class="margin15" style="margin-left: 200px;margin-top: -13px;">
									<el-radio-group v-if="isshow" disabled v-model="shopType">
										<el-radio :label="1">自营</el-radio>
										<el-radio :label="2">三方</el-radio>
									</el-radio-group>
								</div>
								<div class="margin15" style="margin-left: 200px;margin-top: -13px;">
									<el-radio-group v-if="!isshow" v-model="shopType">
										<el-radio :label="1">自营</el-radio>
										<el-radio :label="2">三方</el-radio>
									</el-radio-group>
								</div>
							</div>
							<div class="content_item">
								<span style="display: inline-block;text-align: left;width: 200px;">充电类型:</span>
								<div class="margin15" style="margin-left: 200px;margin-top: -13px;">
									<el-radio-group v-if="isshow" disabled v-model="tearoomType">
										<el-radio :label="1">电瓶车</el-radio>
										<el-radio :label="2">汽车桩</el-radio>
									</el-radio-group>
								</div>
								<div class="margin15" style="margin-left: 200px;margin-top: -13px;">
									<el-radio-group v-if="!isshow" v-model="tearoomType">
										<el-radio :label="1">电瓶车</el-radio>
										<el-radio :label="2">汽车桩</el-radio>
									</el-radio-group>
								</div>
							</div>
							
							<div class="content_item">
								<span style="width: 200px;text-align: left;display:inline-block;">充电站名称:</span>
								<el-input style="width:50%;" v-if="isshow" disabled class="margin15"
									placeholder="请输入充电站名称" v-model="storeName" autosize></el-input>
								<el-input style="width:50%;" v-if="!isshow" class="margin15" placeholder="请输入充电站名称"
									v-model="storeName" autosize></el-input>
							</div>
							<div class="content_item">
								<span style="width: 200px;text-align: left;display:inline-block;">充电站电话:</span>
								<el-input style="width:50%;" v-if="isshow" disabled class="margin15"
									placeholder="请输入充电站电话" v-model="tearoomPhone" autosize></el-input>
								<el-input style="width:50%;" v-if="!isshow" class="margin15" placeholder="请输入充电站电话"
									v-model="tearoomPhone" autosize></el-input>
							</div>
							<div class="content_item">
								<span style="display: inline-block;text-align: left;width: 200px;">佣金比例:</span>
								<el-input style="width:50%;" v-if="isshow" disabled class="margin15"
									placeholder="请输入佣金比例" v-model="rate" autosize></el-input>
								<el-input style="width:50%;" v-if="!isshow" class="margin15" placeholder="请输入佣金比例"
									v-model="rate" autosize></el-input>
							</div>
							<!-- <div class="content_item" style="margin-bottom: 10px;">
								<span style="display: inline-block;text-align: left;width: 200px;">设备标识码:</span>
								<el-input style="width:50%;" v-if="isshow" disabled class="margin15"
									placeholder="请输入设备标识码" v-model="keyCode" autosize></el-input>
								<el-input style="width:50%;" v-if="!isshow" class="margin15" placeholder="请输入设备标识码"
									v-model="keyCode" autosize></el-input>
							</div>
							<div class="content_item" style="margin-bottom: 10px;">
								<span style="display: inline-block;text-align: left;width: 200px;">快手门店ID:</span>
								<el-input style="width:50%;" v-if="isshow" disabled class="margin15"
									placeholder="请输入快手门店ID" v-model="shopPoiId" autosize></el-input>
								<el-input style="width:50%;" v-if="!isshow" class="margin15" placeholder="请输入快手门店ID"
									v-model="shopPoiId" autosize></el-input>
							</div>
							<div class="content_item" style="margin-bottom: 10px;">
								<span style="display: inline-block;text-align: left;width: 200px;">快手APPID:</span>
								<el-input style="width:50%;" v-if="isshow" disabled class="margin15"
									placeholder="请输入快手APPID" v-model="ksAppId" autosize></el-input>
								<el-input style="width:50%;" v-if="!isshow" class="margin15" placeholder="请输入快手APPID"
									v-model="ksAppId" autosize></el-input>
							</div>
							<div class="content_item" style="margin-bottom: 10px;">
								<span style="display: inline-block;text-align: left;width: 200px;">快手AppSecret:</span>
								<el-input style="width:50%;" v-if="isshow" disabled class="margin15"
									placeholder="请输入快手AppSecret" v-model="ksAppSecret" autosize></el-input>
								<el-input style="width:50%;" v-if="!isshow" class="margin15"
									placeholder="请输入快手AppSecret" v-model="ksAppSecret" autosize></el-input>
							</div>
							<div class="content_item" style="margin-bottom: 10px;">
								<span style="display: inline-block;text-align: left;width: 200px;">美团Email:</span>
								<el-input style="width:50%;" v-if="isshow" disabled class="margin15"
									placeholder="请输入美团Email" v-model="mtEmail" autosize></el-input>
								<el-input style="width:50%;" v-if="!isshow" class="margin15" placeholder="请输入美团Email"
									v-model="mtEmail" autosize></el-input>
							</div>
							<div class="content_item" style="margin-bottom: 10px;">
								<span style="display: inline-block;text-align: left;width: 200px;">美团密码:</span>
								<el-input style="width:50%;" v-if="isshow" disabled class="margin15"
									placeholder="请输入美团密码" v-model="mtPassword" autosize></el-input>
								<el-input style="width:50%;" v-if="!isshow" class="margin15" placeholder="请输入美团密码"
									v-model="mtPassword" autosize></el-input>
							</div>
							<div class="content_item" style="margin-bottom: 10px;">
								<span style="display: inline-block;text-align: left;width: 200px;">美团门店ID:</span>
								<el-input style="width:50%;" v-if="isshow" disabled class="margin15"
									placeholder="请输入美团门店ID" v-model="mtStoreId" autosize></el-input>
								<el-input style="width:50%;" v-if="!isshow" class="margin15" placeholder="请输入美团门店ID"
									v-model="mtStoreId" autosize></el-input>
							</div>
							<div class="content_item" style="margin-bottom: 10px;">
								<span style="display: inline-block;text-align: left;width: 200px;">抖音门店ID:</span>
								<el-input style="width:50%;" v-if="isshow" disabled class="margin15"
									placeholder="请输入抖音门店ID" v-model="dyPoiId" autosize></el-input>
								<el-input style="width:50%;" v-if="!isshow" class="margin15" placeholder="请输入抖音门店ID"
									v-model="dyPoiId" autosize></el-input>
							</div>
							<div class="content_item" style="margin-bottom: 10px;">
								<span style="display: inline-block;text-align: left;width: 200px;">抖音AppID:</span>
								<el-input style="width:50%;" v-if="isshow" disabled class="margin15"
									placeholder="请输入抖音AppID" v-model="dyAppId" autosize></el-input>
								<el-input style="width:50%;" v-if="!isshow" class="margin15" placeholder="请输入抖音AppID"
									v-model="dyAppId" autosize></el-input>
							</div>
							<div class="content_item" style="margin-bottom: 10px;">
								<span style="display: inline-block;text-align: left;width: 200px;">抖音Secret:</span>
								<el-input style="width:50%;" v-if="isshow" disabled class="margin15"
									placeholder="请输入抖音Secret" v-model="dySecret" autosize></el-input>
								<el-input style="width:50%;" v-if="!isshow" class="margin15" placeholder="请输入抖音Secret"
									v-model="dySecret" autosize></el-input>
							</div>
							<div class="content_item" style="margin-bottom: 10px;">
								<span style="display: inline-block;text-align: left;width: 200px;">wifi账号:</span>
								<el-input style="width:50%;" v-if="isshow" disabled class="margin15"
									placeholder="请输入wifi账号" v-model="wifiNo" autosize></el-input>
								<el-input style="width:50%;" v-if="!isshow" class="margin15" placeholder="请输入wifi账号"
									v-model="wifiNo" autosize></el-input>
							</div>
							<div class="content_item" style="margin-bottom: 10px;">
								<span style="display: inline-block;text-align: left;width: 200px;">wifi密码:</span>
								<el-input style="width:50%;" v-if="isshow" disabled class="margin15"
									placeholder="请输入wifi密码" v-model="wifiPwd" autosize></el-input>
								<el-input style="width:50%;" v-if="!isshow" class="margin15" placeholder="请输入wifi密码"
									v-model="wifiPwd" autosize></el-input>
							</div>
							<div class="content_item" style="margin-bottom: 10px;">
								<span style="display: inline-block;text-align: left;width: 200px;">语音播报token:</span>
								<el-input style="width:50%;" v-if="isshow" disabled class="margin15"
									placeholder="请输入语音播报token" v-model="yyToken" autosize></el-input>
								<el-input style="width:50%;" v-if="!isshow" class="margin15" placeholder="请输入语音播报token"
									v-model="yyToken" autosize></el-input>
							</div>
							<div class="content_item" style="margin-bottom: 10px;">
								<span style="display: inline-block;text-align: left;width: 200px;">语音播报ID:</span>
								<el-input style="width:50%;" v-if="isshow" disabled class="margin15"
									placeholder="请输入语音播报ID" v-model="yyId" autosize></el-input>
								<el-input style="width:50%;" v-if="!isshow" class="margin15" placeholder="请输入语音播报ID"
									v-model="yyId" autosize></el-input>
							</div>
							<div class="content_item" style="margin-bottom: 10px;">
								<span style="display: inline-block;text-align: left;width: 200px;">播报内容:</span>
								<el-input style="width:50%;" v-if="isshow" disabled class="margin15"
									placeholder="请输入播报内容" v-model="broadcastContent" autosize></el-input>
								<el-input style="width:50%;" v-if="!isshow" class="margin15" placeholder="请输入播报内容"
									v-model="broadcastContent" autosize></el-input>
							</div>
							<div class="content_item" style="margin-bottom: 10px;">
								<span style="display: inline-block;text-align: left;width: 200px;">摄像头配置:</span>
								<el-input style="width:50%;" v-if="isshow" disabled class="margin15"
									placeholder="请输入摄像头配置" v-model="cameraId" autosize></el-input>
								<el-input style="width:50%;" v-if="!isshow" class="margin15" placeholder="请输入摄像头配置"
									v-model="cameraId" autosize></el-input>
							</div> -->
							<div class="content_item">
								<span style="display: inline-block;text-align: left;width: 200px;">充电站销量:</span>
								<el-input style="width:50%;" disabled class="margin15" v-model="sale"
									autosize></el-input>
							</div>
							<div class="content_item" style="margin-bottom: 10px;">
								<span style="display: inline-block;text-align: left;width: 200px;">城市：</span>
								<el-cascader v-if="isshow" disabled style="width:50%" size="large" :options="options"
									ref="cascaderAddr" v-model="storeAddress" :placeholder="storeAddre"
									@change="handleChange55" :value="storeAddre">
								</el-cascader>
								<el-cascader v-if="!isshow" style="width:50%" size="large" :options="options"
									ref="cascaderAddr" v-model="storeAddress" :placeholder="storeAddre"
									@change="handleChange55" :value="storeAddre">
								</el-cascader>
							</div>
							<div class="content_item">
								<span style="display: inline-block;text-align: left;width: 200px;">充电站地址:</span>
								<el-input style="width:50%;" v-if="isshow" disabled class="margin15"
									placeholder="请输入充电站地址" v-model="shipAddress" autosize></el-input>
								<el-input style="width:50%;" v-if="!isshow" class="margin15" placeholder="请输入充电站地址"
									v-model="shipAddress" autosize @keydown.enter.native="selectDz"></el-input>
								<el-button v-if="!isshow" type="info" plain @click="selectDz()">获取经纬度</el-button>
							</div>
							<div class="content_item">
								<span style="display: inline-block;text-align: left;width: 200px;">充电站经度:</span>
								<el-input style="width:50%;" disabled class="margin15" placeholder="请输入充电站经度"
									v-model="store_lat" autosize></el-input>
								<!-- <el-input style="width:50%;" v-if="!isshow" class="margin15" placeholder="请输入充电站经度"
									v-model="store_lat" autosize></el-input> -->
							</div>
							<div class="content_item">
								<span style="display: inline-block;text-align: left;width: 200px;">充电站纬度:</span>
								<el-input style="width:50%;" disabled class="margin15" placeholder="请输入充电站纬度"
									v-model="store_lng" autosize></el-input>
								<!-- <el-input style="width:50%;" v-if="!isshow" class="margin15" placeholder="请输入充电站纬度"
									v-model="store_lng" autosize></el-input> -->
							</div>
							<div class="content_item">
								<span style="display: inline-block;text-align: left;width: 200px;">开店时间：</span>
								<el-time-select v-if="isshow" disabled v-model="openTime" :picker-options="{
							  start: '00:00',
							  step: '00:01',
							  end: '23:59'
							}" placeholder="选择开店时间" style="width:50%;">
								</el-time-select>
								<el-time-select v-if="!isshow" v-model="openTime" :picker-options="{
								  start: '00:00',
								  step: '00:01',
								  end: '23:59'
								}" placeholder="选择开店时间" style="width:50%;">
								</el-time-select>
							</div>
							<div class="content_item">
								<span style="display: inline-block;text-align: left;width: 200px;">闭店时间：</span>
								<el-time-select v-if="isshow" disabled v-model="closeTime" :picker-options="{
							  start: '00:00',
							  step: '00:01',
							  end: '23:59'
							}" placeholder="选择闭店时间" style="width:50%;">
								</el-time-select>
								<el-time-select v-if="!isshow" v-model="closeTime" :picker-options="{
								  start: '00:00',
								  step: '00:01',
								  end: '23:59'
								}" placeholder="选择闭店时间" style="width:50%;">
								</el-time-select>
							</div>
							<div class="content_item">
								<span style="display: inline-block;text-align: left;width: 200px;">充电站状态:</span>
								<div class="margin15" style="margin-left: 200px;margin-top: -13px;">
									<el-radio-group v-if="isshow" disabled v-model="banstatus"
										@change="merchClick(banstatus)">
										<el-radio :label="1">正常</el-radio>
										<el-radio :label="2">打烊</el-radio>
										<el-radio :label="3">今日已满</el-radio>
									</el-radio-group>
								</div>
								<div class="margin15" style="margin-left: 200px;margin-top: -13px;">
									<el-radio-group v-if="!isshow" v-model="banstatus" @change="merchClick(banstatus)">
										<el-radio :label="1">正常</el-radio>
										<el-radio :label="2">打烊</el-radio>
										<el-radio :label="3">今日已满</el-radio>
									</el-radio-group>
								</div>
							</div>
							<!-- <div class="content_item">
								<span style="display: inline-block;text-align: left;width: 200px;">零钱操作权限:</span>
								<div class="margin15" style="margin-left: 200px;margin-top: -13px;">
									<el-radio-group v-if="isshow" disabled v-model="moneyScope">
										<el-radio :label="1">有加减</el-radio>
										<el-radio :label="2">有加</el-radio>
										<el-radio :label="3">有减</el-radio>
										<el-radio :label="4">没有</el-radio>
									</el-radio-group>
								</div>
								<div class="margin15" style="margin-left: 200px;margin-top: -13px;">
									<el-radio-group v-if="!isshow" v-model="moneyScope">
										<el-radio :label="1">有加减</el-radio>
										<el-radio :label="2">有加</el-radio>
										<el-radio :label="3">有减</el-radio>
										<el-radio :label="4">没有</el-radio>
									</el-radio-group>
								</div>
							</div>
							<div class="content_item">
								<span style="display: inline-block;text-align: left;width: 200px;">是否享受会员权益:</span>
								<div class="margin15" style="margin-left: 200px;margin-top: -13px;">
									<el-radio-group v-if="isshow" disabled v-model="isVipPrivilege">
										<el-radio :label="1">是</el-radio>
										<el-radio :label="2">否</el-radio>
									</el-radio-group>
								</div>
								<div class="margin15" style="margin-left: 200px;margin-top: -13px;">
									<el-radio-group v-if="!isshow" v-model="isVipPrivilege">
										<el-radio :label="1">是</el-radio>
										<el-radio :label="2">否</el-radio>
									</el-radio-group>
								</div>
							</div> -->
							<!-- <div class="content_item">
								<span style="display: inline-block;text-align: left;width: 200px;">是否开启人脸识别:</span>
								<div class="margin15" style="margin-left: 200px;margin-top: -13px;">
									<el-radio-group v-if="isshow" disabled v-model="isFaceRecognition">
										<el-radio :label="1">是</el-radio>
										<el-radio :label="2">否</el-radio>
									</el-radio-group>
								</div>
								<div class="margin15" style="margin-left: 200px;margin-top: -13px;">
									<el-radio-group v-if="!isshow" v-model="isFaceRecognition">
										<el-radio :label="1">是</el-radio>
										<el-radio :label="2">否</el-radio>
									</el-radio-group>
								</div>
							</div>
							<div class="content_item">
								<span style="display: inline-block;text-align: left;width: 200px;">门店是否支持点歌:</span>
								<div class="margin15" style="margin-left: 200px;margin-top: -13px;">
									<el-radio-group v-if="isshow" disabled v-model="isMusic">
										<el-radio :label="1">是</el-radio>
										<el-radio :label="2">否</el-radio>
									</el-radio-group>
								</div>
								<div class="margin15" style="margin-left: 200px;margin-top: -13px;">
									<el-radio-group v-if="!isshow" v-model="isMusic">
										<el-radio :label="1">是</el-radio>
										<el-radio :label="2">否</el-radio>
									</el-radio-group>
								</div>
							</div>

							<div class="content_item">
								<span style="display: inline-block;text-align: left;width: 200px;">是否开启预定:</span>
								<div class="margin15" style="margin-left: 200px;margin-top: -13px;">
									<el-radio-group v-if="isshow" disabled v-model="isReserve">
										<el-radio :label="1">是</el-radio>
										<el-radio :label="2">否</el-radio>
									</el-radio-group>
								</div>
								<div class="margin15" style="margin-left: 200px;margin-top: -13px;">
									<el-radio-group v-if="!isshow" v-model="isReserve">
										<el-radio :label="1">是</el-radio>
										<el-radio :label="2">否</el-radio>
									</el-radio-group>
								</div>
							</div>
							<div class="content_item">
								<span style="display: inline-block;text-align: left;width: 200px;">是否线下开台:</span>
								<div class="margin15" style="margin-left: 200px;margin-top: -13px;">
									<el-radio-group v-if="isshow" disabled v-model="isOpen">
										<el-radio :label="1">是</el-radio>
										<el-radio :label="2">否</el-radio>
									</el-radio-group>
								</div>
								<div class="margin15" style="margin-left: 200px;margin-top: -13px;">
									<el-radio-group v-if="!isshow" v-model="isOpen">
										<el-radio :label="1">是</el-radio>
										<el-radio :label="2">否</el-radio>
									</el-radio-group>
								</div>
							</div>
							
							<div class="content_item">
								<span style="display: inline-block;text-align: left;width: 200px;">是否开启爱心活动:</span>
								<div class="margin15" style="margin-left: 200px;margin-top: -13px;">
									<el-radio-group v-if="isshow" disabled v-model="isWelfare">
										<el-radio :label="1">是</el-radio>
										<el-radio :label="2">否</el-radio>
									</el-radio-group>
								</div>
								<div class="margin15" style="margin-left: 200px;margin-top: -13px;">
									<el-radio-group v-if="!isshow" v-model="isWelfare">
										<el-radio :label="1">是</el-radio>
										<el-radio :label="2">否</el-radio>
									</el-radio-group>
								</div>
							</div>
							<div class="content_item" style="margin-bottom: 10px;">
								<span style="display: inline-block;text-align: left;width: 200px;">爱心活动介绍:</span>
								<el-input style="width:50%;" v-if="isshow" disabled class="margin15"
									placeholder="请输入爱心活动介绍" v-model="welfareContent" autosize></el-input>
								<el-input style="width:50%;" v-if="!isshow" class="margin15" placeholder="请输入爱心活动介绍"
									v-model="welfareContent" autosize></el-input>
							</div>
							<div class="shop_content" style="max-width: 800px;margin: auto;">
								<div class="content_item">
									<div style="display: flex;align-items: center;margin: 2% 0;">
										<span
											style="display: inline-block;text-align: left;width: 200px;">爱心活动背景图：</span>
										<div v-if="isshow"
											style="width:148px;height:148px;background-color: #fbfdff; border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
											<img v-if="welfareImg" :src="welfareImg" class="avatar"
												style="width: 148px;height: 148px;border-radius:6px;" />
										</div>
										<div v-if="!isshow"
											style="width:148px;height:148px;background-color: #fbfdff; border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;position: relative;">
											<el-upload class="avatar-uploader" v-model="welfareImg"
												:action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
												:on-success="handleAvatarSuccess4">
												<img v-if="welfareImg" :src="welfareImg" class="avatar"
													style="width: 148px;height: 148px;border-radius:6px;" />
												<div @click="selectDz3()" class="divhove" v-if="welfareImg">
													<i class="el-icon-delete"
														style="margin: 0;font-size: 18px;color: #fff;"></i>
												</div>
												<i v-else class="el-icon-plus avatar-uploader-icon"
													style="font-size: 28px;color: #8c939d"></i>
											</el-upload>

										</div>
									</div>
								</div>
							</div>
							<div class="content_item" style="margin-bottom: 10px;">
								<span style="display: inline-block;text-align: left;width: 200px;">幸运用户序号:</span>
								<el-input style="width:50%;" v-if="isshow" disabled class="margin15"
									placeholder="请输入幸运用户序号" v-model="luckySort" autosize></el-input>
								<el-input style="width:50%;" v-if="!isshow" class="margin15" placeholder="请输入幸运用户序号"
									v-model="luckySort" autosize></el-input>
							</div> -->
							<!-- <div class="content_item" style="margin-bottom: 10px;">
								<span style="display: inline-block;text-align: left;width: 200px;">幸运用户奖励优惠券:</span>
								<div v-if="isshow" style="display: inline-block;width: auto;">
									<span v-if="luckyCouponList" style="display: inline-block;width: auto;">
										<el-tag v-for="(item,index) of luckyCouponList" :key="index"
											type="info">{{item.couponName}}</el-tag>
									</span>
								</div>
								<div v-if="!isshow" style="display: inline-block;width: auto;">
									<span v-if="luckyCouponList" style="display: inline-block;width: auto;">
										<el-tag v-for="(item,index) of luckyCouponList" :key="index" type="info"
											closable @close="yhqClose(index)">{{item.couponName}}</el-tag>
									</span>
									<el-button size="mini" plain type="warning" @click="qingkongYhq()">清空</el-button>
									<el-button icon="el-icon-plus" size="mini" plain circle type="primary"
										@click="userselectYhq()"></el-button>
								</div>
							</div>
							<div class="content_item" style="margin-bottom: 10px;">
								<span style="display: inline-block;text-align: left;width: 200px;">幸运用户播报内容:</span>
								<el-input style="width:50%;" v-if="isshow" disabled class="margin15"
									placeholder="请输入幸运用户播报内容" v-model="luckyContent" autosize></el-input>
								<el-input style="width:50%;" v-if="!isshow" class="margin15" placeholder="请输入幸运用户播报内容"
									v-model="luckyContent" autosize></el-input>
							</div> -->
							<!-- <div class="content_item">
								<span style="display: inline-block;text-align: left;width: 200px;">是否允许操作退款:</span>
								<div class="margin15" style="margin-left: 200px;margin-top: -13px;">
									<el-radio-group v-if="isshow" disabled v-model="isRefund">
										<el-radio :label="1">是</el-radio>
										<el-radio :label="2">否</el-radio>
									</el-radio-group>
								</div>
								<div class="margin15" style="margin-left: 200px;margin-top: -13px;">
									<el-radio-group v-if="!isshow" v-model="isRefund">
										<el-radio :label="1">是</el-radio>
										<el-radio :label="2">否</el-radio>
									</el-radio-group>
								</div>
							</div>
							<div class="content_item">
								<span style="display: inline-block;text-align: left;width: 200px;">是否开启微信支付:</span>
								<div class="margin15" style="margin-left: 200px;margin-top: -13px;">
									<el-radio-group v-if="isshow" disabled v-model="isWxPay">
										<el-radio :label="1">是</el-radio>
										<el-radio :label="2">否</el-radio>
									</el-radio-group>
								</div>
								<div class="margin15" style="margin-left: 200px;margin-top: -13px;">
									<el-radio-group v-if="!isshow" v-model="isWxPay">
										<el-radio :label="1">是</el-radio>
										<el-radio :label="2">否</el-radio>
									</el-radio-group>
								</div>
							</div> -->
							<!-- <div class="content_item">
								<span style="display: inline-block;text-align: left;width: 200px;">扫码器是否自动开台:</span>
								<div class="margin15" style="margin-left: 200px;margin-top: -13px;">
									<el-radio-group v-if="isshow" disabled v-model="isQrCode">
										<el-radio :label="1">是</el-radio>
										<el-radio :label="2">否</el-radio>
									</el-radio-group>
								</div>
								<div class="margin15" style="margin-left: 200px;margin-top: -13px;">
									<el-radio-group v-if="!isshow" v-model="isQrCode">
										<el-radio :label="1">是</el-radio>
										<el-radio :label="2">否</el-radio>
									</el-radio-group>
								</div>
							</div> -->
						</div>
					</div>
				</div>
			</el-tab-pane>
			 <el-tab-pane label="价格配置" name="yuangong">
				<div class="nf-table-card">
				 <div class="merch-income-page__toolbar">
				 	<el-button style='margin-left:15px;' size="mini" type="primary" icon="document"
				 		@click="timeclick()">
				 		刷新
				 	</el-button>
				 	<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="timeadd()">
				 		添加
				 	</el-button>
				 </div>
				 <el-table v-loading="tableDataLoading4" :data="userIds.records" class="merch-income-page__table">
				 	<el-table-column prop="goodsTimeId" fixed label="编号" width="80">
				 	</el-table-column>
				 	<el-table-column prop="startTime" label="开始时间">
				 	</el-table-column>
				 	<el-table-column prop="endTime" label="结束时间">
				 	</el-table-column>
				 	
				 	<el-table-column prop="minutePrice" label="分钟价(元)" v-if="tearoomType==1">
				 	</el-table-column>
					<el-table-column prop="memberMinutePrice" label="会员分钟价(元)" v-if="tearoomType==1">
					</el-table-column>
					
					<!-- <el-table-column prop="hourPrice" label="小时价(元)" v-if="tearoomType==2">
					</el-table-column>
					<el-table-column prop="memberHourPrice" label="会员小时价(元)" v-if="tearoomType==2">
					</el-table-column> -->
					
					<el-table-column prop="electricityPrice" label="用户每度电价格" v-if="tearoomType==2">
					</el-table-column>
					<el-table-column prop="memberElectricityPrice" label="会员每度电价格" v-if="tearoomType==2">
					</el-table-column>
				 	<el-table-column prop="createTime" label="创建时间">
				 	</el-table-column>
				 	<el-table-column label="操作" fixed='right' width="160">
				 		<template slot-scope="scope">
				 			<el-button size="mini" type="primary" @click="timeadd(scope.row)">修改
				 			</el-button>
				 			<el-button size="mini" type="danger" @click="timedelete(scope.row)">删除
				 			</el-button>
				 		</template>
				 	</el-table-column>
				 </el-table>
				 <div class="merch-income-page__pagination">
				 	<el-pagination @size-change="handleSizeChange4" @current-change="handleCurrentChange4"
				 		:page-sizes="[5, 10, 15, 20]" :page-size="size1" :current-page="page1"
				 		layout="total,sizes, prev, pager, next,jumper" :total="userIds.total">
				 	</el-pagination>
				 </div>
				</div>
			</el-tab-pane>
			<!-- <el-tab-pane label="员工管理" name="yuangong">
				<el-button size="mini" plain type="primary" @click="userselectYg(2)">添加</el-button>
				<el-table :data="tableDataYg.list">
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
					<el-table-column prop="age" label="年龄">
					</el-table-column>
					<el-table-column prop="sex" label="性别">
						<template slot-scope="scope">
							<span>{{scope.row.sex==1 ? '男' : '女'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="sex" label="工作时间">
						<template slot-scope="scope">
							<span v-if="scope.row.startTime">{{scope.row.startTime !='' ? scope.row.startTime : '-'}} -
							</span>
							<span v-if="scope.row.endTime">{{scope.row.endTime !='' ? scope.row.endTime : ''}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="tearoomUserStatus" label="状态">
						<template slot-scope="scope">
							<span style="color: #13ce66;cursor: pointer;"
								v-if="scope.row.tearoomUserStatus == 1 ">正常</span>
							<span style="color: #5cc477;cursor: pointer;"
								v-else-if="scope.row.tearoomUserStatus == 2 ">上班</span>
							<span style="color: #ff4949;cursor: pointer;"
								v-else-if="scope.row.tearoomUserStatus == 3 ">下班</span>
							<span style="color: #13ce66;cursor: pointer;" v-else>正常</span>

						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间">
					</el-table-column>
					<el-table-column label="操作" width="180">
						<template slot-scope="scope">
							<el-button style="margin: 5px;" size="mini" type="primary" @click="ygClick(scope.row)">设置
							</el-button>
							<el-button style="margin: 5px;" size="mini" type="warning" plain @click="quanxianBtn(scope.row)">查看权限
							</el-button>
							<el-button style="margin: 5px;" size="mini" type="warning" @click="deletesYg(scope.row)">删除
							</el-button>
							<el-button style="margin: 5px;" size="mini" type="primary" @click="ktClick(scope.row)">开台统计
							</el-button>
							<el-button style="margin: 5px;" size="mini" type="warning" plain @click="shangbanBtn(scope.row)">上班记录
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChangeYg" @current-change="handleCurrentChangeYg"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="size1" :current-page="page1"
						layout="total,sizes, prev, pager, next" :total="tableDataYg.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane> -->
			<el-tab-pane label="充电统计" name="secondKt">
				<el-select v-model="way" style="width:150px;margin-left: 10px;" @change="animeDatKt">
					<el-option v-for="(item,index) in ways" :key="index" :label="item.label" :value="item.value">
					</el-option>
				</el-select>&nbsp;&nbsp;&nbsp;
				<el-date-picker style="width: 160px;margin-left: 10px;" v-model="info.stockDate" align="right"
					type="datetime" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间"
					@change="animeDatKt">
				</el-date-picker>
				<el-row>
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">总收益</div>
								<div class="text_color">
									<span>{{totalRevenueKtTj.sumMoney?totalRevenueKtTj.sumMoney:0 | numFilter}}</span>元
								</div>
							</div>
						</div>
					</el-col>
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color"> 总订单 </div>
								<div class="text_color">
									<span>{{totalRevenueKtTj.sumCount?totalRevenueKtTj.sumCount:0}}</span>单
								</div>
							</div>
						</div>
					</el-col>
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">微信收益</div>
								<div class="text_color">
									<span>{{totalRevenueKtTj.wxMoney?totalRevenueKtTj.wxMoney:0 | numFilter}}</span>元
								</div>
							</div>
						</div>
					</el-col>
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color"> 微信订单 </div>
								<div class="text_color">
									<span>{{totalRevenueKtTj.wxCount?totalRevenueKtTj.wxCount:0}}</span>单
								</div>
							</div>
						</div>
					</el-col>
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">现金收益</div>
								<div class="text_color">
									<span>{{totalRevenueKtTj.xjMoney?totalRevenueKtTj.xjMoney:0 | numFilter}}</span>元
								</div>
							</div>
						</div>
					</el-col>
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color"> 现金订单 </div>
								<div class="text_color">
									<span>{{totalRevenueKtTj.xjCount?totalRevenueKtTj.xjCount:0}}</span>单
								</div>
							</div>
						</div>
					</el-col>
					<!-- <el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">门店会员收益</div>
								<div class="text_color">
									<span>{{totalRevenueKtTj.mdMoney?totalRevenueKtTj.mdMoney:0 | numFilter}}</span>元
								</div>
							</div>
						</div>
					</el-col> -->
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color"> 门店会员订单 </div>
								<div class="text_color">
									<span>{{totalRevenueKtTj.mdCount?totalRevenueKtTj.mdCount:0}}</span>单
								</div>
							</div>
						</div>
					</el-col>
					<!-- <el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">美团核销收益</div>
								<div class="text_color">
									<span>{{totalRevenueKtTj.mtMoney?totalRevenueKtTj.mtMoney:0 | numFilter}}</span>元
								</div>
							</div>
						</div>
					</el-col>
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color"> 美团核销订单 </div>
								<div class="text_color">
									<span>{{totalRevenueKtTj.mtCount?totalRevenueKtTj.mtCount:0}}</span>单
								</div>
							</div>
						</div>
					</el-col>
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">快手核销收益</div>
								<div class="text_color">
									<span>{{totalRevenueKtTj.ksMoney?totalRevenueKtTj.ksMoney:0 | numFilter}}</span>元
								</div>
							</div>
						</div>
					</el-col>
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color"> 快手核销订单 </div>
								<div class="text_color">
									<span>{{totalRevenueKtTj.ksCount?totalRevenueKtTj.ksCount:0}}</span>单
								</div>
							</div>
						</div>
					</el-col>
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">抖音核销收益</div>
								<div class="text_color">
									<span>{{totalRevenueKtTj.dyMoney?totalRevenueKtTj.dyMoney:0 | numFilter}}</span>元
								</div>
							</div>
						</div>
					</el-col>
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color"> 抖音核销订单 </div>
								<div class="text_color">
									<span>{{totalRevenueKtTj.dyCount?totalRevenueKtTj.dyCount:0}}</span>单
								</div>
							</div>
						</div>
					</el-col> -->
				</el-row>

			</el-tab-pane>
			<el-tab-pane label="充电站收入" name="second">
				<el-select v-model="way" style="width:150px;margin-left: 10px;" @change="animeDat">
					<el-option v-for="(item,index) in ways" :key="index" :label="item.label" :value="item.value">
					</el-option>
				</el-select>&nbsp;&nbsp;&nbsp;
				<el-date-picker style="width: 160px;margin-left: 10px;" v-model="info.stockDate" align="right"
					type="datetime" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间"
					@change="timeDate">
				</el-date-picker>
				<!-- </div> -->
				<el-row>
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">总收入</div>
								<div class="text_color"><span>{{income.sumMoney?income.sumMoney:0 | numFilter}}</span>元
								</div>
							</div>
						</div>
					</el-col>
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">当前收入</div>
								<div class="text_color"><span>{{income.money?income.money:0 | numFilter}}</span>元
								</div>
							</div>
						</div>
					</el-col>
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">微信收入</div>
								<div class="text_color">
									<span>{{income.wxMoney?income.wxMoney:0 | numFilter}}</span>元
								</div>
							</div>
						</div>
					</el-col>
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">支付宝收入</div>
								<div class="text_color">
									<span>{{income.zfbMoney?income.zfbMoney:0 | numFilter}}</span>元
								</div>
							</div>
						</div>
					</el-col>
				</el-row>
				<el-row>
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">零钱收入</div>
								<div class="text_color">
									<span>{{income.lqMoney?income.lqMoney:0 | numFilter}}</span>元
								</div>
							</div>
						</div>
					</el-col>
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">订单年成交量</div>
								<div class="text_color"><span>{{income.yearCount?income.yearCount:0}}</span>单
								</div>
							</div>
						</div>
					</el-col>
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">订单月成交量</div>
								<div class="text_color"><span>{{income.monthCount?income.monthCount:0}}</span>单
								</div>
							</div>
						</div>
					</el-col>
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">订单日成交量</div>
								<div class="text_color"><span>{{income.dayCount?income.dayCount:0}}</span>单</div>
							</div>
						</div>
					</el-col>
				</el-row>
				<!-- 充电站收入明细 -->
				<div>
					<el-table :data="tableDataqqq.list">
						<el-table-column prop="goodsId" label="编号" width="100">
						</el-table-column>

						<el-table-column prop="goodsName" label="充电桩名称">
						</el-table-column>
						<el-table-column fixed="right" prop="status" label="充电桩状态">
							<template slot-scope="scope">
								<span style="color: #5cc477;cursor: pointer;" v-if="scope.row.status === 1 ">上架</span>
								<span style="color: #5cc477;cursor: pointer;" v-if="scope.row.status === 2 ">下架</span>
							</template>
						</el-table-column>
						<el-table-column prop="sumMoney" label="充电桩总收益">
							<template slot-scope="scope">
								<span>{{scope.row.sumMoney?scope.row.sumMoney:0 }}</span>
							</template>
						</el-table-column>
					</el-table>
					<div style="text-align: center;margin-top: 10px;">
						<el-pagination @size-change="handleSizeChangeYe1" @current-change="handleCurrentChangeYe1"
							:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
							layout="total,sizes, prev, pager, next" :total="tableDataqqq.totalCount">
						</el-pagination>
					</div>
				</div>
			</el-tab-pane>

			<el-tab-pane label="充电站统计" name="secondQ">
				<div style="font-size: 18px;margin: 10px 20px;font-weight: 600;color: #999;">充电站收益</div>
				<el-row>
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">总收益</div>
								<div class="text_color">
									<span>{{totalRevenue.sumMoney?totalRevenue.sumMoney:0 | numFilter}}</span>元
								</div>
							</div>
						</div>
					</el-col>
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color"> 总订单 </div>
								<div class="text_color"><span>{{totalRevenue.sumCount?totalRevenue.sumCount:0}}</span>单
								</div>
							</div>
						</div>
					</el-col>
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">充电桩数量</div>
								<div class="text_color">
									<span>{{totalRevenue.goodsCount?totalRevenue.goodsCount:0 }}</span>个
								</div>
							</div>
						</div>
					</el-col>
				</el-row>
				<div style="font-size: 18px;margin: 10px 20px;font-weight: 600;color: #999;">订单数量</div>
				<el-row>
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">待支付订单</div>
								<div class="text_color"><span>{{orders.dzfCount?orders.dzfCount:0}}</span>单
								</div>
							</div>
						</div>
					</el-col>
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">待使用订单</div>
								<div class="text_color"><span>{{orders.dsyCount?orders.dsyCount:0}}</span>单
								</div>
							</div>
						</div>
					</el-col>
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">使用中订单</div>
								<div class="text_color"><span>{{orders.syzCount?orders.syzCount:0}}</span>单
								</div>
							</div>
						</div>
					</el-col>
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">已完成订单</div>
								<div class="text_color"><span>{{orders.ywcCount?orders.ywcCount:0}}</span>单
								</div>
							</div>
						</div>
					</el-col>
				</el-row>
				<div style="font-size: 18px;margin: 10px 20px;font-weight: 600;color: #999;">充电站钱包</div>
				<el-row>
					<el-col :span="6" class="cards" v-if="userId">
						<div class="box" style="height: auto;">
							<div class="box_num">
								<div class="box_color">钱包</div>
								<div class="text_color">
									<span>{{tableDataQb.shopMoney?tableDataQb.shopMoney:0 | numFilter}}</span>元
									<br />
									<el-button size="mini" :disabled="!isAuth('userList:updatejf')"
										style="color: #5cc477;background: #fff;border: none;margin: 10px 0;"
										@click="rechargenone(1,0)">修改金额
									</el-button>
									<!-- <el-button size="mini" :disabled="!isAuth('userList:updatejs')"
										style="color: #ff4949;background: #fff;border: none;margin: 10px 0;"
										@click="rechargenone2()">结算金额
									</el-button> -->
								</div>
							</div>
						</div>
					</el-col>
				</el-row>
				<div style="font-size: 18px;margin: 10px 20px;font-weight: 600;color: #999;">钱包明细</div>
				<div>
					<el-table :data="tableDataQbL.list">
						<el-table-column prop="id" label="编号">
						</el-table-column>
						<el-table-column prop="title" label="标题">
						</el-table-column>
						<el-table-column prop="content" label="内容">
						</el-table-column>
						<el-table-column prop="money" label="金额">
							<template slot-scope="scope">
								<span style="color:limegreen;cursor: pointer;" v-if="scope.row.type == 1 ">+
									{{scope.row.money}}</span>
								<span style="color: #ff4949;cursor: pointer;" v-if="scope.row.type == 2 ">-
									{{scope.row.money}}</span>
							</template>
						</el-table-column>
						<el-table-column prop="createTime" label="创建时间">
						</el-table-column>

					</el-table>
					<div style="text-align: center;margin-top: 10px;">
						<el-pagination @size-change="handleSizeChangeYe2" @current-change="handleCurrentChangeYe2"
							:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
							layout="total,sizes, prev, pager, next" :total="tableDataQbL.totalCount">
						</el-pagination>
					</div>
				</div>
			</el-tab-pane>
			<el-tab-pane label="充电桩管理" name="shopQp" v-if="tearoomType==1">
				<div style="margin:2% 0;display: inline-block;">
					<span>充电桩名称：</span>
					<el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入充电桩名称"
						v-model="title">
					</el-input>&nbsp;&nbsp;&nbsp;&nbsp;
					<span>充电桩状态：</span>
					<el-select v-model="status" placeholder="请选择充电桩状态" style="width:150px;" @change="select(status)">
						<el-option v-for="item in statusmain" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>&nbsp;&nbsp;&nbsp;&nbsp;
				</div>
				<div style="display: inline-block;">
					<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="select">查询
					</el-button>
					<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="cleans">重置
					</el-button>
					<el-button style='margin-left:15px;' :disabled="!isAuth('merchantList:add')" size="mini"
						type="primary" plain icon="document" @click="addShop(2)">添加</el-button>
					<el-button style='margin:0 0 20px 20px;' size="mini" type="primary" icon="document"
						@click="transferClcik(2)" :disabled="checkBoxData.length <= 0 ">
						批量下载二维码
					</el-button>
					<el-button style='margin:0 0 20px 20px;' size="mini" type="warning" icon="document"
						@click="jiageClcik()" :disabled="checkBoxData.length <= 0 ">
						批量修改价格
					</el-button>
				</div>
				<el-table v-loading="tableDataLoading" :data="shopData.list" @selection-change="changeFun">
					<el-table-column type="selection">
					</el-table-column>
					<el-table-column fixed prop="goodsId" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="goodsCategory" label="充电桩类型" width="80" fixed="right" v-if="tearoomType==2">
						<template slot-scope="scope">
							<span v-if="scope.row.goodsCategory == 1" style="color: #13ce66;">快充</span>
							<span v-else style="color: #ff4949;">慢充</span>
						</template>
					</el-table-column>
					<!-- <el-table-column fixed prop="goodsCoverImage" label="使用前图片">
						<template slot-scope="scope">
							<img :src="scope.row.goodsCoverImage" alt="" width="60" height="60">
						</template>
					</el-table-column>
					<el-table-column fixed prop="goodsTitleImage" label="使用后图片">
						<template slot-scope="scope">
							<img :src="scope.row.goodsTitleImage" alt="" width="60" height="60">
						</template>
					</el-table-column> -->
					<el-table-column prop="goodsName" label="充电桩名称" width="200">
					</el-table-column>
					<!-- <el-table-column prop="goodsType" label="充电桩信息" width="200">
					</el-table-column>
					<el-table-column fixed prop="iconImg" label="图标">
						<template slot-scope="scope">
							<img :src="scope.row.iconImg" alt="" width="60" height="60">
						</template>
					</el-table-column> -->
					<!-- <el-table-column prop="hourPrice" label="小时价">
						<template slot-scope="scope">
							<span
								style="color: #f56c6c;">{{scope.row.hourPrice?scope.row.hourPrice:0 | numFilter}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="minutePrice" label="分钟价">
						<template slot-scope="scope">
							<span
								style="color: #f56c6c;">{{scope.row.minutePrice?scope.row.minutePrice:0 | numFilter}}</span>
						</template>
					</el-table-column> -->
					<!-- <el-table-column prop="cashPledge" label="押金">
						<template slot-scope="scope">
							<span
								style="color: #f56c6c;">{{scope.row.cashPledge?scope.row.cashPledge:0 | numFilter}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="subscription" label="预约价格">
						<template slot-scope="scope">
							<span
								style="color: #f56c6c;">{{scope.row.subscription?scope.row.subscription:0 | numFilter}}</span>
						</template>
					</el-table-column> -->
					<!-- <el-table-column prop="yyToken" label="语音播报token" width="200">
					</el-table-column>
					<el-table-column prop="yyId" label="语音播报ID" width="200">
					</el-table-column>
					<el-table-column prop="goodsTips" label="推流链接" width="200">
					</el-table-column>
					<el-table-column prop="cameraId" label="摄像头配置">
					</el-table-column> -->
					<el-table-column prop="sales" label="充电桩销量">
						<template slot-scope="scope">
							<span>{{scope.row.sales ? scope.row.sales : '0'}}</span>
						</template>
					</el-table-column>
					<el-table-column label="充电桩二维码" width="100">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="erweima(scope.row)" style="margin: 5px;">查看
							</el-button>
						</template>
					</el-table-column>
					<!-- <el-table-column label="点餐二维码" width="100">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="erweimaDc(scope.row)" style="margin: 5px;">查看
							</el-button>
						</template>
					</el-table-column> -->
					<el-table-column prop="sort" label="排序" width="200">
						<template slot-scope="scope">
							<div>{{scope.row.sort}}
								<el-input type="text" @keydown.enter.native="zhiition(scope.row)"
									v-model="scope.row.sort" clearable placeholder="输入排序,例如1"
									style="width: 120px;font-size:14px;">
									<i slot="suffix" class="el-input__icon el-icon-search"
										@click="zhiition(scope.row)"></i>
								</el-input>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160">
					</el-table-column>
					<!-- <el-table-column prop="isOpen" label="是否线下开台" width="80" fixed="right">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.isOpen" @change="changeTg(scope.row)" :active-value="1"
								:disabled="!isAuth('merchantList:update')" :inactive-value="2" active-color="#13ce66"
								inactive-color="#ff4949">
							</el-switch>
							<span v-if="scope.row.isOpen == 1" style="color: #13ce66;">是</span>
							<span v-else style="color: #ff4949;">否</span>
						</template>
					</el-table-column> -->
					<el-table-column fixed="right" prop="status" label="充电桩状态">
						<template slot-scope="scope">
							<span style="color: #13ce66;cursor: pointer;" v-if="scope.row.status == 1 ">上架</span>
							<span style="color: #ff4949;cursor: pointer;" v-else-if="scope.row.status == 2 ">暂停使用</span>
							<span style="color: #5cc477;cursor: pointer;" v-else-if="scope.row.status == 3 ">清洁中</span>
							<span style="color: orange;cursor: pointer;" v-else-if="scope.row.status == 4 ">维护中</span>
							<span style="color: #999;cursor: pointer;" v-else-if="scope.row.status == 5 ">已打烊</span>
							<el-button size="mini" type="primary" plain :disabled="!isAuth('merchantList:update')"
								@click="UpdateStatus(scope.row)" style="margin-bottom: 5px;">修改</el-button>
						</template>
					</el-table-column>
					<el-table-column fixed="right" label="操作" width="160" style="line-height: 33px;">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="shopDels(scope.row)"
								style="margin: 5px;">数据详情</el-button>
							<!-- <el-button size="mini" type="info" @click="discussTime(scope.row)" plain
								style="margin: 5px;">特殊时间</el-button> -->
							<el-button size="mini" type="primary" :disabled="!isAuth('merchantList:update')"  plain
								@click="shopUpdates(scope.$index, scope.row)" style="margin: 5px;">修改</el-button>
							<el-button size="mini" type="danger" @click="deleteq(scope.row)"
								:disabled="!isAuth('merchantList:delete')" style="margin: 5px;">删除</el-button>
							<!-- <el-button size="mini" type="warning" @click="kaideng(scope.row,1)"
								:disabled="!isAuth('merchantList:updateDeng')" style="margin: 5px;">开灯</el-button>
							<el-button size="mini" type="warning" plain @click="kaideng(scope.row,0)"
								:disabled="!isAuth('merchantList:updateDeng')" style="margin: 5px;">关灯</el-button>
							<el-button size="mini" type="danger" plain @click="liveBtn(scope.row)"
								style="margin: 5px;">直播</el-button> -->
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="shopData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane>
			<el-tab-pane label="快充充电桩管理" name="shop" v-if="tearoomType==2">
				<div style="margin:2% 0;display: inline-block;">
					<span>充电桩名称：</span>
					<el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入充电桩名称"
						v-model="title">
					</el-input>&nbsp;&nbsp;&nbsp;&nbsp;
					<span>充电桩状态：</span>
					<el-select v-model="status" placeholder="请选择充电桩状态" style="width:150px;" @change="select(status)">
						<el-option v-for="item in statusmain" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>&nbsp;&nbsp;&nbsp;&nbsp;
				</div>
				<div style="display: inline-block;">
					<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="select">查询
					</el-button>
					<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="cleans">重置
					</el-button>
					<el-button style='margin-left:15px;' :disabled="!isAuth('merchantList:add')" size="mini"
						type="primary" plain icon="document" @click="addShop(1)">添加</el-button>
					<el-button style='margin:0 0 20px 20px;' size="mini" type="primary" icon="document"
						@click="transferClcik(1)" :disabled="checkBoxData.length <= 0 ">
						批量下载二维码
					</el-button>
					<el-button style='margin:0 0 20px 20px;' size="mini" type="warning" icon="document"
						@click="jiageClcik()" :disabled="checkBoxData.length <= 0 ">
						批量修改价格
					</el-button>
				</div>
				<el-table v-loading="tableDataLoading" :data="shopData.list" @selection-change="changeFun">
					<el-table-column type="selection">
					</el-table-column>
					<el-table-column fixed prop="goodsId" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="goodsCategory" label="充电桩类型" width="80" fixed="right" v-if="tearoomType==2">
						<template slot-scope="scope">
							<span v-if="scope.row.goodsCategory == 1" style="color: #13ce66;">快充</span>
							<span v-else style="color: #ff4949;">慢充</span>
						</template>
					</el-table-column>
					<el-table-column prop="goodsName" label="充电桩名称" width="200">
					</el-table-column>
					<el-table-column prop="sales" label="充电桩销量">
						<template slot-scope="scope">
							<span>{{scope.row.sales ? scope.row.sales : '0'}}</span>
						</template>
					</el-table-column>
					<el-table-column label="充电桩二维码" width="100">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="erweima(scope.row)" style="margin: 5px;">查看
							</el-button>
						</template>
					</el-table-column>
					<el-table-column prop="sort" label="排序" width="200">
						<template slot-scope="scope">
							<div>{{scope.row.sort}}
								<el-input type="text" @keydown.enter.native="zhiition(scope.row)"
									v-model="scope.row.sort" clearable placeholder="输入排序,例如1"
									style="width: 120px;font-size:14px;">
									<i slot="suffix" class="el-input__icon el-icon-search"
										@click="zhiition(scope.row)"></i>
								</el-input>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160">
					</el-table-column>
					<el-table-column fixed="right" prop="status" label="充电桩状态">
						<template slot-scope="scope">
							<span style="color: #13ce66;cursor: pointer;" v-if="scope.row.status == 1 ">上架</span>
							<span style="color: #ff4949;cursor: pointer;" v-else-if="scope.row.status == 2 ">暂停使用</span>
							<span style="color: #5cc477;cursor: pointer;" v-else-if="scope.row.status == 3 ">清洁中</span>
							<span style="color: orange;cursor: pointer;" v-else-if="scope.row.status == 4 ">维护中</span>
							<span style="color: #999;cursor: pointer;" v-else-if="scope.row.status == 5 ">已打烊</span>
							<el-button size="mini" type="primary" plain :disabled="!isAuth('merchantList:update')"
								@click="UpdateStatus(scope.row)" style="margin-bottom: 5px;">修改</el-button>
						</template>
					</el-table-column>
					<el-table-column fixed="right" label="操作" width="160" style="line-height: 33px;">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="shopDels(scope.row)"
								style="margin: 5px;">数据详情</el-button>
							<el-button size="mini" type="primary" :disabled="!isAuth('merchantList:update')"  plain
								@click="shopUpdates(scope.$index, scope.row)" style="margin: 5px;">修改</el-button>
							<el-button size="mini" type="danger" @click="deleteq(scope.row)"
								:disabled="!isAuth('merchantList:delete')" style="margin: 5px;">删除</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="shopData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane>
			<el-tab-pane label="慢充充电桩管理" name="shopQp" v-if="tearoomType==2">
				<div style="margin:2% 0;display: inline-block;">
					<span>充电桩名称：</span>
					<el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入充电桩名称"
						v-model="title">
					</el-input>&nbsp;&nbsp;&nbsp;&nbsp;
					<span>充电桩状态：</span>
					<el-select v-model="status" placeholder="请选择充电桩状态" style="width:150px;" @change="select(status)">
						<el-option v-for="item in statusmain" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>&nbsp;&nbsp;&nbsp;&nbsp;
				</div>
				<div style="display: inline-block;">
					<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="select">查询
					</el-button>
					<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="cleans">重置
					</el-button>
					<el-button style='margin-left:15px;' :disabled="!isAuth('merchantList:add')" size="mini"
						type="primary" plain icon="document" @click="addShop(2)">添加</el-button>
					<el-button style='margin:0 0 20px 20px;' size="mini" type="primary" icon="document"
						@click="transferClcik(2)" :disabled="checkBoxData.length <= 0 ">
						批量下载二维码
					</el-button>
					<el-button style='margin:0 0 20px 20px;' size="mini" type="warning" icon="document"
						@click="jiageClcik()" :disabled="checkBoxData.length <= 0 ">
						批量修改价格
					</el-button>
				</div>
				<el-table v-loading="tableDataLoading" :data="shopData.list" @selection-change="changeFun">
					<el-table-column type="selection">
					</el-table-column>
					<el-table-column fixed prop="goodsId" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="goodsCategory" label="充电桩类型" width="80" fixed="right" v-if="tearoomType==2">
						<template slot-scope="scope">
							<span v-if="scope.row.goodsCategory == 1" style="color: #13ce66;">快充</span>
							<span v-else style="color: #ff4949;">慢充</span>
						</template>
					</el-table-column>
					<el-table-column prop="goodsName" label="充电桩名称" width="200">
					</el-table-column>
					<el-table-column prop="sales" label="充电桩销量">
						<template slot-scope="scope">
							<span>{{scope.row.sales ? scope.row.sales : '0'}}</span>
						</template>
					</el-table-column>
					<el-table-column label="充电桩二维码" width="100">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="erweima(scope.row)" style="margin: 5px;">查看
							</el-button>
						</template>
					</el-table-column>
					<el-table-column prop="sort" label="排序" width="200">
						<template slot-scope="scope">
							<div>{{scope.row.sort}}
								<el-input type="text" @keydown.enter.native="zhiition(scope.row)"
									v-model="scope.row.sort" clearable placeholder="输入排序,例如1"
									style="width: 120px;font-size:14px;">
									<i slot="suffix" class="el-input__icon el-icon-search"
										@click="zhiition(scope.row)"></i>
								</el-input>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160">
					</el-table-column>
					<el-table-column fixed="right" prop="status" label="充电桩状态">
						<template slot-scope="scope">
							<span style="color: #13ce66;cursor: pointer;" v-if="scope.row.status == 1 ">上架</span>
							<span style="color: #ff4949;cursor: pointer;" v-else-if="scope.row.status == 2 ">暂停使用</span>
							<span style="color: #5cc477;cursor: pointer;" v-else-if="scope.row.status == 3 ">清洁中</span>
							<span style="color: orange;cursor: pointer;" v-else-if="scope.row.status == 4 ">维护中</span>
							<span style="color: #999;cursor: pointer;" v-else-if="scope.row.status == 5 ">已打烊</span>
							<el-button size="mini" type="primary" plain :disabled="!isAuth('merchantList:update')"
								@click="UpdateStatus(scope.row)" style="margin-bottom: 5px;">修改</el-button>
						</template>
					</el-table-column>
					<el-table-column fixed="right" label="操作" width="160" style="line-height: 33px;">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="shopDels(scope.row)"
								style="margin: 5px;">数据详情</el-button>
							<el-button size="mini" type="primary" :disabled="!isAuth('merchantList:update')"  plain
								@click="shopUpdates(scope.$index, scope.row)" style="margin: 5px;">修改</el-button>
							<el-button size="mini" type="danger" @click="deleteq(scope.row)"
								:disabled="!isAuth('merchantList:delete')" style="margin: 5px;">删除</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="shopData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane>
			
			<el-tab-pane label="充电站订单" name="order">
				<div style="margin:5px;display: inline-block;">
					<span>开台类型：</span>
					<el-select v-model="isShopT" style="width:150px;margin-left: 10px;" @change="select1">
						<el-option v-for="(item,index) in categoryListP" :key="index" :label="item.label"
							:value="item.value">
						</el-option>
					</el-select>&nbsp;&nbsp;
				</div>
				<div style="margin:5px;display: inline-block;">
					<span>订单状态：</span>
					<el-select v-model="statusorder" placeholder="请选择订单状态" style="width:150px;"
						@change="select1(statusorder)">
						<el-option v-for="item in statusmain1" :key="item.value" :label="item.label"
							:value="item.value">
						</el-option>
					</el-select>&nbsp;&nbsp;
				</div>
				<div style="position: relative;display: inline-block;margin:5px;">
					<span>订单号：</span>
					<el-input style="width: 150px;" @keydown.enter.native="select1" clearable placeholder="请输入订单号"
						v-model="orderNum"></el-input>&nbsp;&nbsp;
				</div>
				<div style="position: relative;display: inline-block;margin:5px;">
					<span>预约人电话：</span>
					<el-input style="width: 200px;" @keydown.enter.native="select1" placeholder="请输入预约人电话"
						v-model="orderType"></el-input>&nbsp;&nbsp;
				</div>
				<div style="position: relative;display: inline-block;margin:5px;">
					<span>预约人姓名：</span>
					<el-input style="width: 200px;" @keydown.enter.native="select1" placeholder="请输入预约人姓名"
						v-model="userName"></el-input>&nbsp;&nbsp;
				</div>
				<div style="position: relative;display: inline-block;margin:5px;">
					<span>充电站名称：</span>
					<el-input style="width: 200px;" @keydown.enter.native="select1" placeholder="请输入充电站名称"
						v-model="ordersUserName"></el-input>&nbsp;&nbsp;
				</div>
				<!-- </div> -->
				<div style="margin:5px;display: inline-block;">
					<span>开始时间：</span>
					<el-date-picker style="width: 160px;margin-left: 10px;" v-model="startTime" align="right"
						type="datetime" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间">
					</el-date-picker>&nbsp;&nbsp;&nbsp;
				</div>
				<div style="margin:5px;display: inline-block;">
					<span>截止时间：</span>
					<el-date-picker style="width: 160px;margin-left: 10px;" v-model="endTime" align="right"
						type="datetime" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择截止时间">
					</el-date-picker>
				</div>
				<div style="display: inline-block;">
					<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="select1">查询
					</el-button>
					<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="cleans1">重置
					</el-button>
				</div>
				<el-table v-loading="tableDataLoading" :data="orderData.list">
					<el-table-column fixed prop="ordersId" label="编号" width="80">
					</el-table-column>
					<!-- <el-table-column prop="isShop" label="开台类型">
						<template slot-scope="scope">
							<span v-if="scope.row.isShop==1">商家开台</span>
							<span v-else>用户开台</span>
						</template>
					</el-table-column>
					<el-table-column prop="goodsCategory" label="订单类型">
						<template slot-scope="scope">
							<span v-if="scope.row.goodsCategory==1">台球</span>
							<span v-if="scope.row.goodsCategory==2">棋牌</span>
							<span v-if="scope.row.goodsCategory==3">其他桌</span>
							<span v-if="scope.row.goodsCategory==4">柜子</span>
						</template>
					</el-table-column> -->
					<el-table-column fixed prop="tearoomName" label="充电站" width="160">
					</el-table-column>
					<!-- <el-table-column fixed prop="tearoomTitleImage" label="充电站图片">
						<template slot-scope="scope">
							<div v-if="scope.row.tearoomTitleImage">
								<span v-for="(item,index) in scope.row.tearoomTitleImage.split(',')">
									<el-popover placement="top-start" title="" trigger="hover" v-if="index==0">
										<img style="width: 50px; height: 50px;margin: 3px;" :src="item" alt=""
											slot="reference">
										<img style="width: 300px; height: 300px" :src="item" alt="">
									</el-popover>
								</span>
							</div>
							<div v-else>暂无图片</div>
						</template>
					</el-table-column> -->
					<el-table-column fixed prop="goodsName" label="充电桩名称">
						<template slot-scope="scope">
							<span>{{scope.row.goodsName}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="ordersNo" label="订单号" width="200">
					</el-table-column>
					<!-- <el-table-column prop="ordersType" label="订单类型" width="100">
						<template slot-scope="scope">
							<span v-if="scope.row.ordersType==1">开台</span>
							<span v-else-if="scope.row.ordersType==2">-</span>
							<span v-else-if="scope.row.ordersType==3">预约</span>
							<span v-else>-</span>
						</template>
					</el-table-column> -->
					<el-table-column prop="nickname" label="用户昵称" width="120">
						<template slot-scope="scope">
							<span style="color: #5cc477;cursor: pointer;" v-if="scope.row.userId"
								@click="updatesUser(scope.row.userId)">{{scope.row.payUserName ? scope.row.payUserName : '未绑定'}}</span>
							<span style="color: #f56c6c;" v-else>商家开台</span>
						</template>
					</el-table-column>
					<!-- <el-table-column prop="vip.vipName" label="会员等级" width="80">
					</el-table-column> -->
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
					<el-table-column prop="payTime" fixed="right" label="操作" width="160">
						<template slot-scope="scope">
							<el-button size="mini" v-if="scope.row.status === 3 " type="primary" plain
								style="margin:3px" :disabled="!isAuth('orderAdmin:huantai')"
								@click="huantaiClick(scope.row)">换台
							</el-button>
							<el-button size="mini" type="danger" plain style="margin:3px"
								:disabled="!isAuth('orderAdmin:zhuandan')&&scope.row.userId"
								@click="userselect(2,scope.row)">转单
							</el-button>
							<el-button size="mini" type="warning" style="margin:3px"
								v-if="scope.row.status === 2||scope.row.status === 3||scope.row.status === 4"
								:disabled="!isAuth('orderAdmin:refund')||scope.row.payMoney==scope.row.refundMoney"
								@click="refundClick(scope.row)">退全款
								<!-- v-if="scope.row.status === 2||scope.row.status === 3||scope.row.status === 4" -->
							</el-button>
							<el-button size="mini" type="warning" style="margin:3px"
								v-if="scope.row.status === 2||scope.row.status === 3||scope.row.status === 4"
								:disabled="!isAuth('orderAdmin:refund')||scope.row.payMoney==scope.row.refundMoney"
								plain @click="refundClickBf(scope.row)">部分退款<!-- -->
							</el-button>
							<!-- <el-button size="mini" type="primary" plain style="margin:3px"
								@click="xufeiClick(scope.row)">续费记录
							</el-button> -->
						</template>
					</el-table-column>
				</el-table>
				<div style="color: #B94A48;font-size: 20px;margin-top: 10px;display: inline-block;">
					本页累计交易统计：{{totalMoney.toFixed(2)}}元</div>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange3" @current-change="handleCurrentChange3"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="orderData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane>
			<el-tab-pane label="已发布优惠券" name="issue">
				<div style="text-align: right;margin-right: 20px;">
					<div style="position: relative;display: inline-block;margin: 5px;">
						<span>分类：</span>
						<el-select v-model="copeType" style="width:150px;margin-left: 10px;" @change="copeTypeBtn">
							<el-option v-for="item in copeMain" :key="item.value" :label="item.label"
								:value="item.value"></el-option>
						</el-select>&nbsp;&nbsp;&nbsp;
					</div>
					<el-button size="mini" type="primary" plain @click="copeTypeBtn()">刷新
					</el-button>
					<el-button size="mini" type="primary" :disabled="!isAuth('coupon:issue')" @click="issue()">发布
					</el-button>
				</div>

				<el-table v-loading="tableDataLoading" :data="issueData.content">
					<el-table-column prop="couponIssueId" label="id" width="80">
					</el-table-column>
					<el-table-column prop="couponType" label="券类型">
						<template slot-scope="scope">
							<span v-if="scope.row.couponType == 1">优惠券</span>
							<span v-if="scope.row.couponType == 2">实物</span>
							<span v-if="scope.row.couponType == 3">定时券</span>
						</template>
					</el-table-column>
					<el-table-column prop="tearoom.tearoomName" label="充电站名称" width="200">
					</el-table-column>
					<el-table-column prop="goodsList.goodsName" label="充电站名称" width="200">
						<template slot-scope="scope">
							<div v-if="scope.row.goodsList">
								<span v-for="(item,index) in scope.row.goodsList" :key="index">
									<span v-if="item!=null">{{item.goodsName}};</span>

								</span>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="couponName" label="优惠券名称" width="200">
					</el-table-column>
					<el-table-column prop="issueNumber" label="发布数量">
					</el-table-column>
					<el-table-column prop="remainNumber" label="剩余数量">
					</el-table-column>
					<!-- <el-table-column prop="hour" label="使用时长">
					</el-table-column> -->
					<el-table-column prop="validDay" label="有效期限(天)">
					</el-table-column>
					<el-table-column prop="teamRule" label="使用规则" width="200">
					</el-table-column>
					<el-table-column prop="buyRule" label="购买须知" width="200">
					</el-table-column>
					<el-table-column label="状态">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.status"
								@change="change(scope.row.couponIssueId,scope.row.status)" :active-value="openValue"
								:inactive-value="closeValue" active-color="#13ce66" inactive-color="#ff4949">
							</el-switch>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="150">
					</el-table-column>
					<el-table-column label="操作" width="150">
						<template slot-scope="scope">
							<el-button size="mini" type="danger" :disabled="!isAuth('couponissue:delete')"
								@click="shopdeletes(scope.row)">删除
							</el-button>
						</template>
					</el-table-column>

				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange6" @current-change="handleCurrentChange6"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="issueData.totalElements">
					</el-pagination>
				</div>
				<el-dialog title="发布优惠券" :visible.sync="dialogFormVisible8" center>
					<div style="margin-bottom: 10px;">
						<span style="width: 200px;display: inline-block;text-align: right;">优惠券类型：</span>
						<el-radio-group v-model="typeClass" @change="coupouClick()">
							<!-- <el-radio :label="1">团购券</el-radio> -->
							<el-radio :label="2">积分兑换券</el-radio>
							<el-radio :label="3">新人优惠券</el-radio>
						</el-radio-group>
					</div>
					<el-table-column prop="type" label="优惠券类型">
						<template slot-scope="scope">
							<!-- <span v-if="scope.row.type == 1">团购券</span> -->
							<span v-if="scope.row.type == 2">优惠券</span>
							<span v-if="scope.row.type == 3">实物</span>
							<span v-if="scope.row.type == 4">定时券</span>
						</template>
					</el-table-column>
					<div style="margin-bottom: 10px;">
						<span style="width: 200px;display: inline-block;text-align: right;">选择优惠券：</span>
						<el-select v-model="couponId" filterable placeholder="请选择优惠券" style="width:50%;"
							@change="shopClick()">
							<el-option v-for="item in coupouData.content" :key="item.couponId" :label="item.couponName"
								:value="item.couponId">
							</el-option>
						</el-select>
					</div>
					<div style="margin-bottom: 10px;">
						<span style="width: 200px;display: inline-block;text-align: right;">选择充电站：</span>
						<el-checkbox-group v-model="goods_ids" style="display: inline-block;">
							<el-checkbox v-for="(item,index) in goods_list" :key="index"
								:label="item.goodsId">{{item.goodsName}}</el-checkbox>
						</el-checkbox-group>
					</div>
					<div style="margin-bottom: 10px;">
						<span style="width: 200px;display: inline-block;text-align: right;">是否限量：</span>
						<el-radio-group v-model="isLimit">
							<el-radio :label="1">限量</el-radio>
							<el-radio :label="2">不限量</el-radio>
						</el-radio-group>
					</div>
					<div style="margin-bottom: 10px;" v-if="isLimit==1">
						<span style="width: 200px;display: inline-block;text-align: right;">发布数量：</span>
						<el-input style="width:50%;" v-model="issueNumber" type="number" :min="0" placeholder="请输入发布数量">
						</el-input>
					</div>

					<div style="margin-bottom: 10px;">
						<span style="width: 200px;display: inline-block;text-align: right;">状态：</span>
						<el-radio-group v-model="status">
							<el-radio :label="1">开启</el-radio>
							<el-radio :label="2">关闭</el-radio>
						</el-radio-group>
					</div>

					<div style="margin-bottom: 10px;">
						<span style="width: 200px;display: inline-block;text-align: right;">使用规则：</span>
						<el-input style="width:50%;" v-model="teamRule" type="textarea" :rows="3" placeholder="请输入使用规则">
						</el-input>
					</div>
					<div style="margin-bottom: 10px;">
						<span style="width: 200px;display: inline-block;text-align: right;">购买须知：</span>
						<el-input style="width:50%;" v-model="buyRule" type="textarea" :rows="3" placeholder="请输入购买须知">
						</el-input>
					</div>
					<div slot="footer" class="dialog-footer">
						<el-button @click="dialogFormVisible8 = false">取 消</el-button>
						<el-button type="primary" @click="issueNoticeTo()">确 定</el-button>
					</div>
				</el-dialog>
				<el-dialog title="修改" :visible.sync="dialogFormVisible9" center>
					<el-form :model="formissue">
						<el-form-item label="状态：" :label-width="formLabelWidth">
							<el-radio-group v-model="formissue.status">
								<el-radio :label="1">开启</el-radio>
								<el-radio :label="2">关闭</el-radio>
							</el-radio-group>
						</el-form-item>
						<el-form-item label="限量：" :label-width="formLabelWidth">
							<el-radio-group v-model="formissue.isLimit">
								<el-radio :label="1">限量</el-radio>
								<el-radio :label="2">不限量</el-radio>
							</el-radio-group>
						</el-form-item>
					</el-form>
					<div slot="footer" class="dialog-footer">
						<el-button @click="dialogFormVisible9 = false">取 消</el-button>
						<el-button type="primary" @click="amendNoticeTo()">确 定</el-button>
					</div>
				</el-dialog>
			</el-tab-pane>
			<el-tab-pane label="优惠券领取记录" name="record">
				<div style="display: inline-block;">
					<div style="margin:5px;position: relative;display: inline-block;">
						<span>优惠券平台：</span>
						<el-select v-model="platformTypeT" style="width:150px;margin-left: 10px;" @change="select3()">
							<el-option v-for="item in platformnum" :key="item.value" :label="item.label"
								:value="item.value">
							</el-option>
						</el-select>&nbsp;&nbsp;
					</div>
					<div style="margin:5px;display: inline-block;">
						<span>优惠券名称：</span>
						<el-input style="width:200px;" @keydown.enter.native="select3" clearable placeholder="请输入优惠券名称"
							v-model="couponName"></el-input>
					</div>
					<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="select3">查询
					</el-button>
					<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="cleans3">重置
					</el-button>
				</div>
				<el-table v-loading="tableDataLoading" :data="recordData.content">
					<el-table-column prop="couponUserId" label="id" width="80">
					</el-table-column>
					<el-table-column prop="platformType" label="优惠券平台">
						<template slot-scope="scope">
							<span v-if="scope.row.platformType==1">美团</span>
							<span v-else-if="scope.row.platformType==2">抖音</span>
							<span v-else-if="scope.row.platformType==3">快手</span>
							<span v-else>-</span>
						</template>
					</el-table-column>
					<el-table-column prop="type" label="优惠券类型">
						<template slot-scope="scope">
							<!-- <span v-if="scope.row.type == 1">团购券</span> -->
							<span v-if="scope.row.type == 2">优惠券</span>
							<span v-if="scope.row.type == 3">实物</span>
							<span v-if="scope.row.type == 4">定时券</span>
						</template>
					</el-table-column>
					<el-table-column prop="tearoom.tearoomName" label="充电站名称">
					</el-table-column>
					<el-table-column prop="couponName" label="优惠券名称" width="150">
					</el-table-column>
					<el-table-column prop="nickName" label="用户昵称">
						<template slot-scope="scope">
							<span style="color: #5cc477;cursor: pointer;"
								@click="updatesUser(scope.row.userId)">{{scope.row.nickName ? scope.row.nickName : '未绑定'}}</span>
						</template>
					</el-table-column>
					<!-- <el-table-column prop="hour" label="使用时长(小时)">
					</el-table-column>
					<el-table-column prop="minHour" label="最低使用时长(小时)">
					</el-table-column> -->
					<el-table-column prop="failureTime" label="有效期限(天)">
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
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange7" @current-change="handleCurrentChange7"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="recordData.totalElements">
					</el-pagination>
				</div>
			</el-tab-pane>
			<el-tab-pane label="充电时长统计" name="kaitaishichang">
				<div style="margin:5px;display: inline-block;">
					<span>开始时间：</span>
					<el-date-picker style="width: 160px;margin-left: 10px;" v-model="startTime" align="right"
						type="datetime" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间">
					</el-date-picker>&nbsp;&nbsp;&nbsp;
				</div>
				<div style="margin:5px;display: inline-block;">
					<span>截止时间：</span>
					<el-date-picker style="width: 160px;margin-left: 10px;" v-model="endTime" align="right"
						type="datetime" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择截止时间">
					</el-date-picker>
				</div>
				<div style="display: inline-block;">
					<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="select4">查询
					</el-button>
				</div>
				<el-row>
					<el-col :span="6" class="cards" v-for="(item,index) in ktsctjData" :key="index">
						<div class="box" style="height: auto;">
							<div class="box_num">
								<div class="box_color">{{item.goodsName}}</div>
								<div class="text_color"><span style="color:#666;font-size: 18px;">时长：</span>
									<span style="font-size: 18px;">{{item.sumSecond?item.sumSecond:0}}</span>
								</div>
								<div class="text_color"><span style="color:#666;font-size: 18px;">收益：</span>
									<span
										style="font-size: 18px;color: #f56c6c;">{{item.sumMoney?item.sumMoney:0 | numFilter}}</span>元
								</div>
							</div>
						</div>
					</el-col>
				</el-row>
			</el-tab-pane>
			<!-- <el-tab-pane label="商铺订单" name="shopOrders">
				<div style="display: inline-block;">
					<div style="position: relative;display: inline-block;margin: 5px;">
						<span>状态：</span>
						<el-select v-model="statusT" style="width:150px;margin-left: 10px;" @change="phoneSelect">
							<el-option v-for="item in taskstates" :key="item.value" :label="item.label"
								:value="item.value"></el-option>
						</el-select>&nbsp;&nbsp;&nbsp;
					</div>
					<div style="position: relative;display: inline-block;margin: 5px;">
						<span>订单号:</span>
						<el-input style="width: 200px;" @keydown.enter.native="phoneSelect" placeholder="请输入订单号"
							v-model="orderNumber"></el-input>&nbsp;&nbsp;
					</div>
					<div style="position: relative;display: inline-block;margin: 5px;">
						<span>充电站名称：</span>
						<el-input style="width: 200px;" @keydown.enter.native="phoneSelect" placeholder="请输入充电站名称"
							v-model="tearoomName">
						</el-input>
					</div>
					<div style="position: relative;display: inline-block;margin: 5px;">
						<span>手机号:</span>
						<el-input style="width: 200px;" @keydown.enter.native="phoneSelect" placeholder="请输入手机号"
							v-model="phone">
						</el-input>&nbsp;&nbsp;
					</div>
					<div style="position: relative;display: inline-block;margin: 5px;">
						<span>昵称:</span>
						<el-input style="width: 200px;" @keydown.enter.native="phoneSelect" placeholder="请输入昵称"
							v-model="userName">
						</el-input>&nbsp;&nbsp;
					</div>
					<el-button style="margin-left:15px;" size="mini" type="primary" icon="document"
						@click="phoneSelect">查询
					</el-button>
					<el-button style="margin-left:15px;" size="mini" type="primary" icon="document"
						@click="cleans2">重置</el-button>
				</div>
				<el-table v-loading="tableDataLoading" :data="tableData2.list">
					<el-table-column fixed prop="orderId" label="编号" width="80"></el-table-column>
					<el-table-column fixed prop="userName" label="下单用户">
						<template slot-scope="scope">
							<span style="color: #5cc477;cursor: pointer;" @click="updatesUser(scope.row.userId)">
								{{ scope.row.userName ? scope.row.userName : '未绑定' }}
							</span>
						</template>
					</el-table-column>
					<el-table-column fixed prop="userName" label="用户头像">
						<template slot-scope="scope">
							<img v-if="scope.row.avatar" :src="scope.row.avatar" width="60" height="60" />
							<span v-else>
								<img src="https://diancanadmin.xianmaxiong.com/sqx_fast/logo.png" width="60"
									height="60" />
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="手机号"></el-table-column>
					<el-table-column prop="tearoomName" label="充电站名称"></el-table-column>
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
							<el-button type="primary" size="mini" plain :disabled="!isAuth('missionSm:update')"
								@click="selectGgBtn(scope.row)">修改</el-button>
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
					<el-table-column fixed="right" prop="status" label="操作" width="120">
						<template slot-scope="scope">
							<el-button type="primary" @click="orderGoodsBtn(scope.row)" plain size="mini"
								style="margin: 3px;">订单商品</el-button>
							<el-button type="primary" @click="dayin(scope.row)" style="margin: 3px;"
								size="mini">打印小票</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChangeSp" @current-change="handleCurrentChangeSp"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="tableData2.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane> -->
			<!-- <el-tab-pane label="核销统计" name="hexiao">
				<el-select v-model="way" style="width:150px;margin-left: 10px;" @change="animeDatHx">
					<el-option v-for="(item,index) in ways" :key="index" :label="item.label" :value="item.value">
					</el-option>
				</el-select>&nbsp;&nbsp;&nbsp;
				<el-date-picker style="width: 160px;margin-left: 10px;" v-model="info.stockDate" align="right"
					type="datetime" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间"
					@change="animeDatHx">
				</el-date-picker>
				<el-row v-loading="tableDataLoading">
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">美团核销总数</div>
								<div class="text_color">
									<span>{{hexiaoCount.mtCount ? hexiaoCount.mtCount : 0}}</span>个
								</div>
							</div>
						</div>
					</el-col>
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">美团核销金额</div>
								<div class="text_color">
									<span>{{hexiaoCount.mtMoney ?  Number(hexiaoCount.mtMoney).toFixed(2) : 0}}</span>元
								</div>
							</div>
						</div>
					</el-col>
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">抖音核销总数</div>
								<div class="text_color">
									<span>{{hexiaoCount.dyCount ? hexiaoCount.dyCount : 0}}</span>个
								</div>
							</div>
						</div>
					</el-col>
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">抖音核销金额</div>
								<div class="text_color">
									<span>{{hexiaoCount.dyMoney ? Number(hexiaoCount.dyMoney).toFixed(2): 0}}</span>元
								</div>
							</div>
						</div>
					</el-col>
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">购买核销总数</div>
								<div class="text_color">
									<span>{{hexiaoCount.payCount ? hexiaoCount.payCount : 0}}</span>个
								</div>
							</div>
						</div>
					</el-col>
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">购买核销金额</div>
								<div class="text_color">
									<span>{{hexiaoCount.payMoney ?  Number(hexiaoCount.payMoney).toFixed(2) : 0}}</span>元
								</div>
							</div>
						</div>
					</el-col>
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">赠送核销总数</div>
								<div class="text_color">
									<span>{{hexiaoCount.zsCount ? hexiaoCount.zsCount : 0}}</span>个
								</div>
							</div>
						</div>
					</el-col>
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">赠送核销金额</div>
								<div class="text_color">
									<span>{{hexiaoCount.zsMoney ? Number(hexiaoCount.zsMoney).toFixed(2): 0}}</span>元
								</div>
							</div>
						</div>
					</el-col>
				</el-row>
			</el-tab-pane> -->
			<el-tab-pane label="收益明细" name="shopShouyi">
				<el-select v-model="way" style="width:150px;margin-left: 10px;" @change="animeDatSy">
					<el-option v-for="(item,index) in ways1" :key="index" :label="item.label" :value="item.value">
					</el-option>
				</el-select>&nbsp;&nbsp;&nbsp;
				<div>
					<el-table :data="tableDataq">
						<el-table-column prop="goodsId" label="编号" width="100">
							<template slot-scope="scope">
								<span>{{scope.$index+1}}</span>
							</template>
						</el-table-column>
						<el-table-column prop="dateTime" label="时间">
						</el-table-column>
						<el-table-column prop="money" label="收益">
							<template slot-scope="scope">
								<span>{{scope.row.money?scope.row.money:0 }}</span>
							</template>
						</el-table-column>
						<el-table-column fixed="right" prop="status" label="操作" width="120">
							<template slot-scope="scope">
								<el-button type="primary" @click="orderSysBtn(scope.row)" plain size="mini"
									style="margin: 3px;">详情列表</el-button>
							</template>
						</el-table-column>
					</el-table>
					<div style="text-align: center;margin-top: 10px;">
						<el-button size="mini" :disabled="page==1" @click="handleCurrentChangeMx1">上一页</el-button>
						<el-button size="mini" @click="handleCurrentChangeMx2">下一页</el-button>
					</div>
				</div>
			</el-tab-pane>
			<!-- <el-tab-pane label="排号列表" name="paihao">
				<div style="margin:5px;display: inline-block;">
					<span>排号类型：</span>
					<el-select v-model="goodsCategory" style="width:150px;margin-left: 10px;" @change="animeDatPh">
						<el-option v-for="(item,index) in goodsCategoryListP" :key="index" :label="item.label"
							:value="item.value">
						</el-option>
					</el-select>&nbsp;&nbsp;&nbsp;
					<span>状态：</span>
					<el-select v-model="statusP" style="width:150px;margin-left: 10px;" @change="animeDatPh">
						<el-option v-for="(item,index) in statusListP" :key="index" :label="item.label"
							:value="item.value">
						</el-option>
					</el-select>&nbsp;&nbsp;&nbsp;
					<span>日期：</span>
					<el-date-picker style="width: 160px;margin-left: 10px;" v-model="createTime" align="right"
						type="date" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择日期">
					</el-date-picker>&nbsp;&nbsp;&nbsp;
					<el-button type="primary" @click="animeDatPh()" plain size="mini"
						style="margin: 3px;">查询</el-button>
					<el-button type="primary" @click="animeDatCz()" plain size="mini"
						style="margin: 3px;">重置</el-button>
				</div>
				<div>
					<el-table :data="tableDatPh.list">
						<el-table-column prop="queueUpId" label="编号" width="100">
						</el-table-column>
						<el-table-column prop="nickname" label="排号用户" width="120">
							<template slot-scope="scope">
								<span style="color: #5cc477;cursor: pointer;"
									@click="updatesUser(scope.row.userId)">{{scope.row.user ? scope.row.user.userName : '-'}}</span>
							</template>
						</el-table-column>
						<el-table-column prop="num" label="号码">
						</el-table-column>
						<el-table-column prop="goodsCategory" label="排号类型">
							<template slot-scope="scope">
								<span v-if="scope.row.goodsCategory==1">台球</span>
								<span v-if="scope.row.goodsCategory==2">棋牌</span>
								<span v-if="scope.row.goodsCategory==3">其他桌</span>
								<span v-if="scope.row.goodsCategory==4">柜子</span>
							</template>
						</el-table-column>
						<el-table-column prop="status" label="状态" width="100">
							<template slot-scope="scope">
								<span v-if="scope.row.status==1" style="color: #B94A48;">排队中</span>
								<span v-if="scope.row.status==2">已完成</span>
								<span v-if="scope.row.status==3">已过号</span>
							</template>
						</el-table-column>
						<el-table-column prop="createTime" label="创建时间">
						</el-table-column>
						<el-table-column fixed="right" prop="status" label="操作" width="120">
							<template slot-scope="scope">
								<el-button type="primary" v-if="scope.row.status==1" @click="guohaoBtn(scope.row)" plain
									size="mini" style="margin: 3px;">过号</el-button>
							</template>
						</el-table-column>
					</el-table>
					<div style="text-align: center;margin-top: 10px;">
						<el-pagination @size-change="handleSizeChangeSh" @current-change="handleCurrentChangeSh"
							:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
							layout="total,sizes, prev, pager, next" :total="tableDatPh.totalCount">
						</el-pagination>
					</div>
				</div>
			</el-tab-pane>
			<el-tab-pane label="爱心钱包" name="aixinqianbao">
				<el-row v-loading="tableDataLoading">
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">爱心账户</div>
								<div class="text_color">
									<span>{{hexiaoCount.money ?  Number(hexiaoCount.money).toFixed(2) : 0}}</span>元
									<el-button size="mini" :disabled="!isAuth('userList:updatejf')"
										style="color: #5cc477;background: #fff;border: none;margin: 10px 0;"
										@click="rechargenone(2,1)">修改
									</el-button>
								</div>
							</div>
						</div>
					</el-col>
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">已捐款金额</div>
								<div class="text_color">
									<span>{{hexiaoCount.donationMoney ?  Number(hexiaoCount.donationMoney).toFixed(2) : 0}}</span>元
									<el-button size="mini" :disabled="!isAuth('userList:updatejf')"
										style="color: #5cc477;background: #fff;border: none;margin: 10px 0;"
										@click="rechargenone(2,2)">修改
									</el-button>
								</div>
							</div>
						</div>
					</el-col>
				</el-row>
				<div style="font-size: 18px;margin: 10px 20px;font-weight: 600;color: #999;">爱心钱包明细</div>
				<div style="margin:5px;display: inline-block;">
					<div style="position: relative;display: inline-block;margin:5px;">
						<span>类型：</span>
						<el-select v-model="moneyTypeT" style="width:150px;margin-left: 10px;" @change="animeDatAxqb">
							<el-option v-for="(item,index) in moneyTypeList" :key="index" :label="item.label"
								:value="item.value">
							</el-option>
						</el-select>&nbsp;&nbsp;&nbsp;
					</div>
					<div style="position: relative;display: inline-block;margin:5px;">
						<span>用户昵称：</span>
						<el-input style="width: 150px;" @keydown.enter.native="animeDatAxqb" clearable
							placeholder="请输入用户昵称" v-model="userNameT"></el-input>&nbsp;&nbsp;
					</div>
					<div style="position: relative;display: inline-block;margin:5px;">
						<span>手机号：</span>
						<el-input style="width: 150px;" @keydown.enter.native="animeDatAxqb" clearable
							placeholder="请输入手机号" v-model="phoneT"></el-input>&nbsp;&nbsp;
					</div>
					<div style="position: relative;display: inline-block;margin:5px;">
						<span>标题：</span>
						<el-input style="width: 150px;" @keydown.enter.native="animeDatAxqb" clearable
							placeholder="请输入标题" v-model="titleT"></el-input>&nbsp;&nbsp;
					</div>
					<div style="position: relative;display: inline-block;margin:5px;">
						<span>内容：</span>
						<el-input style="width: 150px;" @keydown.enter.native="animeDatAxqb" clearable
							placeholder="请输入内容" v-model="contentT"></el-input>&nbsp;&nbsp;
					</div>
					<el-button type="primary" @click="animeDatAxqb()" plain size="mini"
						style="margin: 3px;">查询</el-button>
					<el-button type="primary" @click="animeDatAxqbCz()" plain size="mini"
						style="margin: 3px;">重置</el-button>
				</div>
				<div>
					<el-table :data="tableDatPh.list" v-loading="tableDataLoadingA">
						<el-table-column prop="id" label="编号" width="100">
						</el-table-column>
						<el-table-column prop="moneyType" label="类型" width="100">
							<template slot-scope="scope">
								<span v-if="scope.row.moneyType==1" style="color: #13ce66;">爱心钱包</span>
								<span v-if="scope.row.moneyType==2" style="color: #B94A48;">已捐款</span>
							</template>
						</el-table-column>
						<el-table-column prop="userName" label="用户昵称" width="120">
							<template slot-scope="scope">
								<span style="color: #5cc477;cursor: pointer;"
									@click="updatesUser(scope.row.userId)">{{scope.row.userName ? scope.row.userName : '-'}}</span>
							</template>
						</el-table-column>
						<el-table-column prop="phone" label="手机号">
						</el-table-column>
						<el-table-column prop="classify" label="分类" width="100">
							<template slot-scope="scope">
								<span v-if="scope.row.classify==1" style="color: #13ce66;">订单</span>
								<span v-if="scope.row.classify==2" style="color: #B94A48;">后台</span>
							</template>
						</el-table-column>
						<el-table-column prop="title" label="标题">
						</el-table-column>
						<el-table-column prop="content" label="内容">
						</el-table-column>
						<el-table-column prop="money" label="金额" width="100">
							<template slot-scope="scope">
								<span v-if="scope.row.type==1" style="color: #13ce66;">+ {{scope.row.money}}</span>
								<span v-if="scope.row.type==2" style="color: #B94A48;">- {{scope.row.money}}</span>
							</template>
						</el-table-column>
						<el-table-column prop="createTime" label="创建时间">
						</el-table-column>
					</el-table>
					<div style="text-align: center;margin-top: 10px;">
						<el-pagination @size-change="handleSizeChangeAx" @current-change="handleCurrentChangeAx"
							:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
							layout="total,sizes, prev, pager, next" :total="tableDatPh.totalCount">
						</el-pagination>
					</div>
				</div>
			</el-tab-pane>
			<el-tab-pane label="爱心公示" name="aixingongshi">
				<div style="position: relative;display: inline-block;margin:5px;">
					<span>标题：</span>
					<el-input style="width: 150px;" @keydown.enter.native="animeDatAxgs" clearable placeholder="请输入标题"
						v-model="titleT"></el-input>&nbsp;&nbsp;
				</div>
				<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="animeDatAxgs">查询
				</el-button>
				<el-button style='margin-left:15px;' size="mini" :disabled="!isAuth('materialsList:add')" type="primary"
					icon="document" @click="updateswz()">添加</el-button>&nbsp;&nbsp;&nbsp;&nbsp;
				<div>
					<el-table :data="tableDataq.list">
						<el-table-column prop="welfareWordId" label="编号" width="100">
						</el-table-column>
						<el-table-column prop="welfareWordTitle" label="标题" width="250">
							<template slot-scope="scope">
								<div
									style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
									{{scope.row.welfareWordTitle}}
								</div>
							</template>
						</el-table-column>
						<el-table-column prop="welfareImg" label="轮播图" width="250">
							<template slot-scope="scope">
								<div v-if="scope.row.welfareImg == null || scope.row.welfareImg == ''">
									暂无图片
								</div>
								<div v-if="scope.row.welfareImg" style="display:flex;flex-wrap: wrap;">
									<div v-for="(item,index) in scope.row.welfareImg.split(',')">
										<el-popover placement="top-start" title="" trigger="hover">
											<img style="width: 50px; height: 50px;margin: 3px;" :src="item" alt=""
												slot="reference">
											<img style="width: 200px; height: auto" :src="item" alt="">
										</el-popover>
									</div>
								</div>
							</template>
						</el-table-column>
						<el-table-column prop="welfareWordContent" label="内容">
							<template slot-scope="scope">
								<div
									style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
									{{scope.row.welfareWordContent}}
								</div>
							</template>
						</el-table-column>
						<el-table-column prop="sort" label="排序" width="100">
						</el-table-column>
						<el-table-column prop="createTime" label="创建时间" width="160">
						</el-table-column>
						<el-table-column label="操作" width="160">
							<template slot-scope="scope">
								<el-button size="mini" type="primary" :disabled="!isAuth('materialsList:update')"
									@click="updateswz(scope.row)">编辑
								</el-button>
								<el-button size="mini" type="danger" :disabled="!isAuth('materialsList:delete')"
									@click="deleteswz(scope.row)">删除
								</el-button>
							</template>
						</el-table-column>
					</el-table>
					<div style="text-align: center;margin-top: 10px;">
						<el-pagination @size-change="handleSizeChangeAxgs" @current-change="handleCurrentChangeAxgs"
							:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
							layout="total,sizes, prev, pager, next" :total="tableDataq.totalCount">
						</el-pagination>
					</div>
				</div>
			</el-tab-pane> -->
			<!-- <el-tab-pane label="会员钱包" name="vipwallet">
				<el-row v-loading="tableDataLoading1">
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">会员剩余总金额</div>
								<div class="text_color">
									<span>{{vipCount.money ?  Number(vipCount.money).toFixed(2) : 0}}</span>元
								</div>
							</div>
						</div>
					</el-col>
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">会员总人数</div>
								<div class="text_color">
									<span>{{vipCount.count ?  Number(vipCount.count) : 0}}</span>人

								</div>
							</div>
						</div>
					</el-col>
				</el-row>
				<div style="font-size: 18px;margin: 10px 20px;font-weight: 600;color: #999;">会员钱包列表</div>
				<div style="position: relative;display: inline-block;margin:5px;">
					<span>手机号：</span>
					<el-input style="width: 150px;" @keydown.enter.native="animeDatVipW" clearable placeholder="请输入手机号"
						v-model="titleT"></el-input>&nbsp;&nbsp;
				</div>
				<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="animeDatVipW">查询
				</el-button>
				<el-button style='margin-left:15px;' size="mini" :disabled="!isAuth('materialsList:add')" type="primary"
					icon="document" @click="updatesVipW()">添加</el-button>&nbsp;&nbsp;&nbsp;&nbsp;
				<div>
					<el-table :data="tableDataWallet.records">
						<el-table-column prop="memberUserId" label="编号" width="100">
						</el-table-column>
						<el-table-column prop="phone" label="手机号">
							<template slot-scope="scope">
								<span style="color: #5cc477;cursor: pointer;"
									@click="updatesUser(scope.row.memberUserId)">{{scope.row.phone ? scope.row.phone : '-'}}</span>
							</template>
						</el-table-column>
						<el-table-column prop="money" label="钱包金额">
							<template slot-scope="scope">
								{{scope.row.money?scope.row.money:'0'}}
								<el-button size="mini" type="primary" :disabled="!isAuth('materialsList:update')"
									@click="updatesVipWmx(1,scope.row)">修改
								</el-button>
							</template>
						</el-table-column>
						<el-table-column prop="createTime" label="创建时间" width="160">
						</el-table-column>
						<el-table-column label="操作" width="160">
							<template slot-scope="scope">
								<el-button size="mini" type="primary" :disabled="!isAuth('materialsList:update')"
									@click="updatesVipW(scope.row)">编辑
								</el-button>
								<el-button size="mini" type="danger" :disabled="!isAuth('materialsList:delete')"
									@click="deletesVipW(scope.row)">删除
								</el-button>
							</template>
						</el-table-column>
					</el-table>
					<div style="text-align: center;margin-top: 10px;">
						<el-pagination @size-change="handleSizeChangeVipW" @current-change="handleCurrentChangeVipW"
							:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
							layout="total,sizes, prev, pager, next" :total="tableDataWallet.total">
						</el-pagination>
					</div>
				</div>
			</el-tab-pane>
			<el-tab-pane label="会员钱包明细" name="vipwalletMx">
				<div style="position: relative;display: inline-block;margin:5px;">
					<span>手机号：</span>
					<el-input style="width: 150px;" @keydown.enter.native="animeDatVipW" clearable placeholder="请输入手机号"
						v-model="titleT"></el-input>&nbsp;&nbsp;
				</div>
				<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="animeDatVipW">查询
				</el-button>
				<el-button style='margin-left:15px;' size="mini" :disabled="!isAuth('materialsList:add')" type="primary"
					icon="document" @click="updatesVipWmx(2)">添加</el-button>&nbsp;&nbsp;&nbsp;&nbsp;
				<div>
					<el-table :data="tableDataWalletMx.records">
						<el-table-column prop="memberUserId" label="编号" width="100">
						</el-table-column>
						<el-table-column prop="phone" label="手机号">
						</el-table-column>
						<el-table-column prop="title" label="标题">
						</el-table-column>
						<el-table-column prop="content" label="内容">
						</el-table-column>
						<el-table-column prop="money" label="金额">
							<template slot-scope="scope">
								<span style="color:limegreen;cursor: pointer;" v-if="scope.row.type == 1 ">+
									{{scope.row.money}}</span>
								<span style="color: #ff4949;cursor: pointer;" v-if="scope.row.type == 2 ">-
									{{scope.row.money}}</span>
							</template>
						</el-table-column>
						<el-table-column prop="createTime" label="创建时间" width="160">
						</el-table-column>
						<el-table-column label="操作" width="160">
							<template slot-scope="scope">
								<el-button size="mini" type="primary" :disabled="!isAuth('materialsList:update')"
									@click="updatesVipWmx(2,scope.row)">编辑
								</el-button>
								<el-button size="mini" type="danger" :disabled="!isAuth('materialsList:delete')"
									@click="deletesVipWmx(scope.row)">删除
								</el-button>
							</template>
						</el-table-column>
					</el-table>
					<div style="text-align: center;margin-top: 10px;">
						<el-pagination @size-change="handleSizeChangeVipWmx" @current-change="handleCurrentChangeVipWmx"
							:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
							layout="total,sizes, prev, pager, next" :total="tableDataWalletMx.total">
						</el-pagination>
					</div>
				</div>
			</el-tab-pane> -->
			<!-- <el-tab-pane label="收藏歌单" name="musicPlay">
				<div style="position: relative;display: inline-block;margin:5px;">
					<span>关键词：</span>
					<el-input style="width: 150px;" @keydown.enter.native="animeDatmusic" clearable placeholder="请输入关键词"
						v-model="titleT"></el-input>&nbsp;&nbsp;
				</div>
				<el-button style="margin-left:15px;" size="mini" type="primary" icon="document"
					@click="animeDatmusic">查询
				</el-button>
				<el-button style='margin-left:15px;' size="mini" :disabled="!isAuth('materialsList:add')" type="primary"
					icon="document" @click="musicselect()">添加</el-button>&nbsp;&nbsp;&nbsp;&nbsp;
				<div>
					<el-table :data="tableDataWalletMx.records">
						<el-table-column prop="shopCollectMusicId" label="编号" width="80" fixed="left">
						</el-table-column>
						<el-table-column prop="musicName" label="歌曲名称">
						</el-table-column>
						<el-table-column prop="musicUrl" label="歌曲地址" width="350">
							<template slot-scope="scope">
								<audio :id="'myAudio'+scope.row.musicLibraryId"
									v-if="scope.row.musicUrl!=''&&scope.row.musicUrl!=null&&scope.row.musicUrl!='null'"
									duration :src="scope.row.musicUrl" type="audio/wav" controls
									preload="metadata"></audio>
								<span v-else>暂无语音</span>

							</template>
						</el-table-column>
						<el-table-column prop="musicAuthor" label="歌曲作者">
						</el-table-column>
						<el-table-column prop="musicPicUrl" label="歌曲封面图" width="80">
							<template slot-scope="scope">
								　　<img v-if="scope.row.musicPicUrl &&scope.row.musicPicUrl!=''"
									:src="scope.row.musicPicUrl" width="40" height="40" />
								<span v-else>暂无图片</span>
							</template>
						</el-table-column>
						<el-table-column prop="createTime" label="创建时间" width="160">
						</el-table-column>
						<el-table-column label="操作" width="160">
							<template slot-scope="scope">
								<el-button size="mini" type="danger" :disabled="!isAuth('materialsList:delete')"
									@click="deletesmusic(scope.row)">删除
								</el-button>
							</template>
						</el-table-column>
					</el-table>
					<div style="text-align: center;margin-top: 10px;">
						<el-pagination @size-change="handleSizeChangemusic" @current-change="handleCurrentChangemusic"
							:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
							layout="total,sizes, prev, pager, next" :total="tableDataWalletMx.total">
						</el-pagination>
					</div>
				</div>
			</el-tab-pane> -->
			<el-dialog title="开关管理" :visible.sync="dialogFormVisible10" center>
				<el-form :model="switchq">
					<el-form-item>
						<el-table :data="gridData" border height="300">
							<el-table-column property="name" label="菜单名称"></el-table-column>
							<el-table-column property="menusstate" label="开关设置">
								<template slot-scope="scope">
									<el-switch active-text="开" inactive-text="关" active-color="#13ce66"
										inactive-color="#ff4949" active-value='1' inactive-value='0'
										v-model="scope.row.menusstate" @change="changeswitchq(scope.$index,scope.row)">
									</el-switch>
								</template>
							</el-table-column>
						</el-table>
					</el-form-item>
				</el-form>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible10 = false">取 消</el-button>
					<el-button type="primary" @click="switchamendNoticeTo()">确 定</el-button>
				</div>
			</el-dialog>
			<el-dialog title="修改充电桩状态" :visible.sync="dialogFormVisibleCs" center width="80%">
				<div>
					<span style="width: 200px;display: inline-block;text-align: right;">状态：</span>
					<el-radio-group v-model="types">
						<el-radio :label="1">上架</el-radio>
						<el-radio :label="2">暂停使用</el-radio>
					</el-radio-group>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisibleCs = false">取 消</el-button>
					<el-button type="primary" @click="change()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 修改金额 -->
			<el-dialog title="修改金额" :visible.sync="dialogFormVisibleQb" center>
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
				<div style="margin-bottom: 10px;" v-if="typeIndex==2">
					<span style="width: 200px;display: inline-block;text-align: right;">标题：</span>
					<el-input style="width:50%;" v-model="titleAx" type="text" :controls="false"
						placeholder="请输入标题"></el-input>
				</div>
				<div style="margin-bottom: 10px;" v-if="typeIndex==2">
					<span style="width: 200px;display: inline-block;text-align: right;">内容：</span>
					<el-input style="width:50%;" v-model="contentAx" type="text" :controls="false"
						placeholder="请输入内容"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisibleQb = false">取 消</el-button>
					<el-button type="primary" @click="StairNoticeTo1()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 结算金额 -->
			<el-dialog title="结算金额" :visible.sync="dialogFormVisibleJs" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">金额：</span>
					<el-input style="width:50%;" v-model="money" type="number" :min="0" :max="100" :controls="false"
						placeholder="请输入金额"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisibleJs = false">取 消</el-button>
					<el-button type="primary" @click="StairNoticeTo2()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 充电站数据 -->
			<el-dialog title="充电桩数据" :visible.sync="dialogFormVisibleCssj" center width="70%">
				<div style="font-size: 18px;margin: 10px 20px;font-weight: 600;color: #999;">日数据</div>
				<el-date-picker style="width: 160px;margin-left: 10px;" v-model="info.stockDate" align="right"
					type="date" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择时间" @change="timeDateR">
				</el-date-picker>
				<el-row v-loading="tableDataLoadingR">
					<el-col :span="12" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">总收益</div>
								<div class="text_color"><span>{{incomeR.money?incomeR.money:0 | numFilter}}</span>元
								</div>
							</div>
						</div>
					</el-col>
					<el-col :span="12" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">订单数</div>
								<div class="text_color"><span>{{incomeR.count?incomeR.count:0}}</span>单
								</div>
							</div>
						</div>
					</el-col>
				</el-row>
				<div style="font-size: 18px;margin: 10px 20px;font-weight: 600;color: #999;">周数据</div>
				<el-date-picker v-model="value1" type="daterange" range-separator="至" start-placeholder="开始日期"
					end-placeholder="结束日期" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择时间"
					@change="timeDateZ" :picker-options="pickerOptions">
					<!-- :disabled-date="disabledDateFn" @calendar-Change="calendarChange" -->
				</el-date-picker>
				<el-row v-loading="tableDataLoadingZ">
					<el-col :span="12" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">总收益</div>
								<div class="text_color">
									<span>{{incomeZ.money?incomeZ.money:0 | numFilter}}</span>元
								</div>
							</div>
						</div>
					</el-col>
					<el-col :span="12" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">订单数</div>
								<div class="text_color">
									<span>{{incomeZ.count?incomeZ.count:0}}</span>单
								</div>
							</div>
						</div>
					</el-col>
				</el-row>
				<div style="font-size: 18px;margin: 10px 20px;font-weight: 600;color: #999;">月数据</div>
				<el-date-picker style="width: 160px;margin-left: 10px;" v-model="info.stockDate" align="right"
					type="month" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择时间" @change="timeDateY">
				</el-date-picker>
				<el-row v-loading="tableDataLoadingY">
					<el-col :span="12" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">总收益</div>
								<div class="text_color"><span>{{incomeY.money?incomeY.money:0 | numFilter}}</span>元
								</div>
							</div>
						</div>
					</el-col>
					<el-col :span="12" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">订单数</div>
								<div class="text_color"><span>{{incomeY.count?incomeY.count:0}}</span>单
								</div>
							</div>
						</div>
					</el-col>
				</el-row>
				<div style="font-size: 18px;margin: 10px 20px;font-weight: 600;color: #999;">年数据</div>
				<el-date-picker style="width: 160px;margin-left: 10px;" v-model="info.stockDate" align="right"
					type="year" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择时间" @change="timeDateN">
				</el-date-picker>
				<el-row v-loading="tableDataLoadingN">
					<el-col :span="12" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">总收益</div>
								<div class="text_color">
									<span>{{incomeN.money?incomeN.money:0 | numFilter}}</span>元
								</div>
							</div>
						</div>
					</el-col>
					<el-col :span="12" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">订单数</div>
								<div class="text_color">
									<span>{{incomeN.count?incomeN.count:0}}</span>单
								</div>
							</div>
						</div>
					</el-col>
				</el-row>
			</el-dialog>
			<!-- 用户列表 -->
			<el-dialog title="用户列表" :visible.sync="dialogFormVisible3" center width="80%">
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
					<el-button style='margin-left:15px;' size="mini" type="primary" plain icon="document"
						@click="userclose">
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
					<el-table-column prop="age" label="年龄">
					</el-table-column>
					<el-table-column prop="sex" label="性别">
						<template slot-scope="scope">
							<span>{{scope.row.sex==1 ? '男' : '女'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间">
					</el-table-column>
					<el-table-column label="操作" fixed='right'>
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="confirm(scope.row)"
								v-if="indexType==1&&activeName=='first'">确定
							</el-button>
							<el-button size="mini" type="primary" @click="confirmBj(scope.row)" v-if="indexType==2">确定
							</el-button>
							<!-- <el-button size="mini" type="primary" @click="confirmBj(scope.row)"
								v-if="activeName=='baojie'&&!scope.row.tearoomId">确定添加
							</el-button> -->
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
			<!-- 用户列表 -->
			<el-dialog title="用户列表" :visible.sync="dialogFormVisibleYg" center width="80%">
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
					<el-button style='margin-left:15px;' size="mini" type="primary" plain icon="document"
						@click="userclose">
						重置
					</el-button>
					<el-button style='margin:0 0 20px 20px;' v-if="typeIndex==1" size="mini" type="warning"
						icon="document" @click="userClcik()" :disabled="checkBoxData.length <= 0">
						确定选择用户
					</el-button>
				</div>
				<el-table width="780px" v-loading="tableDataLoading4" :data="userIds.list"
					@selection-change="changeFun">
					<el-table-column type="selection">
					</el-table-column>
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
					<el-table-column prop="age" label="年龄">
					</el-table-column>
					<el-table-column prop="sex" label="性别">
						<template slot-scope="scope">
							<span>{{scope.row.sex==1 ? '男' : '女'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间">
					</el-table-column>
					<el-table-column v-if="typeIndex==2" prop="createTime" label="操作" width="100">
						<template slot-scope="scope">
							<el-button style='margin:0 0 20px 20px;' size="mini" type="warning" icon="document"
								@click="userClcik2(scope.row)">
								确定添加
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
			<!-- 充电站二维码 -->
			<el-dialog :visible.sync="dialogFormVisible2" center :title="titles">
				<div>
					<img :src="erweimaImg" style="width: 50%;margin-left: 25%;" />
					<div style="text-align: center;margin-top: 20px;">
						<el-button type="primary" @click="chakan()">打开窗口查看</el-button>
						<el-button type="primary" @click="downloadImage()"><a style="color: #fff;">下载</a></el-button>
					</div>
				</div>
			</el-dialog>
			<!-- 修改金额 -->
			<el-dialog title="批量修改金额" :visible.sync="dialogFormVisibleJe" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">小时价：</span>
					<el-input style="width:50%;" v-model="hourPrice" type="number" placeholder="请输入小时价"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">分钟价：</span>
					<el-input style="width:50%;" v-model="minutePrice" type="number" placeholder="请输入分钟价"></el-input>
				</div>

				<!-- <div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">押金：</span>
					<el-input style="width:50%;" v-model="cashPledge" type="number" placeholder="请输入押金"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">预约金额：</span>
					<el-input style="width:50%;" v-model="subscription" type="number" placeholder="请输入预约金额"></el-input>
				</div> -->
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisibleJe = false">取 消</el-button>
					<el-button type="primary" @click="StairNoticeToJe()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 特殊时间表 -->
			<el-dialog title="特殊时间表" :visible.sync="dialogFormVisibleTssj" center width="70%">
				<div style="margin:2% 0;text-align: right;">
					<el-button style='margin-left:15px;' size="mini" type="primary" icon="document"
						@click="timeclick()">
						刷新
					</el-button>
					<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="timeadd()">
						添加
					</el-button>
				</div>
				<el-table width="780px" v-loading="tableDataLoading4" :data="userIds.records">
					<el-table-column prop="goodsTimeId" fixed label="编号" width="80">
					</el-table-column>
					<el-table-column prop="startTime" label="开始时间">
					</el-table-column>
					<el-table-column prop="endTime" label="结束时间">
					</el-table-column>
					<el-table-column prop="hourPrice" label="小时价(元)">
					</el-table-column>
					<el-table-column prop="minutePrice" label="分钟价(元)">
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间">
					</el-table-column>
					<el-table-column label="操作" fixed='right' width="160">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="timeadd(scope.row)">修改
							</el-button>
							<el-button size="mini" type="danger" @click="timedelete(scope.row)">删除
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange4" @current-change="handleCurrentChange4"
						:page-sizes="[5, 10, 15, 20]" :page-size="size1" :current-page="page1"
						layout="total,sizes, prev, pager, next,jumper" :total="userIds.total">
					</el-pagination>
				</div>
			</el-dialog>
			<!-- 添加、修改 时间弹框 -->
			<el-dialog :title="titles" :visible.sync="dialogFormVisibleTs" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">开始时间：</span>
					<el-time-select v-model="startTime" :picker-options="{
					    start: '00:00',
					    step: '00:01',
					    end: '23:59'
					  }" placeholder="选择时间" style="width:50%;">
					</el-time-select>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">结束时间：</span>
					<el-time-select v-model="endTime" :picker-options="{
					    start: '00:00',
					    step: '00:01',
					    end: '23:59'
					  }" placeholder="选择时间" style="width:50%;">
					</el-time-select>
				</div>
				<!-- <div style="margin-bottom: 10px;" v-if="tearoomType==2">
					<span style="width: 200px;display: inline-block;text-align: right;">小时价(元)：</span>
					<el-input style="width:50%;" v-model="hourPrice" type="number" placeholder="请输入小时价"></el-input>
				</div>
				<div style="margin-bottom: 10px;" v-if="tearoomType==2">
					<span style="width: 200px;display: inline-block;text-align: right;">会员小时价(元)：</span>
					<el-input style="width:50%;" v-model="memberHourPrice" type="number" placeholder="请输入会员小时价"></el-input>
				</div> -->
				
				<div style="margin-bottom: 10px;" v-if="tearoomType==1">
					<span style="width: 200px;display: inline-block;text-align: right;">分钟价(元)：</span>
					<el-input style="width:50%;" v-model="minutePrice" type="number" placeholder="请输入分钟价"></el-input>
				</div>
				<div style="margin-bottom: 10px;" v-if="tearoomType==1">
					<span style="width: 200px;display: inline-block;text-align: right;">会员分钟价(元)：</span>
					<el-input style="width:50%;" v-model="memberMinutePrice" type="number" placeholder="请输入会员分钟价"></el-input>
				</div>
				<div style="margin-bottom: 10px;" v-if="tearoomType==2">
					<span style="width: 200px;display: inline-block;text-align: right;">用户每度电价格(元)：</span>
					<el-input style="width:50%;" v-model="electricityPrice" type="number" placeholder="请输入用户每度电价格"></el-input>
				</div>
				<div style="margin-bottom: 10px;" v-if="tearoomType==2">
					<span style="width: 200px;display: inline-block;text-align: right;">会员每度电价格(元)：</span>
					<el-input style="width:50%;" v-model="memberElectricityPrice" type="number" placeholder="请输入会员每度电价格"></el-input>
				</div>
				
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisibleTs = false">取 消</el-button>
					<el-button type="primary" @click="timeNoticeToJe()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 在线直播 -->
			<el-dialog title="直播推流" :visible.sync="dialogFormVisibleZb" center width="80%" :before-close="clonkVideo">
				<video id='aaaa' ref="videoElement" controls width="80%" style="margin: 2% 10%;"></video>

				<div slot="footer" class="dialog-footer">
					<el-button @click="clonkVideo">取 消</el-button>
				</div>
			</el-dialog>
			<!-- 商铺列表 -->
			<el-dialog title="商铺列表" :visible.sync="dialogFormVisibleSp" center width="80%">
				<div style="margin:2% 0;display: inline-block;">
					<span>名称：</span>
					<el-input style="width: 150px;" @keydown.enter.native="userclick" clearable placeholder="请输入名称"
						v-model="nickName2"></el-input>
					<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="userclick">
						查询
					</el-button>
					<el-button style='margin-left:15px;' size="mini" type="primary" plain icon="document"
						@click="userclose">
						重置
					</el-button>
					<el-button style='margin:0 0 20px 20px;' size="mini" type="warning" icon="document"
						@click="shopClcik()" :disabled="checkBoxData.length <= 0">
						确定选择商铺
					</el-button>
				</div>
				<el-table width="780px" v-loading="tableDataLoading4" :data="userIds.list"
					@selection-change="changeFun">
					<el-table-column type="selection">
					</el-table-column>
					<el-table-column label="编号" prop="shopId" width="80" fixed="left">
					</el-table-column>
					<el-table-column label="名称" prop="shopName" fixed="left">
					</el-table-column>
					<el-table-column prop="goodsCover" label="轮播图" width="150">
						<template slot-scope="scope">
							<div v-if="scope.row.shopBanner">
								<div v-for="(item,index) in scope.row.shopBanner.split(',')" :key="index"
									style="display: inline-block; margin: 3px;">
									<el-popover placement="top-start" title="" trigger="hover">
										<img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
										<img style="width: 300px; height: auto" :src="item" alt="">
									</el-popover>
								</div>
							</div>
							<!-- <img :src="scope.row.goodsCover" alt="" width="60" height="60"> -->
						</template>
					</el-table-column>
					<el-table-column label="开店时间" prop="openTime">
					</el-table-column>
					<el-table-column label="闭店时间" prop="closeTime">
					</el-table-column>
					<el-table-column label="营业状态" prop="putawayFlag" fixed="right">
						<template slot-scope="scope">
							<span>{{scope.row.putawayFlag==1?'上班啦':'打烊啦'}}</span>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChangeShop" @current-change="handleCurrentChangeShop"
						:page-sizes="[5, 10, 15, 20]" :page-size="size1" :current-page="page1"
						layout="total,sizes, prev, pager, next,jumper" :total="userIds.totalCount">
					</el-pagination>
				</div>
			</el-dialog>
			<!-- 订单商品列表 -->
			<el-dialog title="订单商品列表" :visible.sync="shopOrderGoods" width="80%">
				<el-table :data="orderGoodsList.list" style="width: 100%">
					<el-table-column prop="id" label="编号" width="80px">
					</el-table-column>
					<el-table-column prop="shopName" label="店铺名称">
					</el-table-column>
					<el-table-column prop="goodsName" label="商品名称">
					</el-table-column>
					<el-table-column prop="goodsCover" label="商品图片" width="120px">
						<template slot-scope="scope">
							<img :src="scope.row.goodsCover" style="width: 100px;height: 100px;" alt="" srcset="">
						</template>
					</el-table-column>
					<el-table-column prop="skuMessage" label="商品规格">
					</el-table-column>
					<el-table-column prop="goodsPrice" label="商品价格">
					</el-table-column>
					<el-table-column prop="goodsPack" label="打包费" width="100"></el-table-column>
					<el-table-column prop="goodsNum" label="商品数量">
						<template slot-scope="scope">
							{{scope.row.goodsNum}}
						</template>
					</el-table-column>
					<el-table-column prop="goodRemarks" label="商品备注">
					</el-table-column>
					<el-table-column prop="goodsPicture" label="操作">
						<template slot-scope="scope">
							<el-button type="warning" @click="oncetk(scope.row)">删除</el-button>
						</template>
					</el-table-column>
				</el-table>
				<span slot="footer" class="dialog-footer">
					<el-button @click="shopOrderGoods = false">取 消</el-button>
					<el-button type="primary" @click="shopOrderGoods = false">确 定</el-button>
				</span>
			</el-dialog>
			<!-- 桌台列表 -->
			<el-dialog title="桌台列表" :visible.sync="dialogFormVisiblezt" center width="80%">
				<el-table width="780px" v-loading="tableDataLoadingU" :data="goodslist">
					<el-table-column fixed prop="goodsId" width="80" label="编号">
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
							<span
								style="color: #f56c6c;">{{scope.row.hourPrice?scope.row.hourPrice:0 | numFilter}}</span>
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
							<span
								style="color: #f56c6c;">{{scope.row.cashPledge?scope.row.cashPledge:0 | numFilter}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="subscription" label="预约价格">
						<template slot-scope="scope">
							<span
								style="color: #f56c6c;">{{scope.row.subscription?scope.row.subscription:0 | numFilter}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="sales" label="充电桩销量">
						<template slot-scope="scope">
							<span>{{scope.row.sales ? scope.row.sales : '0'}}</span>
						</template>
					</el-table-column>
					<el-table-column fixed="right" label="操作" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" v-if="hourPrice==Number(scope.row.hourPrice)"
								@click="confirmZt(scope.row)">确定
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
					<div style="color: red;margin-left: 200px;">* 最大可退款金额为：{{payMoney}}</div>
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
			<!-- 收益详情 -->
			<el-dialog title="收益详情" :visible.sync="shopOrderGoodsSy" width="80%" center>
				<el-table :data="orderGoodsList.list" style="width: 100%">
					<el-table-column fixed prop="ordersId" label="编号" width="80">
					</el-table-column>
					<el-table-column fixed prop="goodsName" label="充电桩名称">
						<template slot-scope="scope">
							<span>{{scope.row.goodsName}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="ordersNo" label="订单号" width="200">
					</el-table-column>
					<el-table-column prop="ordersType" label="订单类型" width="100">
						<template slot-scope="scope">
							<span v-if="scope.row.ordersType==1">开台</span>
							<span v-else-if="scope.row.ordersType==2">-</span>
							<span v-else-if="scope.row.ordersType==3">预约</span>
							<span v-else>-</span>
						</template>
					</el-table-column>
					<el-table-column prop="nickname" label="用户昵称" width="120">
						<template slot-scope="scope">
							<span style="color: #5cc477;cursor: pointer;" v-if="scope.row.userId"
								@click="updatesUser(scope.row.userId)">{{scope.row.payUserName ? scope.row.payUserName : '未绑定'}}</span>
							<span style="color: #f56c6c;" v-else>商家开台</span>
						</template>
					</el-table-column>
					<el-table-column prop="hourPrice" label="每小时价格">
						<template slot-scope="scope">
							<span style="color: deepskyblue;"
								v-if="scope.row.goods">{{scope.row.goods.hourPrice}}元</span>
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
						</template>
					</el-table-column>
					<el-table-column prop="refundMoney" label="已退款金额">
						<template slot-scope="scope">
							<span
								style="color: #f56c6c;cursor: pointer;">{{scope.row.refundMoney?scope.row.refundMoney+'元':'-' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="minuteMoney" label="超出费用">
					</el-table-column>
					<el-table-column prop="cashPledge" label="押金">
					</el-table-column>
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
					<el-table-column prop="payTime" label="付款时间" width="160">
						<template slot-scope="scope">
							<span>{{scope.row.payTime ? scope.row.payTime : '未付款'}}</span>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChangeSy" @current-change="handleCurrentChangeSy"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="orderGoodsList.totalCount">
					</el-pagination>
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
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChangeXf" @current-change="handleCurrentChangeXf"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="sizeU" :current-page="pageU"
						layout="total,sizes, prev, pager, next,jumper" :total="xufeiData.totalPage">
					</el-pagination>
				</div>
			</el-dialog>
			<!-- 修改弹框 -->
			<el-dialog :title="titles" :visible.sync="dialogFormVisibleAxgs" center width="80%">
				<el-form :model="form">
					<el-form-item label="排序：" :label-width="formLabelWidth">
						<el-input-number size="medium" v-model="formwz.sort"></el-input-number>
					</el-form-item>
					<el-form-item label="标题：" :label-width="formLabelWidth">
						<el-input v-model="formwz.welfareWordTitle" style="width:80%;"></el-input>
					</el-form-item>
					<el-form-item label="轮播图：" :label-width="formLabelWidth">
						<div style="display: flex;flex-wrap: wrap;margin-left: 0;">
							<div class="imgs" v-for="(item,index) in formwz.welfareImg" :key="index">
								<img width="100%" class="images" height="100%" :src="item" alt="">
								<span class="dels">
									<i class="el-icon-delete" @click="clear2(index)"></i>
								</span>
							</div>
							<div class="imgs" style="">
								<el-upload :action="$http.adornUrl('alioss/upload')" v-if="formwz.welfareImg.length<3"
									list-type="picture-card" :show-file-list="false" :on-success="handleUploadSuccess4"
									:on-progress="onprogress2">
									<el-progress v-if="percentage2>0 && percentage2<100" type="circle"
										:percentage="percentage2"></el-progress>
									<i v-else class="el-icon-plus"></i>
								</el-upload>
							</div>
						</div>
					</el-form-item>
					<el-form-item label="内容：" :label-width="formLabelWidth">
						<quill-editor ref="myTextEditor" v-model="formwz.welfareWordContent" :options="quillOption"
							style="padding-bottom: 50px;height: 500px;width: 80%;display: inline-table;margin-bottom: 60px;">
						</quill-editor>
					</el-form-item>
				</el-form>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisibleAxgs = false">取 消</el-button>
					<el-button type="primary" @click="amendNoticeTowz()">确 定</el-button>
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
					<!-- <el-table-column prop="hour" label="使用时长">
					</el-table-column> -->
					<el-table-column prop="validDay" label="有效期限(天)">
					</el-table-column>
					<el-table-column prop="teamRule" label="使用规则" width="250">
						<template slot-scope="scope">
							<el-popover placement="top-start" title="" trigger="hover">
								<div style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;"
									slot="reference">
									{{scope.row.teamRule}}
								</div>
								<div style="width: 500px;height: auto;">{{scope.row.teamRule}}</div>
							</el-popover>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChangeYhq" @current-change="handleCurrentChangeYhq"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="size1" :current-page="page1"
						layout="total,sizes, prev, pager, next,jumper" :total="tableDataYhq.totalElements">
					</el-pagination>
				</div>
			</el-dialog>
			<!-- 添加 修改 会员钱包弹框 -->
			<el-dialog :title="titles" :visible.sync="dialogFormVisibleVipW" center width="80%">
				<el-form :model="form">
					<el-form-item label="手机号：" :label-width="formLabelWidth">
						<el-input v-model="phone" style="width:80%;"></el-input>
					</el-form-item>
					<el-form-item label="钱包金额：" :label-width="formLabelWidth">
						<el-input v-model="money" type="number" style="width:80%;"></el-input>
					</el-form-item>

				</el-form>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisibleVipW = false">取 消</el-button>
					<el-button type="primary" @click="amendNoticeToVipW()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 添加 修改 会员钱包明细 弹框 -->
			<el-dialog :title="titles" :visible.sync="dialogFormVisibleVipWmx" center width="80%">
				<div style="margin-bottom: 10px;" v-if="typeIndex==2">
					<span style="width: 200px;display: inline-block;text-align: right;">手机号：</span>
					<el-input style="width:50%;" v-model="phone" @focus="phoneListselect()"
						placeholder="请输入手机号"></el-input>
				</div>
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
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">标题：</span>
					<el-input style="width:50%;" v-model="titleAx" type="text" :controls="false"
						placeholder="请输入标题"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">内容：</span>
					<el-input style="width:50%;" v-model="contentAx" type="text" :controls="false"
						placeholder="请输入内容"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisibleVipWmx = false">取 消</el-button>
					<el-button type="primary" v-if="typeIndex==1" @click="amendNoticeToVipQb()">确 定</el-button>
					<el-button type="primary" v-if="typeIndex==2" @click="amendNoticeToVipWmx()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 会员钱包列表 弹框 -->
			<el-dialog title="会员钱包列表" :visible.sync="dialogFormVisibleHyqb" center width="60%">
				<div style="margin:2% 0;display: inline-block;">
					<span>手机号：</span>
					<el-input style="width: 150px;" @keydown.enter.native="userclick" clearable placeholder="请输入手机号"
						v-model="phone1"></el-input>&nbsp;&nbsp;&nbsp;&nbsp;
					<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="phoneclick">
						查询
					</el-button>
				</div>
				<el-table v-loading="tableDataLoading4" :data="userIds.records">
					<el-table-column prop="memberUserId" label="编号" width="100">
					</el-table-column>
					<el-table-column prop="phone" label="手机号">
					</el-table-column>
					<el-table-column prop="money" label="钱包金额">
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160">
					</el-table-column>
					<el-table-column label="操作" fixed='right'>
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="phoneconfirm(scope.row)">确定
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChangeHyqb" @current-change="handleCurrentChangeHyqb"
						:page-sizes="[5, 10, 15, 20]" :page-size="size1" :current-page="page1"
						layout="total,sizes, prev, pager, next,jumper" :total="userIds.total">
					</el-pagination>
				</div>
			</el-dialog>
			<!-- 音乐库列表 -->
			<el-dialog title="音乐库列表" :visible.sync="dialogFormVisibleM" center width="80%">
				<div style="width: 100%;margin-right: 30px;">
					<div style="margin:5px;display: inline-block;">
						<span>分类名称：</span>
						<el-select v-model="musicTypeIdYyk" style="width:150px;margin-left: 10px;"
							@change="handleClickYyk()">
							<el-option v-for="(item,index) in tableDataType" :key="index" :label="item.musicTypeName"
								:value="item.musicTypeId">
							</el-option>
						</el-select>&nbsp;&nbsp;&nbsp;&nbsp;
					</div>
					<div style="margin:5px;display: inline-block;">
						<span>歌曲名称：</span>
						<el-input style="width: 160px;" @keydown.enter.native="handleClickYyk" clearable
							placeholder="请输入歌曲名称" v-model="musicNameYyk"></el-input>&nbsp;&nbsp;&nbsp;&nbsp;
					</div>
					<div style="margin:5px;display: inline-block;">
						<span>歌曲作者：</span>
						<el-input style="width: 160px;" @keydown.enter.native="handleClickYyk" clearable
							placeholder="请输入歌曲作者" v-model="musicAuthorYyk"></el-input>&nbsp;&nbsp;&nbsp;&nbsp;
					</div>

					<el-button style="margin:10px;" size="mini" type="primary" plain icon="document"
						@click="handleClickYyk()">查询
					</el-button>
					<el-button style="margin:10px;" size="mini" type="primary" plain icon="document"
						@click="clearClickYyk()">重置
					</el-button>
				</div>
				<el-table v-loading="tableDataLoading1" :data="tableDataYyk.records">
					<el-table-column prop="musicLibraryId" label="编号" width="80" fixed='left'>
					</el-table-column>
					<el-table-column prop="tearoomName" label="门店名称">
					</el-table-column>
					<el-table-column prop="musicTypeName" label="分类名称">
					</el-table-column>
					<el-table-column prop="musicName" label="歌曲名称" fixed='left'>
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
					<el-table-column prop="createTime" label="创建时间" width="160">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="170" align="center" fixed='right'>
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="YykAdd(scope.row)">选择
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChangeYyk" @current-change="handleCurrentChangeYyk"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="tableDataYyk.total">
					</el-pagination>
				</div>
			</el-dialog>
			<!-- 修改 员工权限 弹框 -->
			<el-dialog title="设置权限" :visible.sync="dialogFormVisibleYgqx" center width="60%">
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">工作开始时间：</span>
					<!-- <el-time-select v-model="startTime" :picker-options="{
					    start: '00:00',
					    step: '00:01',
					    end: '23:59'
					  }" placeholder="选择开始时间">
					</el-time-select> -->
					<el-time-picker
					    v-model="startTime" format="HH:mm" value-format="HH:mm"
					    :picker-options="{
					      start: '00:00',
					      step: '00:01',
					      end: '23:59'
					    }"
					    placeholder="选择开始时间">
					  </el-time-picker>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">工作结束时间：</span>
					<!-- <el-time-select v-model="endTime" :picker-options="{
					    start: '00:00',
					    step: '00:01',
					    end: '23:59'
					  }" placeholder="选择结束时间">
					</el-time-select> -->
					<el-time-picker  format="HH:mm" value-format="HH:mm"
					    v-model="endTime"
					    :picker-options="{
					      start: '00:00',
					      step: '00:01',
					      end: '23:59'
					    }"
					    placeholder="选择结束时间">
					  </el-time-picker>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">用户状态：</span>
					<el-radio-group v-model="tearoomUserStatus">
						<el-radio :label="1">正常</el-radio>
						<el-radio :label="2">上班</el-radio>
						<el-radio :label="3">下班</el-radio>
					</el-radio-group>
				</div>

				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisibleYgqx = false">取 消</el-button>
					<el-button type="primary" @click="amendNoticeToYg()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 员工开台统计 弹框 -->
			<el-dialog title="员工开台统计" :visible.sync="dialogFormVisibleKt" center width="80%">
				<el-select v-model="way" style="width:150px;margin-left: 10px;" @change="animeDatKtYg">
					<el-option v-for="(item,index) in ways" :key="index" :label="item.label" :value="item.value">
					</el-option>
				</el-select>&nbsp;&nbsp;&nbsp;
				<el-date-picker style="width: 160px;margin-left: 10px;" v-model="info.stockDate" align="right"
					type="datetime" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间"
					@change="animeDatKtYg">
				</el-date-picker>
				<el-row v-loading="tableDataLoading1">
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">总收益</div>
								<div class="text_color">
									<span>{{totalRevenueKtTj.sumMoney?totalRevenueKtTj.sumMoney:0 | numFilter}}</span>元
								</div>
							</div>
						</div>
					</el-col>
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color"> 总订单 </div>
								<div class="text_color">
									<span>{{totalRevenueKtTj.sumCount?totalRevenueKtTj.sumCount:0}}</span>单
								</div>
							</div>
						</div>
					</el-col>
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">微信收益</div>
								<div class="text_color">
									<span>{{totalRevenueKtTj.wxMoney?totalRevenueKtTj.wxMoney:0 | numFilter}}</span>元
								</div>
							</div>
						</div>
					</el-col>
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color"> 微信订单 </div>
								<div class="text_color">
									<span>{{totalRevenueKtTj.wxCount?totalRevenueKtTj.wxCount:0}}</span>单
								</div>
							</div>
						</div>
					</el-col>
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">现金收益</div>
								<div class="text_color">
									<span>{{totalRevenueKtTj.xjMoney?totalRevenueKtTj.xjMoney:0 | numFilter}}</span>元
								</div>
							</div>
						</div>
					</el-col>
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color"> 现金订单 </div>
								<div class="text_color">
									<span>{{totalRevenueKtTj.xjCount?totalRevenueKtTj.xjCount:0}}</span>单
								</div>
							</div>
						</div>
					</el-col><el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">门店会员收益</div>
								<div class="text_color">
									<span>{{totalRevenueKtTj.mdMoney?totalRevenueKtTj.mdMoney:0 | numFilter}}</span>元
								</div>
							</div>
						</div>
					</el-col>
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color"> 门店会员订单 </div>
								<div class="text_color">
									<span>{{totalRevenueKtTj.mdCount?totalRevenueKtTj.mdCount:0}}</span>单
								</div>
							</div>
						</div>
					</el-col><el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">美团核销收益</div>
								<div class="text_color">
									<span>{{totalRevenueKtTj.mtMoney?totalRevenueKtTj.mtMoney:0 | numFilter}}</span>元
								</div>
							</div>
						</div>
					</el-col>
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color"> 美团核销订单 </div>
								<div class="text_color">
									<span>{{totalRevenueKtTj.mtCount?totalRevenueKtTj.mtCount:0}}</span>单
								</div>
							</div>
						</div>
					</el-col>
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">快手核销收益</div>
								<div class="text_color">
									<span>{{totalRevenueKtTj.ksMoney?totalRevenueKtTj.ksMoney:0 | numFilter}}</span>元
								</div>
							</div>
						</div>
					</el-col>
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color"> 快手核销订单 </div>
								<div class="text_color">
									<span>{{totalRevenueKtTj.ksCount?totalRevenueKtTj.ksCount:0}}</span>单
								</div>
							</div>
						</div>
					</el-col>
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">抖音核销收益</div>
								<div class="text_color">
									<span>{{totalRevenueKtTj.dyMoney?totalRevenueKtTj.dyMoney:0 | numFilter}}</span>元
								</div>
							</div>
						</div>
					</el-col>
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color"> 抖音核销订单 </div>
								<div class="text_color">
									<span>{{totalRevenueKtTj.dyCount?totalRevenueKtTj.dyCount:0}}</span>单
								</div>
							</div>
						</div>
					</el-col>
				</el-row>
				</el-dialog>
				<!-- 查看 员工权限 弹框 -->
				<el-dialog title="查看权限" :visible.sync="dialogFormVisibleQx" center width="60%">
					<div class="shop_item" style="text-align:right;margin-right: 10%;">
						<el-button v-if="isbtn" style="margin-bottom: 20px;" size="mini" type="primary"
							:disabled="!isAuth('merchantList:update')" @click="updatesQx">修改
						</el-button>
						<div v-if="!isbtn">
							<el-button style="margin-bottom: 20px;" size="mini" type="info" @click="cancelQx">取消
							</el-button>
							<el-button style="margin-bottom: 20px;" size="mini" type="primary" @click="saveClickQx">保存
							</el-button>
						</div>
					</div>
					
					<div class="content_item">
						<span style="display: inline-block;text-align: right;width: 200px;">待结算统计 ：</span>
						<div class="margin15" style="display: inline-block;">
							<el-radio-group v-if="isshow" disabled v-model="totalRevenueQx.isDaijiesuan">
								<el-radio :label="1">是</el-radio>
								<el-radio :label="2">否</el-radio>
							</el-radio-group>
						</div>
						<div class="margin15" style="display: inline-block;">
							<el-radio-group v-if="!isshow" v-model="totalRevenueQx.isDaijiesuan">
								<el-radio :label="1">是</el-radio>
								<el-radio :label="2">否</el-radio>
							</el-radio-group>
						</div>
					</div>
					<div class="content_item">
						<span style="display: inline-block;text-align: right;width: 200px;">点餐管理 ：</span>
						<div class="margin15" style="display: inline-block;">
							<el-radio-group v-if="isshow" disabled v-model="totalRevenueQx.isDiancanguanli">
								<el-radio :label="1">是</el-radio>
								<el-radio :label="2">否</el-radio>
							</el-radio-group>
						</div>
						<div class="margin15" style="display: inline-block;">
							<el-radio-group v-if="!isshow" v-model="totalRevenueQx.isDiancanguanli">
								<el-radio :label="1">是</el-radio>
								<el-radio :label="2">否</el-radio>
							</el-radio-group>
						</div>
					</div>
					<div class="content_item">
						<span style="display: inline-block;text-align: right;width: 200px;">点歌管理 ：</span>
						<div class="margin15" style="display: inline-block;">
							<el-radio-group v-if="isshow" disabled v-model="totalRevenueQx.isDiange">
								<el-radio :label="1">是</el-radio>
								<el-radio :label="2">否</el-radio>
							</el-radio-group>
						</div>
						<div class="margin15" style="display: inline-block;">
							<el-radio-group v-if="!isshow" v-model="totalRevenueQx.isDiange">
								<el-radio :label="1">是</el-radio>
								<el-radio :label="2">否</el-radio>
							</el-radio-group>
						</div>
					</div>
					<div class="content_item">
						<span style="display: inline-block;text-align: right;width: 200px;">点餐管理 ：</span>
						<div class="margin15" style="display: inline-block;">
							<el-radio-group v-if="isshow" disabled v-model="totalRevenueQx.isDingdanguanli">
								<el-radio :label="1">是</el-radio>
								<el-radio :label="2">否</el-radio>
							</el-radio-group>
						</div>
						<div class="margin15" style="display: inline-block;">
							<el-radio-group v-if="!isshow" v-model="totalRevenueQx.isDingdanguanli">
								<el-radio :label="1">是</el-radio>
								<el-radio :label="2">否</el-radio>
							</el-radio-group>
						</div>
					</div>
					<div class="content_item">
						<span style="display: inline-block;text-align: right;width: 200px;">核销统计 ：</span>
						<div class="margin15" style="display: inline-block;">
							<el-radio-group v-if="isshow" disabled v-model="totalRevenueQx.isHexiaotongji">
								<el-radio :label="1">是</el-radio>
								<el-radio :label="2">否</el-radio>
							</el-radio-group>
						</div>
						<div class="margin15" style="display: inline-block;">
							<el-radio-group v-if="!isshow" v-model="totalRevenueQx.isHexiaotongji">
								<el-radio :label="1">是</el-radio>
								<el-radio :label="2">否</el-radio>
							</el-radio-group>
						</div>
					</div>
					<div class="content_item">
						<span style="display: inline-block;text-align: right;width: 200px;">会员管理 ：</span>
						<div class="margin15" style="display: inline-block;">
							<el-radio-group v-if="isshow" disabled v-model="totalRevenueQx.isHuiyuanguanli">
								<el-radio :label="1">是</el-radio>
								<el-radio :label="2">否</el-radio>
							</el-radio-group>
						</div>
						<div class="margin15" style="display: inline-block;">
							<el-radio-group v-if="!isshow" v-model="totalRevenueQx.isHuiyuanguanli">
								<el-radio :label="1">是</el-radio>
								<el-radio :label="2">否</el-radio>
							</el-radio-group>
						</div>
					</div>
					<div class="content_item">
						<span style="display: inline-block;text-align: right;width: 200px;">会员权益 ：</span>
						<div class="margin15" style="display: inline-block;">
							<el-radio-group v-if="isshow" disabled v-model="totalRevenueQx.isHuiyuanquanyi">
								<el-radio :label="1">是</el-radio>
								<el-radio :label="2">否</el-radio>
							</el-radio-group>
						</div>
						<div class="margin15" style="display: inline-block;">
							<el-radio-group v-if="!isshow" v-model="totalRevenueQx.isHuiyuanquanyi">
								<el-radio :label="1">是</el-radio>
								<el-radio :label="2">否</el-radio>
							</el-radio-group>
						</div>
					</div>
					<div class="content_item">
						<span style="display: inline-block;text-align: right;width: 200px;">活动中心 ：</span>
						<div class="margin15" style="display: inline-block;">
							<el-radio-group v-if="isshow" disabled v-model="totalRevenueQx.isHuodongzhongxin">
								<el-radio :label="1">是</el-radio>
								<el-radio :label="2">否</el-radio>
							</el-radio-group>
						</div>
						<div class="margin15" style="display: inline-block;">
							<el-radio-group v-if="!isshow" v-model="totalRevenueQx.isHuodongzhongxin">
								<el-radio :label="1">是</el-radio>
								<el-radio :label="2">否</el-radio>
							</el-radio-group>
						</div>
					</div>
					<div class="content_item">
						<span style="display: inline-block;text-align: right;width: 200px;">开台收益 ：</span>
						<div class="margin15" style="display: inline-block;">
							<el-radio-group v-if="isshow" disabled v-model="totalRevenueQx.isKaitaishouyi">
								<el-radio :label="1">是</el-radio>
								<el-radio :label="2">否</el-radio>
							</el-radio-group>
						</div>
						<div class="margin15" style="display: inline-block;">
							<el-radio-group v-if="!isshow" v-model="totalRevenueQx.isKaitaishouyi">
								<el-radio :label="1">是</el-radio>
								<el-radio :label="2">否</el-radio>
							</el-radio-group>
						</div>
					</div>
					<div class="content_item">
						<span style="display: inline-block;text-align: right;width: 200px;">门店会员 ：</span>
						<div class="margin15" style="display: inline-block;">
							<el-radio-group v-if="isshow" disabled v-model="totalRevenueQx.isMendianhuiyuan">
								<el-radio :label="1">是</el-radio>
								<el-radio :label="2">否</el-radio>
							</el-radio-group>
						</div>
						<div class="margin15" style="display: inline-block;">
							<el-radio-group v-if="!isshow" v-model="totalRevenueQx.isMendianhuiyuan">
								<el-radio :label="1">是</el-radio>
								<el-radio :label="2">否</el-radio>
							</el-radio-group>
						</div>
					</div>
					<div class="content_item">
						<span style="display: inline-block;text-align: right;width: 200px;">排号 ：</span>
						<div class="margin15" style="display: inline-block;">
							<el-radio-group v-if="isshow" disabled v-model="totalRevenueQx.isPaihao">
								<el-radio :label="1">是</el-radio>
								<el-radio :label="2">否</el-radio>
							</el-radio-group>
						</div>
						<div class="margin15" style="display: inline-block;">
							<el-radio-group v-if="!isshow" v-model="totalRevenueQx.isPaihao">
								<el-radio :label="1">是</el-radio>
								<el-radio :label="2">否</el-radio>
							</el-radio-group>
						</div>
					</div>
					<div class="content_item">
						<span style="display: inline-block;text-align: right;width: 200px;">商家开台 ：</span>
						<div class="margin15" style="display: inline-block;">
							<el-radio-group v-if="isshow" disabled v-model="totalRevenueQx.isShangjiakaitai">
								<el-radio :label="1">是</el-radio>
								<el-radio :label="2">否</el-radio>
							</el-radio-group>
						</div>
						<div class="margin15" style="display: inline-block;">
							<el-radio-group v-if="!isshow" v-model="totalRevenueQx.isShangjiakaitai">
								<el-radio :label="1">是</el-radio>
								<el-radio :label="2">否</el-radio>
							</el-radio-group>
						</div>
					</div>
					<div class="content_item">
						<span style="display: inline-block;text-align: right;width: 200px;">商家开台统计 ：</span>
						<div class="margin15" style="display: inline-block;">
							<el-radio-group v-if="isshow" disabled v-model="totalRevenueQx.isShangjiakaitaitongji">
								<el-radio :label="1">是</el-radio>
								<el-radio :label="2">否</el-radio>
							</el-radio-group>
						</div>
						<div class="margin15" style="display: inline-block;">
							<el-radio-group v-if="!isshow" v-model="totalRevenueQx.isShangjiakaitaitongji">
								<el-radio :label="1">是</el-radio>
								<el-radio :label="2">否</el-radio>
							</el-radio-group>
						</div>
					</div>
					<div class="content_item">
						<span style="display: inline-block;text-align: right;width: 200px;">时长统计 ：</span>
						<div class="margin15" style="display: inline-block;">
							<el-radio-group v-if="isshow" disabled v-model="totalRevenueQx.isShichangtongji">
								<el-radio :label="1">是</el-radio>
								<el-radio :label="2">否</el-radio>
							</el-radio-group>
						</div>
						<div class="margin15" style="display: inline-block;">
							<el-radio-group v-if="!isshow" v-model="totalRevenueQx.isShichangtongji">
								<el-radio :label="1">是</el-radio>
								<el-radio :label="2">否</el-radio>
							</el-radio-group>
						</div>
					</div>
					<div class="content_item">
						<span style="display: inline-block;text-align: right;width: 200px;">实物核销 ：</span>
						<div class="margin15" style="display: inline-block;">
							<el-radio-group v-if="isshow" disabled v-model="totalRevenueQx.isShiwuhexiao">
								<el-radio :label="1">是</el-radio>
								<el-radio :label="2">否</el-radio>
							</el-radio-group>
						</div>
						<div class="margin15" style="display: inline-block;">
							<el-radio-group v-if="!isshow" v-model="totalRevenueQx.isShiwuhexiao">
								<el-radio :label="1">是</el-radio>
								<el-radio :label="2">否</el-radio>
							</el-radio-group>
						</div>
					</div>
					<div class="content_item">
						<span style="display: inline-block;text-align: right;width: 200px;">总收益 ：</span>
						<div class="margin15" style="display: inline-block;">
							<el-radio-group v-if="isshow" disabled v-model="totalRevenueQx.isShouyi">
								<el-radio :label="1">是</el-radio>
								<el-radio :label="2">否</el-radio>
							</el-radio-group>
						</div>
						<div class="margin15" style="display: inline-block;">
							<el-radio-group v-if="!isshow" v-model="totalRevenueQx.isShouyi">
								<el-radio :label="1">是</el-radio>
								<el-radio :label="2">否</el-radio>
							</el-radio-group>
						</div>
					</div>
					<div class="content_item">
						<span style="display: inline-block;text-align: right;width: 200px;">收益明细 ：</span>
						<div class="margin15" style="display: inline-block;">
							<el-radio-group v-if="isshow" disabled v-model="totalRevenueQx.isShouyimingxi">
								<el-radio :label="1">是</el-radio>
								<el-radio :label="2">否</el-radio>
							</el-radio-group>
						</div>
						<div class="margin15" style="display: inline-block;">
							<el-radio-group v-if="!isshow" v-model="totalRevenueQx.isShouyimingxi">
								<el-radio :label="1">是</el-radio>
								<el-radio :label="2">否</el-radio>
							</el-radio-group>
						</div>
					</div>
					<div class="content_item">
						<span style="display: inline-block;text-align: right;width: 200px;">门店状态 ：</span>
						<div class="margin15" style="display: inline-block;">
							<el-radio-group v-if="isshow" disabled v-model="totalRevenueQx.isStatus">
								<el-radio :label="1">是</el-radio>
								<el-radio :label="2">否</el-radio>
							</el-radio-group>
						</div>
						<div class="margin15" style="display: inline-block;">
							<el-radio-group v-if="!isshow" v-model="totalRevenueQx.isStatus">
								<el-radio :label="1">是</el-radio>
								<el-radio :label="2">否</el-radio>
							</el-radio-group>
						</div>
					</div>
					<div class="content_item">
						<span style="display: inline-block;text-align: right;width: 200px;">提现 ：</span>
						<div class="margin15" style="display: inline-block;">
							<el-radio-group v-if="isshow" disabled v-model="totalRevenueQx.isTixian">
								<el-radio :label="1">是</el-radio>
								<el-radio :label="2">否</el-radio>
							</el-radio-group>
						</div>
						<div class="margin15" style="display: inline-block;">
							<el-radio-group v-if="!isshow" v-model="totalRevenueQx.isTixian">
								<el-radio :label="1">是</el-radio>
								<el-radio :label="2">否</el-radio>
							</el-radio-group>
						</div>
					</div>
					<div class="content_item">
						<span style="display: inline-block;text-align: right;width: 200px;">消费排行 ：</span>
						<div class="margin15" style="display: inline-block;">
							<el-radio-group v-if="isshow" disabled v-model="totalRevenueQx.isXiaofeipaihang">
								<el-radio :label="1">是</el-radio>
								<el-radio :label="2">否</el-radio>
							</el-radio-group>
						</div>
						<div class="margin15" style="display: inline-block;">
							<el-radio-group v-if="!isshow" v-model="totalRevenueQx.isXiaofeipaihang">
								<el-radio :label="1">是</el-radio>
								<el-radio :label="2">否</el-radio>
							</el-radio-group>
						</div>
					</div>
					<div class="content_item">
						<span style="display: inline-block;text-align: right;width: 200px;">员工管理 ：</span>
						<div class="margin15" style="display: inline-block;">
							<el-radio-group v-if="isshow" disabled v-model="totalRevenueQx.isYuangongguanli">
								<el-radio :label="1">是</el-radio>
								<el-radio :label="2">否</el-radio>
							</el-radio-group>
						</div>
						<div class="margin15" style="display: inline-block;">
							<el-radio-group v-if="!isshow" v-model="totalRevenueQx.isYuangongguanli">
								<el-radio :label="1">是</el-radio>
								<el-radio :label="2">否</el-radio>
							</el-radio-group>
						</div>
					</div>
					<div class="content_item">
						<span style="display: inline-block;text-align: right;width: 200px;">语音播报 ：</span>
						<div class="margin15" style="display: inline-block;">
							<el-radio-group v-if="isshow" disabled v-model="totalRevenueQx.isYuyin">
								<el-radio :label="1">是</el-radio>
								<el-radio :label="2">否</el-radio>
							</el-radio-group>
						</div>
						<div class="margin15" style="display: inline-block;">
							<el-radio-group v-if="!isshow" v-model="totalRevenueQx.isYuyin">
								<el-radio :label="1">是</el-radio>
								<el-radio :label="2">否</el-radio>
							</el-radio-group>
						</div>
					</div>
					<div class="content_item">
						<span style="display: inline-block;text-align: right;width: 200px;">赠送优惠券 ：</span>
						<div class="margin15" style="display: inline-block;">
							<el-radio-group v-if="isshow" disabled v-model="totalRevenueQx.isZengsongyouhuijuan">
								<el-radio :label="1">是</el-radio>
								<el-radio :label="2">否</el-radio>
							</el-radio-group>
						</div>
						<div class="margin15" style="display: inline-block;">
							<el-radio-group v-if="!isshow" v-model="totalRevenueQx.isZengsongyouhuijuan">
								<el-radio :label="1">是</el-radio>
								<el-radio :label="2">否</el-radio>
							</el-radio-group>
						</div>
					</div>
					<div class="content_item">
						<span style="display: inline-block;text-align: right;width: 200px;">账单对账 ：</span>
						<div class="margin15" style="display: inline-block;">
							<el-radio-group v-if="isshow" disabled v-model="totalRevenueQx.isZhangdantongji">
								<el-radio :label="1">是</el-radio>
								<el-radio :label="2">否</el-radio>
							</el-radio-group>
						</div>
						<div class="margin15" style="display: inline-block;">
							<el-radio-group v-if="!isshow" v-model="totalRevenueQx.isZhangdantongji">
								<el-radio :label="1">是</el-radio>
								<el-radio :label="2">否</el-radio>
							</el-radio-group>
						</div>
					</div>
					<div class="content_item">
						<span style="display: inline-block;text-align: right;width: 200px;">桌台管理 ：</span>
						<div class="margin15" style="display: inline-block;">
							<el-radio-group v-if="isshow" disabled v-model="totalRevenueQx.isZhuotaiguanli">
								<el-radio :label="1">是</el-radio>
								<el-radio :label="2">否</el-radio>
							</el-radio-group>
						</div>
						<div class="margin15" style="display: inline-block;">
							<el-radio-group v-if="!isshow" v-model="totalRevenueQx.isZhuotaiguanli">
								<el-radio :label="1">是</el-radio>
								<el-radio :label="2">否</el-radio>
							</el-radio-group>
						</div>
					</div>
					<div class="content_item">
						<span style="display: inline-block;text-align: right;width: 200px;">桌台数据 ：</span>
						<div class="margin15" style="display: inline-block;">
							<el-radio-group v-if="isshow" disabled v-model="totalRevenueQx.isZhuotaishuju">
								<el-radio :label="1">是</el-radio>
								<el-radio :label="2">否</el-radio>
							</el-radio-group>
						</div>
						<div class="margin15" style="display: inline-block;">
							<el-radio-group v-if="!isshow" v-model="totalRevenueQx.isZhuotaishuju">
								<el-radio :label="1">是</el-radio>
								<el-radio :label="2">否</el-radio>
							</el-radio-group>
						</div>
					</div>
			</el-dialog>
			<!-- 查看 上班记录 弹框 -->
			<el-dialog title="上班记录" :visible.sync="dialogFormVisibleSbjl" center width="80%">
				
				<el-table v-loading="tableDataLoading1" :data="totalRevenueSbjl.records">
					<el-table-column label="编号" prop="tearoomUserRecordId" width="80" >
					</el-table-column>
					<el-table-column prop="userId" label="员工ID" width="80">
					</el-table-column>
					<el-table-column label="上班时间" prop="startTime" width="160">
					</el-table-column>
					<el-table-column label="下班时间" prop="endTime" width="160">
					</el-table-column>
					<el-table-column label="总订单数量" prop="sumCount" width="100">
					</el-table-column>
					<el-table-column label="总订单收益" prop="sumMoney" width="100">
					</el-table-column>
					<el-table-column label="商家抖音收益" prop="dyMoney" width="100">
					</el-table-column>
					<el-table-column label="'商家抖音数量" prop="dyCount" width="100">
					</el-table-column>
					<el-table-column label="商家快手数量" prop="ksCount" width="100">
					</el-table-column>
					<el-table-column label="商家快手收益" prop="ksMoney" width="100">
					</el-table-column>
					<el-table-column label="'商家门店会员订单数量" prop="memberCount" width="100">
					</el-table-column>
					<el-table-column label="商家门店会员订单收益" prop="memberMoney" width="100">
					</el-table-column>
					<el-table-column label="商家美团数量" prop="mtCount" width="100">
					</el-table-column>
					<el-table-column label="商家美团收益" prop="mtMoney" width="100">
					</el-table-column>
					<el-table-column label="商家现金订单数量" prop="rmbCount" width="100">
					</el-table-column>
					<el-table-column label="商家现金订单收益" prop="rmbMoney" width="100">
					</el-table-column>
					<el-table-column label="扫码器抖音数量" prop="smqDyCount" width="100">
					</el-table-column>
					<el-table-column label="扫码器抖音收益" prop="smqDyMoney" width="100">
					</el-table-column>
					<el-table-column label="扫码器快手数量" prop="smqKsCount" width="100">
					</el-table-column>
					<el-table-column label="扫码器快手收益" prop="smqKsMoney" width="100">
					</el-table-column>
					<el-table-column label="扫码器美团数量'" prop="smqMtCount" width="100">
					</el-table-column>
					<el-table-column label="扫码器美团收益" prop="smqMtMoney" width="100">
					</el-table-column>
					<el-table-column label="商家微信订单数量" prop="wxCount" width="100">
					</el-table-column>
					<el-table-column label="商家微信订单收益" prop="wxMoney" width="100">
					</el-table-column>
					<el-table-column label="用户抖音数量" prop="yhDyCount" width="100">
					</el-table-column>
					<el-table-column label="用户抖音收益" prop="yhDyMoney" width="100">
					</el-table-column>
					<el-table-column label="用户快手数量" prop="yhKsCount" width="100">
					</el-table-column>
					<el-table-column label="用户快手收益" prop="yhKsMoney" width="100">
					</el-table-column>
					<el-table-column label="用户门店会员订单数量" prop="yhMemberCount" width="100">
					</el-table-column>
					<el-table-column label="用户门店会员订单收益" prop="yhMemberMoney" width="100">
					</el-table-column>
					<el-table-column label="用户美团数量" prop="yhMtCount" width="100">
					</el-table-column>
					<el-table-column label="用户美团收益" prop="yhMtMoney" width="100">
					</el-table-column>
					<el-table-column label="用户系统券数量" prop="yhSysCount" width="100">
					</el-table-column>
					<el-table-column label="用户系统券收益" prop="yhSysMoney" width="100">
					</el-table-column>
					<el-table-column label="用户微信订单数量" prop="yhWxCount" width="100">
					</el-table-column>
					<el-table-column label="用户微信订单收益" prop="yhWxMoney" width="100">
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChangeShopYg" @current-change="handleCurrentChangeShopYg"
						:page-sizes="[5, 10, 15, 20]" :page-size="size1" :current-page="page1"
						layout="total,sizes, prev, pager, next,jumper" :total="totalRevenueSbjl.total">
					</el-pagination>
				</div>
			</el-dialog>
		</el-tabs>
	</div>
</template>
<script>
	import flvjs from 'flv.js';
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
	const flvPlayer = null
	import {
		quillEditor
	} from 'vue-quill-editor'
	import 'quill/dist/quill.core.css'
	import 'quill/dist/quill.snow.css'
	import 'quill/dist/quill.bubble.css'
	import quillConfig from '../selfShop/quill-config.js'
	export default {
		components: {
			quillEditor
		},
		name: 'FlvPlayer',
		data() {
			return {
				tableDataqqq: {},
				tableDataq: [],
				gridData: [{
						name: "大门开关",
						menusstate: 0,
					},
					{
						name: "充电站开关",
						menusstate: 0,
					},
					{
						name: "电源开关",
						menusstate: 0,
					},
					{
						name: "空调开关",
						menusstate: 0,
					},
					{
						name: '茶几开关',
						menusstate: 0,
					}
				],
				dialogFormVisibleduihuan: false,
				//开关管理
				switchq: {
					gate: '',
					TeaHouse: '',
					source: '',
					airconditioning: '',
					Teatable: '',
					name: '',
					region: '',
					date1: '',
					date2: '',
					delivery: false,
					type: [],
					resource: '',
					desc: ''
				},
				form: {},
				sale: '',
				moneyScope: '',
				isReserve: '',
				isFaceRecognition: '',
				isMusic: 1,
				isWelfare: '', //是否开启爱心活动
				welfareContent: '', //爱心活动介绍
				qunImg: '',
				wxCode: '',
				welfareImg: '',
				isOpen: '',
				isQrCode:2,
				banstatus: 0,
				store_lable: [],
				userEntityList: [],
				employeeUserIds: [],
				shopsList: [],
				shopIds: [],
				bq: '',
				store_lat: '',
				store_lng: '',
				size: 10,
				limit: 10,
				page: 1,
				id: '',
				title: '',
				mobile: '',
				money: '',
				orderNum: '',
				pay_order: '',
				status: 0,
				isshow: true,
				isbtn: true,
				refusedRefund: '',
				logo: '',
				storeName: '',
				tearoomPhone: '',
				openTime: '',
				closeTime: '',
				province: '',
				options: regionData,
				city: '',
				area: '',
				storeAddress: [],
				storeAddre: '请选择城市',
				img: '',
				imgs: [],
				storeId: '',
				isbtn: true,
				merchantId: '',
				shipAddress: '',
				margin_charge: '',
				shopData: {},
				baojieData: {},
				orderData: [],
				typeDatas: {},
				name: '',
				num: 1,
				price: '',
				is_obligatory: 2,
				couponName1: '',
				couponType: '',
				couponId: '',
				lessMoney: 0,
				minMoney: 0,
				validDay: 1,
				sort: 0,
				issueNumber: 1,
				isLimit: 1,
				goodsIds: '',
				userName: '',
				ordersUserName: '',
				orderType: '',
				orderTypes: [{
						value: 0,
						label: '全部'
					},
					{
						value: 1,
						label: '普通订单'
					},
					{
						value: 2,
						label: '拼团订单'
					},
					{
						value: 3,
						label: '秒杀订单'
					},
					{
						value: 4,
						label: '积分订单'
					},
					{
						value: 5,
						label: '虚拟商品订单'
					},
					{
						value: 6,
						label: '商户保证金'
					}
				],
				couponform: {
					couponId: '',
					type: '',
					couponName: '',
					lessMoney: '',
					goodsIds: '',
					goodsImages: '',
					minMoney: '',
					validDay: '',
					sort: '',
					status: '',
					createTime: '',
					storeId: '',
					merchantId: '',
					storeName: ''
				},
				issueNumber: 0,
				title: '',
				goodsId: '',
				content: '',
				type: '',
				recordData: [],
				recordDataDh: {},
				way: 3,
				ways: [{
					value: 1,
					label: '按天查询'
				}, {
					value: 2,
					label: '按月查询'
				}, {
					value: 3,
					label: '按年查询'
				}],
				ways1: [{
					value: 1,
					label: '按天查询'
				}, {
					value: 2,
					label: '按月查询'
				}, ],
				score: '',
				typeId: '',
				value: [],
				status: 0,
				statusmain: [{
						value: 0,
						label: '全部'
					},
					{
						value: 1,
						label: '上架'
					},
					{
						value: 2,
						label: '暂停使用'
					},
					// {
					// 	value: 3,
					// 	label: '清洁中'
					// },
					// {
					// 	value: 4,
					// 	label: '维护中'
					// },
					// {
					// 	value: 5,
					// 	label: '已打烊'
					// }
				],
				statusmain1: [{ // 1待支付 2待使用 预约中 3使用中 4已完成 5已取消 6待评价 7待补费
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
					// },
				],
				formissue: {
					couponId: '',
					couponIssueId: '',
					couponName: '',
					createTime: '',
					endTime: '',
					isLimit: '',
					issueNumber: '',
					remainNumber: '',
					startTime: '',
					status: '',
					type: '',
					createTime: '',
					storeId: '',
					merchantId: '',
					storeName: ''
				},
				openValue: 1,
				closeValue: 0,
				withdrawData: [],
				formLabelWidth: '200px',
				activeName: 'first',
				tableDataLoading: false,
				dialogFormVisible: false,
				dialogFormVisible4: false,
				dialogFormVisible5: false,
				dialogFormVisible6: false,
				dialogFormVisible8: false,
				dialogFormVisible9: false,
				dialogFormVisible10: false,
				couponData: [],
				issueData: [],
				totalMoney: 0,
				info: {
					stockDate: this.getNowTime(), //日期
				},
				info2: {
					stockDate2: this.getNowTime2(), //日期
				},
				infoR: {
					stockDate: this.getNowTime(), //日期
				},
				infoZ: {
					stockDate: this.getNowTime(), //日期
				},
				infoY: {
					stockDate: this.getNowTime(), //日期
				},
				infoN: {
					stockDate: this.getNowTime(), //日期
				},
				startTime: '',
				endTime: '',
				tableDataYe: {},
				income: {}, //充电站收入
				incomeR: {},
				incomeZ: {},
				incomeY: {},
				incomeN: {},
				statusorder: 'all',
				typeClass: 2,
				coupouData: [],
				coupon_id: '',
				goods_list: [],
				goods_ids: [],
				dialogFormVisibleCs: false,
				types: '',
				store_money: 0,
				tableDataQb: {},
				tableDataQbL: {},
				dialogFormVisibleQb: false,
				dialogFormVisibleJs: false,

				couponName: '',

				teamRule: '',
				buyRule: '',
				goodsImages: '',
				integral: '',
				hour: '',
				minHour: '',
				tableDataPj: {},
				scoreType: '',
				scoreTypes: [{
						value: '',
						label: '全部'
					},
					{
						value: 3,
						label: '好评'
					},
					{
						value: 2,
						label: '中评'
					},
					{
						value: 1,
						label: '差评'
					}
				],
				copeType: 2,
				copeMain: [
					// {
					// 	value: 1,
					// 	label: '团购券'
					// },
					{
						value: 2,
						label: '积分兑换券'
					},
					{
						value: 3,
						label: '新人券'
					},
					// {
					// 	value: 4,
					// 	label: '会员券'
					// },

				],
				dialogFormVisibleCssj: false,
				totalRevenue: {},
				orders: {},

				userId: '',
				nickName: '',
				phone1: '',
				nickName1: '', //门店用户
				nickName2: '',
				nickName3: '', //团长用户
				userIds: {}, //用户列表
				size1: 10,
				page1: 1,
				dialogFormVisible3: false,
				dialogFormVisibleYg: false,
				dialogFormVisibleSp: false,
				tableDataLoading4: false,
				tableDataLoadingR: false,
				tableDataLoadingZ: false,
				tableDataLoadingY: false,
				tableDataLoadingN: false,
				dialogVisible: false,
				dialogFormVisible2: false,
				erweimaImg: '',
				erweimaImgName: '',
				checkBoxData: [], //多选框选择的值
				value1: '',
				timeOptionRange: '',
				pickerOptions: {
					disabledDate(time) {
						let secondNum = 60 * 60 * 24 * 7 * 1000;
						if (time) {
							if ((time.getTime() + secondNum) > (Date.now())) {
								return time.getTime() > Date.now() || time.getTime() < (time.getTime() - secondNum);
							}
							return time.getTime() > (time.getTime() + secondNum) || time.getTime() > new Date().getTime()
						}
						// return time.getTime() > new Date().getTime()
					},
					onPick(data) {
						// let secondNum = 60 * 60 * 24 * 7 * 1000;
						data.minDate.getTime()
						console.log('data', data, '----', )
					},
					firstDayOfWeek: 7,
				},
				cashPledge: '',
				hourPrice: '',
				minutePrice: '',
				memberHourPrice: '',
				memberMinutePrice: '',
				electricityPrice: '',
				memberElectricityPrice: '',
				subscription: '',
				keyCode: '',
				yyToken: '',
				yyId: '',
				goodsCategory: 1, //类型 1台球 2棋牌 3其他桌 4柜子
				broadcastContent: '',
				cameraId: '',
				ktsctjData: [],
				dialogFormVisibleJe: false,
				indTy: 0,
				indTixt: '',

				dialogFormVisibleTssj: false,
				dialogFormVisibleTs: false,
				titles: '',
				goodsTimeId: '',
				startTime: '',
				endTime: '',
				hourPrice: '',
				minutePrice: '',
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
				luckyCouponIds: [], //幸运用户奖励优惠券
				luckyContent: '', //幸运用户播报内容
				isRefund: 1, //是否允许操作退款 1是 2否
				isWxPay: 1, //是否开启微信支付 1是 2否
				luckyCouponList: [],


				wifiNo: '',
				rate: '',
				wifiPwd: '',
				indexType: 1,
				dialogFormVisibleZb: false,

				tableData2: {},
				orderNumber: '',
				orderGoodsList: {},
				shopOrderGoods: false,
				shopOrderGoodsSy: false,
				statusT: '',
				taskstates: [{
						label: '全部',
						value: ''
					},
					{
						label: '已下单',
						value: 2
					},

					{
						label: '已完成',
						value: 4
					},
					{
						label: '已取消',
						value: 5
					}
				],
				phone: '',
				tearoomName: '',
				tableDataLoadingU: false,
				dialogFormVisiblezt: false,
				tearoomId: '',
				goodslist: [],
				hourPrice: '',
				qitaData: {},
				ordersId: '',
				dialogFormVisibleVip: false,
				hexiaoCount: {},
				vipCount: {},
				platformTypeT: '',
				platformnum: [{
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
				dialogFormVisibleBftk: false,
				refundMoney: 0,
				payMoney: 0,
				remark: '',
				dateTimes: '',
				tableDatPh: {},
				statusP: '',
				statusListP: [{
					value: '',
					label: '全部'
				}, {
					value: 1,
					label: '排队中'
				}, {
					value: 2,
					label: '已完成'
				}, {
					value: 3,
					label: '已过号'
				}, ],
				createTime: '',
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
				shopType: 1, //店铺类型 1自营 2三方
				tearoomType:1,//充电类型 1电瓶车 2汽车桩
				isVipPrivilege: 1, //是否享受会员权益  1是 2否
				dialogFormVisibleXf: false,
				xufeiData: {},
				sizeU: 10,
				pageU: 1,

				userNameT: '',
				phoneT: '',
				titleT: '',
				contentT: '',
				quillOption: quillConfig,
				dialogFormVisibleAxgs: false,
				formwz: {
					welfareWordId: '',
					welfareWordContent: '',
					welfareWordTitle: '',
					createTime: '',
					sort: 0,
					welfareImg: [],
				},
				typeIndex: 1,
				titleAx: '',
				contentAx: '',
				moneyType: '',
				qbId: '',
				moneyTypeT: '',
				moneyTypeList: [{
					value: '',
					label: '全部'
				}, {
					value: 1,
					label: '爱心钱包'
				}, {
					value: 2,
					label: '已捐款'
				}, ],
				tableDataLoadingA: false,
				percentage2: 0,
				tableDataLoadingY: false,
				tableDataYhq: {},
				typeY: 1,
				statusY: '',
				couponNameY: '',
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
				xzList: [],
				tableDataWallet: {},
				dialogFormVisibleVipW: false,
				memberUserId: '',
				tableDataWalletMx: {},
				dialogFormVisibleVipWmx: false,
				dialogFormVisibleHyqb: false,
				id: '',
				tableDataLoading1: false,
				dialogFormVisibleM: false,
				size1: 10,
				page1: 1,
				tableDataLoading1: false,
				tableDataYyk: {},
				musicTypeIdYyk: '',
				tearoomNameYyk: '',
				musicNameYyk: '',
				musicAuthorYyk: '',
				dialogFormVisibleYgqx: false,
				tableDataYg: {},
				tearoomUserStatus: 1,
				tableDataType: [],
				totalRevenueKtTj: {},
				dialogFormVisibleKt: false,
				categoryListP: [{
					value: '',
					label: '全部'
				}, {
					value: 1,
					label: '商家开台'
				}, {
					value: 2,
					label: '用户开台'
				}, ],
				isShopT: '',
				dialogFormVisibleQx:false,
				totalRevenueQx:{},
				dialogFormVisibleSbjl:false,
				totalRevenueSbjl:{},

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
			// 详情跳转
			updatesUser(userId) {
				if (userId) {
					this.$router.push({
						path: '/userDetail',
						query: {
							userId: userId
						}
					})
				}
			},
			formatDate(row) {
				let date = new Date(row['str_time'] * 1000);
				let Y = date.getFullYear() + '-';
				let M = date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) + '-' : date.getMonth() + 1 + '-';
				let D = date.getDate() < 10 ? '0' + date.getDate() + ' ' : date.getDate() + ' ';
				let h = date.getHours() < 10 ? '0' + date.getHours() + ':' : date.getHours() + ':';
				let m = date.getMinutes() < 10 ? '0' + date.getMinutes() + ':' : date.getMinutes() + ':';
				let s = date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds();
				return Y + M + D + h + m + s;
			},
			formatDate2(row) {
				let date = new Date(row['end_time'] * 1000);
				let Y = date.getFullYear() + '-';
				let M = date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) + '-' : date.getMonth() + 1 + '-';
				let D = date.getDate() < 10 ? '0' + date.getDate() + ' ' : date.getDate() + ' ';
				let h = date.getHours() < 10 ? '0' + date.getHours() + ':' : date.getHours() + ':';
				let m = date.getMinutes() < 10 ? '0' + date.getMinutes() + ':' : date.getMinutes() + ':';
				let s = date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds();
				return Y + M + D + h + m + s;
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
			// 修改状态弹框
			UpdateStatus(row) {
				this.goodsId = row.goodsId
				this.types = row.status
				this.dialogFormVisibleCs = true
			},
			// 上下架
			change() {
				if (this.types === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择状态',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl('goods/updateGoods'),
					method: 'post',
					data: this.$http.adornData({
						'goodsId': this.goodsId,
						'status': this.types
					})
				}).then(({
					data
				}) => {
					if (data && data.code == 0) {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
						})
						this.dialogFormVisibleCs = false
						this.shopSelect()
					} else {
						this.$message({
							message: data.msg,
							type: 'success',
							duration: 1500,
						})
					}
				})
			},
			// 刷新（充电桩管理）
			refresh() {
				this.shopSelect()
			},

			handleSizeChange1(val) {
				this.size = val
				this.shopSelect()
			},
			handleCurrentChange1(val) {
				this.page = val
				this.shopSelect()
			},
			//订单
			handleSizeChange3(val) {
				this.size = val
				this.orderSelect()
			},
			handleCurrentChange3(val) {
				this.page = val
				this.orderSelect()
			},
			//优惠券列表
			handleSizeChange5(val) {
				this.size = val
				this.couponSelect()
			},
			handleCurrentChange5(val) {
				this.page = val
				this.couponSelect()
			},
			//已发布优惠券
			handleSizeChange6(val) {
				this.size = val
				this.published()
			},
			handleCurrentChange6(val) {
				this.page = val
				this.published()
			},
			//优惠券领取记录
			handleSizeChange7(val) {
				this.size = val
				this.record()
			},
			handleCurrentChange7(val) {
				this.page = val
				this.record()
			},
			handleSizeChangeY(val) {
				this.size = val
				this.recordDh()
			},
			handleCurrentChangeY(val) {
				this.page = val
				this.recordDh()
			},
			handleSizeChangeYe(val) {
				this.size = val
				this.dataSelectYe()
			},

			handleCurrentChangeYe(val) {
				this.page = val
				this.dataSelectYe()
			},
			handleSizeChangeYe1(val) {
				this.size = val
				this.shopSelect()
			},
			handleCurrentChangeYe1(val) {
				this.page = val
				this.shopSelect()
			},
			handleSizeChangeYe2(val) {
				this.size = val
				this.qianbaoSelect()
			},
			handleCurrentChangeYe2(val) {
				this.page = val
				this.qianbaoSelect()
			},

			handleCurrentChangeMx1(val) {
				this.page = this.page - 1
				this.shopshouyiSelect()
			},
			handleCurrentChangeMx2(val) {
				this.page = this.page + 1
				this.shopshouyiSelect()
			},
			handleSizeChangePj(val) {
				this.size = val
				this.dataSelectPj()
			},
			handleCurrentChangePj(val) {
				this.page = val
				this.dataSelectPj()
			},
			handleSizeChange2(val) {
				this.size1 = val
				this.userClass()
			},
			handleCurrentChange2(val) {
				this.page1 = val
				this.userClass()
			},
			handleSizeChangeShop(val) {
				this.size1 = val
				this.shopClass()
			},
			handleCurrentChangeShop(val) {
				this.page1 = val
				this.shopClass()
			},
			handleSizeChangeShopYg(val) {
				this.size1 = val
				this.sbjlSelect()
			},
			handleCurrentChangeShopYg(val) {
				this.page1 = val
				this.sbjlSelect()
			},

			handleSizeChange4(val) {
				this.size1 = val
				this.timeClass()
			},
			handleCurrentChange4(val) {
				this.page1 = val
				this.timeClass()
			},
			handleSizeChangeBj(val) {
				this.size = val
				this.baojieSelect()
			},
			handleCurrentChangeBj(val) {
				this.page = val
				this.baojieSelect()
			},
			handleSizeChangeYg(val) {
				this.size1 = val
				this.classSelectYg()
			},
			handleCurrentChangeYg(val) {
				this.page1 = val
				this.classSelectYg()
			},
			// 刷新
			refresh1() {
				this.couponSelect()
			},
			//搜索
			select() {
				console.log(this.value)
				if (this.value.length != 0) {
					this.value = this.value[this.value - 1]
				}
				this.shopSelect()
			},
			// 重置
			cleans() {
				this.title = ''
				this.status = 0
				this.typeId = ''
				this.value = []
				this.shopSelect()
			},
			//搜索
			select1() {
				this.page = 1
				this.size = 10
				this.orderSelect()
			},
			// 重置
			cleans1() {
				this.orderType = ''
				this.orderNum = ''
				this.ordersUserName = ''
				this.statusorder = ''
				this.startTime = ''
				this.endTime = ''
				this.userName = ''
				this.isShopT = ''
				this.orderSelect()
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
			//发布商品
			addShop(index) {
				let storeId = this.$route.query.storeId
				let merchantId = this.$route.query.merchantId
				this.$router.push({
					path: '/shopPublish2',
					query: {
						storeId: storeId,
						merchantId: merchantId,
						goodsCategory: index,
						tearoomType:this.tearoomType
					}
				})
			},
			// 修改商品
			shopUpdates(index, rows) {
				let storeId = this.$route.query.storeId
				let merchantId = this.$route.query.merchantId
				this.$router.push({
					path: '/shopAmend2',
					query: {
						storeId: storeId,
						merchantId: merchantId,
						id: rows.goodsId,
						goodsCategory: this.goodsCategory
					}
				});
			},
			// 日期选择
			animeDat(value) {
				this.moneySelect()
				// let vanumber = value
				// if (vanumber === 3) {
				// 	this.moneySelect()
				// 	this.orderSelect()
				// }
				// if (vanumber === 2) {
				// 	this.moneySelect()
				// 	this.orderSelect()
				// }
				// if (vanumber === 1) {
				// 	this.moneySelect()
				// 	this.orderSelect()
				// }
			},
			timeDate() {
				this.moneySelect()
				// this.orderSelect()
			},
			// 商品评价跳转
			discuss(index, rows) {
				this.$router.push({
					path: '/discuss',
					query: {
						goodsId: rows.id
					}
				});
			},
			handleClick(tab, event) {
				this.page = 1;
				this.size = 10;
				this.copeType = 2
				this.userNameT = ''
				this.phoneT = ''
				this.titleT = ''
				this.contentT = ''
				this.moneyTypeT = ''
				if (tab._props.label == '充电站信息') {
					this.dataSelect()
				}
				if (tab._props.label == '充电站收入') {
					this.moneySelect()
					this.shopSelect()
				}
				if (tab._props.label == '充电桩管理') {
					this.goodsCategory = 2
					this.shopSelect()
				}
				if (tab._props.label == '慢充充电桩管理') {
					this.goodsCategory = 2
					this.shopSelect()
				}
				if (tab._props.label == '快充充电桩管理') {
					this.goodsCategory = 1
					this.shopSelect()
				}
				if (tab._props.label == '柜子管理') {
					this.goodsCategory = 4
					this.shopSelect()
				}

				if (tab._props.label == '充电站订单') {
					this.startTime = ''
					this.endTime = ''
					this.orderType = ''
					this.statusorder = ''
					this.isShopT = ''
					this.orderSelect()
				}
				if (tab._props.label == '充电站订单待退款') {
					this.startTime = ''
					this.endTime = ''
					this.statusorder = 7
					this.ordersUserName = ''
					this.isShopT = ''
					this.orderSelect()
				}
				if (tab._props.label == '套餐配置') {
					this.couponSelect()
				}
				if (tab._props.label == '已发布优惠券') {
					this.published()

				}
				if (tab._props.label == '优惠券领取记录') {
					this.record()
				}
				if (tab._props.label == '余额明细') {
					this.dataSelectYe()
				}
				if (tab._props.label == '验券列表') {
					this.recordDh()
				}

				if (tab._props.label == '充电统计') {
					// this.dataSelect()
					this.kaitaiSelect()
				}
				if (tab._props.label == '充电站统计') {
					// this.dataSelect()
					this.qianbaoSelect()
				}
				if (tab._props.label == '充电站评价') {
					this.dataSelectPj()
				}
				if (tab._props.label == '保洁管理') {
					this.baojieSelect()
				}
				if (tab._props.label == '充电时长统计') {
					this.startTime = ''
					this.endTime = ''
					this.kaitaishichangSelect()
				}
				if (tab._props.label == '商铺订单') {
					this.page = 1
					this.taskdataSelect()
				}
				if (tab._props.label == '核销统计') {
					this.way = 1
					this.dongtaiSelectHx()
				}
				if (tab._props.label == '收益明细') {
					this.page = 1
					this.way = 1
					this.shopshouyiSelect()
				}
				if (tab._props.label == '排号列表') {
					this.page = 1
					this.statusP = 1
					this.goodsCategory = ''
					this.createTime = this.info.stockDate
					this.paihaoSelect()
				}
				if (tab._props.label == '爱心钱包') {
					this.way = 1
					this.dongtaiSelectAxqb()
					this.aixinpaihaoSelect()
				}
				if (tab._props.label == '爱心公示') {
					this.titleT = ''
					this.page = 1
					this.way = 1
					this.aixingongshiSelect()
				}
				if (tab._props.label == '会员钱包') {
					this.titleT = ''
					this.page = 1
					this.dongtaiSelectVip()
					this.vipWalletSelect()
				}
				if (tab._props.label == '会员钱包明细') {
					this.titleT = ''
					this.page = 1
					this.vipWalletSelectMx()
				}
				if (tab._props.label == '收藏歌单') {
					this.titleT = ''
					this.page = 1
					this.musicPlaytSelect()
				}
				if (tab._props.label == '员工管理') {
					this.titleT = ''
					this.page = 1
					this.classSelectYg()
				}
				if (tab._props.label == '价格配置') {
					this.titleT = ''
					this.page1 = 1
					this.timeClass()
				}
				





			},
			// 收入金额统计
			moneySelect() {
				let storeId = this.$route.query.storeId
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('orders/selectTearoomOrdersIncome'),
					method: 'get',
					params: this.$http.adornParams({
						'time': this.info.stockDate,
						'tearoomId': storeId,
						'flag': this.way
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.income = returnData
				})
			},
			// 获取充电站详情数据
			dataSelect() {
				let storeId = this.$route.query.storeId
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('tearoom/selectTearoomById'),
					method: 'get',
					params: this.$http.adornParams({
						'tearoomId': storeId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.logo = returnData.tearoomTitleImage
					this.storeName = returnData.tearoomName
					this.tearoomPhone = returnData.tearoomPhone
					this.nickName1 = returnData.userName
					this.userId = returnData.userId
					this.storeAddress = []
					this.storeAddre = returnData.tearoomProvince + '/' + returnData.tearoomCity + '/' + returnData
						.tearoomDistrict
					this.province = returnData.tearoomProvince
					this.city = returnData.tearoomCity
					this.area = returnData.tearoomDistrict
					this.shipAddress = returnData.tearoomAddress
					// this.margin_charge = returnData.margin_charge
					this.score = returnData.score
					// this.merchantId = returnData.merchantId
					this.banstatus = returnData.status

					if (returnData.sales) {
						this.sale = returnData.sales
					} else {
						this.sale = 0
					}
					if (returnData.moneyScope) {
						this.moneyScope = returnData.moneyScope
					} else {
						this.moneyScope = ''
					}
					if (returnData.isReserve) {
						this.isReserve = returnData.isReserve
					} else {
						this.isReserve = 2
					}
					if (returnData.startTime) {
						this.openTime = returnData.startTime
						this.closeTime = returnData.endTime
					} else {
						this.openTime = ''
						this.closeTime = ''
					}
					if (returnData.isWelfare) {
						this.isWelfare = returnData.isWelfare
					} else {
						this.isWelfare = 2
					}
					if (returnData.welfareContent) {
						this.welfareContent = returnData.welfareContent
					} else {
						this.welfareContent = ''
					}
					if (returnData.isFaceRecognition) {
						this.isFaceRecognition = returnData.isFaceRecognition
					} else {
						this.isFaceRecognition = 2
					}
					if (returnData.isMusic) {
						this.isMusic = returnData.isMusic
					} else {
						this.isMusic = 2
					}

					this.store_lat = returnData.tearoomLatitude
					this.store_lng = returnData.tearoomLongitude
					this.storeId = returnData.tearoomId
					if (returnData.tearoomCoverImage) {
						let imgs = returnData.tearoomCoverImage.split(',')
						this.imgs = imgs;
					} else {
						this.img = returnData.tearoomCoverImage;
					}
					if (returnData.tearoomLabel) {
						this.store_lable = returnData.tearoomLabel.split(',')
					} else {
						this.store_lable = []
					}
					if (returnData.userEntityList) {
						this.userEntityList = returnData.userEntityList
					} else {
						this.userEntityList = []
					}
					if (returnData.employeeUserIds) {
						this.employeeUserIds = returnData.employeeUserIds.split(',')
					} else {
						this.employeeUserIds = []
					}
					if (returnData.goodsShopList) {
						this.shopsList = returnData.goodsShopList
					} else {
						this.shopsList = []
					}
					if (returnData.shopIds) {
						this.shopIds = returnData.shopIds.split(',')
					} else {
						this.shopIds = []
					}
					if (returnData.cashPledge) {
						this.cashPledge = returnData.cashPledge
					} else {
						this.cashPledge = ''
					}
					if (returnData.hourPrice) {
						this.hourPrice = returnData.hourPrice
					} else {
						this.hourPrice = ''
					}
					if (returnData.minutePrice) {
						this.minutePrice = returnData.minutePrice
					} else {
						this.minutePrice = ''
					}
					if (returnData.subscription) {
						this.subscription = returnData.subscription
					} else {
						this.subscription = ''
					}
					if (returnData.keyCode) {
						this.keyCode = returnData.keyCode
					} else {
						this.keyCode = ''
					}
					if (returnData.yyToken) {
						this.yyToken = returnData.yyToken
					} else {
						this.yyToken = ''
					}
					if (returnData.yyId) {
						this.yyId = returnData.yyId
					} else {
						this.yyId = ''
					}
					if (returnData.broadcastContent) {
						this.broadcastContent = returnData.broadcastContent
					} else {
						this.broadcastContent = ''
					}
					if (returnData.cameraId) {
						this.cameraId = returnData.cameraId
					} else {
						this.cameraId = ''
					}

					if (returnData.wifiNo) {
						this.wifiNo = returnData.wifiNo
					} else {
						this.wifiNo = ''
					}
					if (returnData.wifiPwd) {
						this.wifiPwd = returnData.wifiPwd
					} else {
						this.wifiPwd = ''
					}
					if (returnData.shopPoiId) {
						this.shopPoiId = returnData.shopPoiId
					} else {
						this.shopPoiId = ''
					}

					if (returnData.ksAppId) {
						this.ksAppId = returnData.ksAppId
					} else {
						this.ksAppId = ''
					}
					if (returnData.ksAppSecret) {
						this.ksAppSecret = returnData.ksAppSecret
					} else {
						this.ksAppSecret = ''
					}
					if (returnData.mtEmail) {
						this.mtEmail = returnData.mtEmail
					} else {
						this.mtEmail = ''
					}
					if (returnData.mtPassword) {
						this.mtPassword = returnData.mtPassword
					} else {
						this.mtPassword = ''
					}
					if (returnData.mtStoreId) {
						this.mtStoreId = returnData.mtStoreId
					} else {
						this.mtStoreId = ''
					}
					if (returnData.dyPoiId) {
						this.dyPoiId = returnData.dyPoiId
					} else {
						this.dyPoiId = ''
					}
					if (returnData.dyAppId) {
						this.dyAppId = returnData.dyAppId
					} else {
						this.dyAppId = ''
					}
					if (returnData.dySecret) {
						this.dySecret = returnData.dySecret
					} else {
						this.dySecret = ''
					}

					if (returnData.rate) {
						this.rate = returnData.rate
					} else {
						this.rate = ''
					}
					if (returnData.qunImg) {
						this.qunImg = returnData.qunImg
					} else {
						this.qunImg = ''
					}
					if (returnData.wxCode) {
						this.wxCode = returnData.wxCode
					} else {
						this.wxCode = ''
					}
					if (returnData.welfareImg) {
						this.welfareImg = returnData.welfareImg
					} else {
						this.welfareImg = ''
					}

					if (returnData.isOpen) {
						this.isOpen = returnData.isOpen
					} else {
						this.isOpen = 1
					}
					if (returnData.isQrCode) {
						this.isQrCode = returnData.isQrCode
					} else {
						this.isQrCode = 2
					}
					
					if (returnData.shopType) {
						this.shopType = returnData.shopType
					} else {
						this.shopType = 1
					}
					if (returnData.tearoomType) {
						this.tearoomType = returnData.tearoomType
					} else {
						this.tearoomType = 1
					}
					
					if (returnData.isVipPrivilege) {
						this.isVipPrivilege = returnData.isVipPrivilege
					} else {
						this.isVipPrivilege = 2
					}


					if (returnData.couponIssueList) {
						this.luckyCouponList = returnData.couponIssueList
					} else {
						this.luckyCouponList = []
					}

					if (returnData.luckyCouponIds) {
						this.luckyCouponIds = returnData.luckyCouponIds.split(',')
					} else {
						this.luckyCouponIds = []
					}
					if (returnData.luckySort) {
						this.luckySort = returnData.luckySort
					} else {
						this.luckySort = ''
					}
					if (returnData.luckyContent) {
						this.luckyContent = returnData.luckyContent
					} else {
						this.luckyContent = ''
					}
					if (returnData.isRefund) {
						this.isRefund = returnData.isRefund
					} else {
						this.isRefund = 2
					}
					if (returnData.isWxPay) {
						this.isWxPay = returnData.isWxPay
					} else {
						this.isWxPay = 2
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
					this.store_lable.push(this.bq)
					this.bq = ''
				}
			},
			// 删除标签
			bqClose(index) {
				this.store_lable.splice(index, 1);
			},
			//上传成功
			handleAvatarSuccess(file, fileList) {
				console.log(file)
				this.logo = file.data
			},
			handleAvatarSuccess2(file, fileList) {
				this.qunImg = file.data
			},
			handleAvatarSuccess3(file, fileList) {
				this.wxCode = file.data
			},
			handleAvatarSuccess4(file, fileList) {
				this.welfareImg = file.data
			},

			selectDz1() {
				event.stopPropagation();
				this.qunImg = ''
			},
			selectDz2() {
				event.stopPropagation();
				this.wxCode = ''
			},
			selectDz3() {
				event.stopPropagation();
				this.welfareImg = ''
			},
			
			//上传成功
			handleUploadSuccess(file, fileList) {
				this.img = file.data
				this.imgs.push(this.img)
			},
			handleChange(e) {
				console.log(e)
				// this.hideUpload = fileList.length >= this.limit;
			},
			// 删除
			clear(index) {
				this.imgs.splice(index, 1);
			},
			// 详情跳转
			updatesUser(userId) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: userId
					}
				})
			},
			// 编辑
			updates() {
				this.isshow = false
				this.isbtn = false
			},
			// 取消
			cancel() {
				this.isshow = true
				this.isbtn = true
				this.dataSelect()
			},
			// 获取省市区
			handleChange55(value) {
				// console.log(this.$refs['cascaderAddr'].getCheckedNodes())
				console.log(this.$refs['cascaderAddr'].currentLabels)
				var value1 = this.$refs['cascaderAddr'].currentLabels
				// var value1 = this.$refs['cascaderAddr'].getCheckedNodes()
				if (value1) {
					var val2 = value1
					// var val2 = value1[0].pathLabels
					console.log('val2', val2)
					this.province = val2[0]
					this.city = val2[1]
					this.area = val2[2]

					if (this.city == '市辖区') {
						this.city = this.province
					}
				}
			},
			// 地图定位
			selectDz() {
				console.log(this.province + this.city + this.area + this.shipAddress)
				var addr = this.province + this.city + this.area + this.shipAddress //
				let that = this
				jsonp('https://apis.map.qq.com/ws/geocoder/v1/?address==' + addr +
					'&key=ZBABZ-ZWECU-UQTVV-4LYDR-COK3F-5SF75&get_poi=1&output=jsonp', {
						myCustomUrlParam: 'veryNice'
					}).then(response => {
					// handle success
					console.log('response', response)
					that.store_lng = response.result.location.lng; // 经度
					that.store_lat = response.result.location.lat; // 纬度
					// that.address = response.result.title
					// that.setMap()
				}).catch(error => {
					// handle error
				}).then(() => {
					// always executed
				});
			},
			// 保存
			saveClick() {
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
				// if (this.subscription === '') {
				// 	this.$notify({
				// 		message: ' 预约价格不能为空',
				// 		type: 'warning',
				// 		duration: 1500
				// 	})
				// 	return
				// }
				// if (this.keyCode === '') {
				// 	this.$notify({
				// 		message: '设备标识码不能为空',
				// 		type: 'warning',
				// 		duration: 1500
				// 	})
				// 	return
				// }
				// if (this.yyToken === '') {
				// 	this.$notify({
				// 		message: '语音播报token不能为空',
				// 		type: 'warning',
				// 		duration: 1500
				// 	})
				// 	return
				// }
				// if (this.yyId === '') {
				// 	this.$notify({
				// 		message: '语音播报ID不能为空',
				// 		type: 'warning',
				// 		duration: 1500
				// 	})
				// 	return
				// }

				if (this.rate === '' || this.rate == null) {
					this.$notify({
						message: '佣金比例不能为空',
						type: 'warning',
						duration: 1500
					})
					return
				}

				var titmes = this.openTime + '-' + this.closeTime
				this.$http({
					url: this.$http.adornUrl('tearoom/updateTearoom'),
					method: 'post',
					data: this.$http.adornParams({
						'tearoomId': this.storeId,
						'tearoomTitleImage': this.logo,
						'tearoomCoverImage': this.imgs.toString(),
						'tearoomName': this.storeName,
						'tearoomPhone': this.tearoomPhone,
						'tearoomAddress': this.shipAddress,
						'tearoomLongitude': this.store_lng,
						'tearoomLatitude': this.store_lat,
						'startTime': this.openTime,
						'endTime': this.closeTime,
						'tearoomLabel': this.store_lable.toString(),
						'tearoomProvince': this.province,
						'tearoomCity': this.city,
						'tearoomDistrict': this.area,
						'score': this.score,
						'status': this.banstatus,
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
						'employeeUserIds': this.employeeUserIds.toString(),
						'isReserve': this.isReserve,
						'rate': this.rate,
						'shopIds': this.shopIds.toString(),
						'broadcastContent': this.broadcastContent,
						'cameraId': this.cameraId,
						'qunImg': this.qunImg,
						'wxCode': this.wxCode,
						'isOpen': this.isOpen,
						'isQrCode':this.isQrCode,
						'shopType': this.shopType,
						'tearoomType':this.tearoomType,
						'isVipPrivilege': this.isVipPrivilege,
						'shopPoiId': this.shopPoiId,
						'isWelfare': this.isWelfare, //是否开启爱心活动
						'welfareContent': this.welfareContent, //爱心活动介绍
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
						'luckySort': this.luckySort,
						'luckyCouponIds': this.luckyCouponIds.toString(),
						'luckyContent': this.luckyContent,
						'isRefund': this.isRefund,
						'isWxPay': this.isWxPay,

					})
				}).then(({
					data
				}) => {
					this.$message({
						message: '修改成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.isshow = true
							this.isbtn = true
							this.dataSelect()
						}
					})
				})
			},
			// 获取充电站数据
			shopSelect() {
				let storeId = this.$route.query.storeId
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('goods/selectGoodsList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.size,
						'goodsName': this.title,
						'goodsCategory': this.goodsCategory,
						'status': this.status,
						'tearoomId': storeId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.shopData = returnData
					this.goods_list = returnData.list
					this.tableDataqqq = returnData
				})
			},
			//更改状态
			merchClick(value) {
				let storeId = this.$route.query.storeId
				this.$http({
					url: this.$http.adornUrl('admin/storecomplain/status_store'),
					method: 'post',
					params: this.$http.adornParams({
						'store_id': storeId,
						'status': value
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
			},
			//验劵列表
			yjSelectYe() {
				let storeId = this.$route.query.storeId
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/coupon/third_coupon_list'),
					method: 'post',
					params: this.$http.adornParams({
						'page': this.page,
						'num': this.size,
						'type': 'all',
						'store_id': storeId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.orderData = returnData
				})
			},
			// 获取订单数据
			orderSelect() {
				let storeId = this.$route.query.storeId
				this.totalMoney = 0
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('orders/selectOrdersList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.size,
						'phone': this.orderType,
						'userName': this.userName,
						'goodsName': this.ordersUserName,
						'ordersNo': this.orderNum,
						'status': this.statusorder,
						'startTime': this.startTime,
						'endTime': this.endTime,
						'tearoomId': storeId,
						'isShop': this.isShopT,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					for (var i in data.data.data) {
						if (data.data.data[i].payment) {
							this.totalMoney = this.totalMoney + data.data.data[i].payment * 1
							// console.log(this.totalMoney)
						}
					}
					let returnData = data.data;
					this.orderData = returnData
				})
			},
			//删除充电站
			deleteq(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('goods/deleteGoodsById'),
						method: 'post',
						params: this.$http.adornParams({
							'goodsId': row.goodsId
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
									this.shopSelect()
								}
							})
						} else {
							this.$message({
								message: data.msg,
								type: 'warning',
								duration: 1500,
								onClose: () => {
									this.shopSelect()
								}
							})
						}

					})
				}).catch(() => {})
			},
			//获取套餐数据
			couponSelect() {
				let storeId = this.$route.query.storeId
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('product/selectProductPage'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.size,
						'productName': '',
						'tearoomId': storeId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.couponData = returnData
				})
			},
			// 添加优惠券弹框
			addCoupon() {
				this.type = 2
				this.status = 1
				this.couponName = ''
				this.lessMoney = ''
				this.validDay = ''
				this.minMoney = ''
				this.sort = 0
				this.dialogFormVisible5 = true
			},
			// 添加优惠券
			couponNoticeTo() {
				let storeId = this.$route.query.storeId
				let merchantId = this.$route.query.merchantId
				let storeName = this.$route.query.storeName
				if (this.couponName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入套餐名称',
						type: 'warning'
					});
					return
				}
				if (Number(this.lessMoney) === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入套餐现价',
						type: 'warning'
					});
					return
				}
				if (Number(this.minMoney) === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入套餐原价',
						type: 'warning'
					});
					return
				}
				// if (Number(this.validDay) === '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请输入使用时长',
				// 		type: 'warning'
				// 	});
				// 	return
				// }
				this.$http({
					url: this.$http.adornUrl('product/insertProduct'),
					method: 'post',
					data: this.$http.adornData({
						'productName': this.couponName,
						'productPrice': this.lessMoney,
						'productOldPrice': this.minMoney,
						'tearoomId': storeId,
						'productNum': this.validDay,
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
								this.dialogFormVisible5 = false
								this.couponSelect()
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
			// 修改弹框
			couponUpdates(index, row) {
				this.dialogFormVisible6 = true
				this.couponform.couponId = row.productId
				this.couponform.couponName = row.productName
				this.couponform.lessMoney = row.productPrice
				this.couponform.minMoney = row.productOldPrice
				this.couponform.validDay = row.productNum
				this.couponform.storeId = row.tearoomId
			},
			// 修改
			couponNoticeTo2() {
				if (this.couponform.couponName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入套餐名称',
						type: 'warning'
					});
					return
				}
				if (Number(this.couponform.lessMoney) === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '套餐现价不能为空',
						type: 'warning'
					});
					return
				}
				if (Number(this.couponform.minMoney) === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '套餐原价不能为空',
						type: 'warning'
					});
					return
				}
				// if (Number(this.couponform.validDay) === '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '使用时长不能为空',
				// 		type: 'warning'
				// 	});
				// 	return
				// }
				this.$http({
					url: this.$http.adornUrl('product/updateProduct'),
					method: 'post',
					data: this.$http.adornData({
						'productId': this.couponform.couponId,
						'productName': this.couponform.couponName,
						'productPrice': this.couponform.lessMoney,
						'productOldPrice': this.couponform.minMoney,
						'productNum': this.couponform.validDay,
						'tearoomId': this.couponform.storeId,
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
								this.dialogFormVisible6 = false
								this.couponSelect()
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
			// 删除套餐
			couponDeletes(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('product/deleteProductById'),
						method: 'post',
						params: this.$http.adornParams({
							'productId': row.productId
						})
					}).then(({
						data
					}) => {
						if (data && data.code == 0) {
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.couponSelect()
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
			// 发布优惠券弹框
			issue() {

				this.status = 1
				this.goods_ids = []
				this.couponId = ''
				this.couponName1 = ''
				this.goodsIds = ''
				this.typeClass = 2
				this.couponType = ''
				this.teamRule = ''
				this.buyRule = ''
				this.issueNumber = 0

				this.isLimit = 1
				this.dataSelectCoupon()
				if( this.tearoomType==1){
					this.goodsCategory = 2
				}
				this.shopSelect()
				this.dialogFormVisible8 = true
			},
			// 发布优惠券
			issueNoticeTo() {
				let storeId = this.$route.query.storeId
				let merchantId = this.$route.query.merchantId
				let storeName = this.$route.query.storeName
				if (this.couponId === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择优惠券',
						type: 'warning'
					});
					return
				}
				if (this.isLimit == 1) {
					if (this.issueNumber === '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入发布数量',
							type: 'warning'
						});
						return
					}
				}

				if (this.goods_ids.length == 0) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择充电站',
						type: 'warning'
					});
					return
				}

				var remainNumber = this.issueNumber
				if (this.teamRule == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入使用规则',
						type: 'warning'
					});
					return
				}
				if (this.buyRule == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入购买须知',
						type: 'warning'
					});
					return
				}


				var remainNumber = this.issueNumber
				this.$http({ // /admin/storeorder/store_release_coupon
					url: this.$http.adornUrl('selfCouponIssue/save'),
					method: 'post',
					data: this.$http.adornData({
						'couponId': this.couponId,
						'tearoomId': storeId,
						'goodsIds': this.goods_ids.toString(),
						'status': this.status,
						'type': this.typeClass,
						'teamRule': this.teamRule,
						'buyRule': this.buyRule,
						'issueNumber': this.issueNumber,
						'remainNumber': remainNumber,
						'hour': this.hour,
						'couponName': this.couponName1,
						'integral': this.integral,
						'goodsImages': this.goodsImages,
						'validDay': this.validDay,
						'isLimit': this.isLimit,
						'couponType': this.couponType,
						'minHour': this.minHour,

					})

				}).then(({
					data
				}) => {
					if (data.status == 0) {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.status = 1
								this.dialogFormVisible8 = false
								this.published()
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
			copeTypeBtn() {
				this.page = 1
				this.published()
			},
			// 已发布优惠券
			published() {
				let storeId = this.$route.query.storeId
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('selfCouponIssue/list'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page - 1,
						'size': this.size,
						'couponName': '',
						'tearoomName': '',
						'type': this.copeType,
						'tearoomId': storeId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.issueData = returnData
				})
			},
			// 删除优惠券
			shopdeletes(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('selfCouponIssue/delete'),
						method: 'get',
						params: this.$http.adornParams({
							'id': row.couponIssueId
						})
					}).then(({
						data
					}) => {
						if (data && data.status == 0) {
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.published()
								}
							})
						} else {
							this.$message({
								message: data.msg,
								type: 'error',
								duration: 1500,
								onClose: () => {
									this.published()
								}
							})
						}
					})
				}).catch(() => {})
			},
			// 修改已发布优惠券
			amendcoupon(index, row) {
				console.log(row)
				this.dialogFormVisible9 = true
				this.formissue.couponId = row.coupon_id
				this.formissue.couponIssueId = row.coupon_issue_id
				this.formissue.couponName = row.coupon_name
				this.formissue.createTime = row.create_time
				this.formissue.storeId = row.store_id
				this.formissue.storeName = row.store_name
				this.formissue.endTime = row.end_time
				this.formissue.isLimit = row.is_limit
				this.formissue.issueNumber = row.issue_number
				this.formissue.remainNumber = row.remain_number
				this.formissue.startTime = row.start_time
				this.formissue.status = row.status
				this.formissue.type = row.type
			},
			// 修改已发布优惠券
			amendNoticeTo() {
				if (this.formissue.type == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择状态',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl('admin/coupon/edit_fabu_coupon'),
					method: 'post',
					data: this.$http.adornData({
						// 'couponId': this.formissue.couponId,
						'fabu_coupon_id': this.formissue.couponIssueId,
						// 'startTime': this.formissue.startTime,
						// 'couponName': this.formissue.couponName,
						// 'storeId': this.formissue.storeId,
						// 'merchantId': this.formissue.merchantId,
						// 'storeName': this.formissue.storeName,
						// 'endTime': this.formissue.endTime,
						// 'issueNumber': this.formissue.issueNumber,
						// 'remainNumber': this.formissue.issueNumber,
						'is_limit': this.formissue.isLimit,
						'status': this.formissue.status,
						// 'type': this.formissue.type,
						// 'createTime': this.formissue.createTime
					})
				}).then(({
					data
				}) => {
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.dialogFormVisible9 = false
							this.published()
						}
					})
				})
			},
			// 查询
			select3() {
				this.page = 1
				this.record()
			},
			// 重置
			cleans3() {
				this.page = 1
				this.couponName = ''
				this.platformTypeT = ''
				this.record()
			},
			//获取已发布优惠券数据
			record() {
				let storeId = this.$route.query.storeId
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('selfCouponUser/list'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page - 1,
						'size': this.size,
						'couponName': this.couponName,
						'type': this.copeType,
						'nickName': '',
						'tearoomId': storeId,
						'platformType': this.platformTypeT,

					})

				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.recordData = returnData
				})
			},
			// 查询
			selectDh() {
				this.recordDh()
			},
			// 重置
			cleansDh() {
				this.couponName = ''
				this.recordDh()
			},
			recordDh() {
				let storeId = this.$route.query.storeId
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('message/selectMessageByType'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.size,
						'state': 100,
						'tearoomId': storeId
					})

				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.recordDataDh = returnData
				})
			},

			// 导出
			exportBtn() {
				// if (this.endTime == '') {
				// 	this.endTime = this.info.stockDate
				// }
				// if (this.startTime == '') {
				// 	this.startTime = this.info2.stockDate2
				// }
				var endTime = this.endTime
				if (this.endTime != '') {
					endTime = this.endTime + " 23:59:59"
				}
				let storeId = this.$route.query.storeId
				this.$http({
					url: this.$http.adornUrl('orders/listExcel'),
					method: 'get',
					responseType: 'blob',
					params: this.$http.adornParams({
						'orderNum': this.orderNum,
						'status': this.status,
						'storeId': storeId,
						'orderType': this.orderType,
						'startTime': this.startTime,
						'endTime': endTime
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
			exportBtnH() {
				let storeId = this.$route.query.storeId
				var endTime = this.endTime
				if (this.endTime != '') {
					endTime = this.endTime + " 23:59:59"
				}
				this.$http({
					url: this.$http.adornUrl('orders/listExcel'),
					method: 'get',
					responseType: 'blob',
					params: this.$http.adornParams({
						'orderNum': this.orderNum,
						'status': this.status,
						'title': this.title,
						'storeId': storeId,
						'mobile': this.mobile,
						'isExpress': this.isExpress,
						'orderType': this.orderType,
						'startTime': this.startTime,
						'endTime': endTime
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
			// 余额明细
			dataSelectYe() {
				let storeId = this.$route.query.storeId
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/storecomplain/store_money_wallet'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'num': this.size,
						'store_id': storeId,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableDataYe = returnData
				})
			},
			// 优惠券上下架
			changeYhq(id, type) {
				let types = type
				this.$http({
					url: this.$http.adornUrl('admin/coupon/edit_fabu_coupon'),
					method: 'post',
					params: this.$http.adornParams({
						'fabu_coupon_id': id,
						'status': types
					})
				}).then(({
					data
				}) => {
					if (data && data.code == 0) {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
						})
						this.published()
					} else {
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
						})
						this.published()
					}
				})
			},
			// 获取优惠券
			dataSelectCoupon() {
				this.$http({
					url: this.$http.adornUrl('selfCoupon/list'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page - 1,
						'size': this.size,
						'type': this.typeClass,
						'status': '',
						'couponName': ''
					})
					// url: this.$http.adornUrl('admin/coupon/coupon_list'),
					// method: 'post',
					// params: this.$http.adornParams({
					// 	'page': 1,
					// 	'num': 100,
					// 	'type': this.typeClass,
					// 	'status': '',
					// 	'coupon_name': ''
					// })
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.coupouData = returnData
				})
			},
			shopClick() {
				for (var i in this.coupouData.content) {
					if (this.couponId == this.coupouData.content[i].couponId) {
						this.couponName1 = this.coupouData.content[i].couponName
						this.goodsImages = this.coupouData.content[i].goodsImages
						this.lessMoney = this.coupouData.content[i].lessMoney
						this.validDay = this.coupouData.content[i].validDay
						this.hour = this.coupouData.content[i].hour
						this.minMoney = this.coupouData.content[i].minMoney
						this.integral = this.coupouData.content[i].integral
						this.couponType = this.coupouData.content[i].couponType
						this.minHour = this.couponData[i].minHour

					}
				}
			},
			coupouClick() {
				this.dataSelectCoupon()
				this.couponId = ''
			},
			// 日期选择
			animeDatKt(value) {
				this.kaitaiSelect()
			},
			// 开台统计
			kaitaiSelect() {
				let storeId = this.$route.query.storeId
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('orders/selectShopOpenOrdersCount'),
					method: 'get',
					params: this.$http.adornParams({
						'tearoomId': storeId,
						'time': this.info.stockDate,
						'type': this.way
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.totalRevenueKtTj = returnData
				})
			},
			// 钱包明细
			qianbaoSelect() {
				let storeId = this.$route.query.storeId
				this.tableDataLoading = true

				// 获取充电站收益
				this.$http({
					url: this.$http.adornUrl('orders/selectOrdersMoneyByTearoomId'),
					method: 'get',
					params: this.$http.adornParams({
						'tearoomId': storeId,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.totalRevenue = returnData
				})
				// 获取商家订单数量
				this.$http({
					url: this.$http.adornUrl('orders/selectOrdersStatusListByTearoomId'),
					method: 'get',
					params: this.$http.adornParams({
						'tearoomId': storeId,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.orders = returnData
				})

				if (this.userId) {
					// 获取商家钱包
					this.$http({
						url: this.$http.adornUrl('Details/selectMyMoney'),
						method: 'get',
						params: this.$http.adornParams({
							'userId': this.userId,
						})
					}).then(({
						data
					}) => {
						this.tableDataLoading = false
						let returnData = data.data;
						this.tableDataQb = returnData
					})
				}
				// 钱包明细
				this.$http({
					url: this.$http.adornUrl('Details/selectShopUserMoneyDetailsList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.size,
						'userId': this.userId,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableDataQbL = returnData
				})
			},
			// 不可提现金额充值
			rechargenone(index, moneyType) {
				this.typeIndex = index
				this.type = ''
				this.money = ''
				this.titleAx = ''
				this.contentAx = ''
				this.moneyType = moneyType
				this.dialogFormVisibleQb = true
			},
			StairNoticeTo1() {
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
				let storeId = this.$route.query.storeId
				if (this.typeIndex == 1) {
					this.$http({
						url: this.$http.adornUrl('Details/updateShopMoney'),
						method: 'post',
						// data: this.$http.adornData({
						params: this.$http.adornParams({
							'userId': this.userId,
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
									this.qianbaoSelect()
								}
							})
							this.dialogFormVisibleQb = false
						} else {
							this.$message.error(data.msg)
						}

					})
				} else {
					this.$http({
						url: this.$http.adornUrl('welfareMoney/updateWelfareMoney'),
						method: 'post',
						// data: this.$http.adornData({
						params: this.$http.adornParams({
							'type': this.type,
							'money': this.money,
							'title': this.titleAx,
							'content': this.contentAx,
							'moneyType': this.moneyType,
							'id': this.qbId,
							'classify': 2,
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
									if (this.typeIndex == 1) {
										this.qianbaoSelect()
									} else {
										this.dongtaiSelectAxqb()
										this.aixinpaihaoSelect()
									}

								}
							})
							this.dialogFormVisibleQb = false
						} else {
							this.$message.error(data.msg)
						}

					})
				}


			},
			// 结算金额
			rechargenone2() {
				this.money = ''
				this.dialogFormVisibleJs = true
			},
			StairNoticeTo2() {

				if (this.money == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入金额',
						type: 'warning'
					})
					return
				}
				this.$http({
					url: this.$http.adornUrl('Details/cashShopMoney'),
					method: 'post',
					// data: this.$http.adornData({
					params: this.$http.adornParams({
						'userId': this.userId,
						'money': this.money
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
								this.qianbaoSelect()
							}
						})
						this.dialogFormVisibleJs = false
					} else {
						this.$message.error(data.msg)
					}

				})
			},
			// 获取评价数据
			dataSelectPj() {
				let storeId = this.$route.query.storeId
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('comment/selectCommentList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.size,
						'tearoomId': storeId,
						'score': this.scoreType,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.tableDataPj = returnData
				})
			},
			refreshPj() {
				this.page = 1
				this.dataSelectPj()
			},
			// 充电站数据详情弹框
			shopDels(row) {
				this.goodsId = row.goodsId
				this.dataSelectCssjR()
				this.dataSelectCssjZ()
				this.dataSelectCssjY()
				this.dataSelectCssjN()
				this.dialogFormVisibleCssj = true
			},
			// 充电站日数据
			dataSelectCssjR() {
				this.tableDataLoadingR = true
				this.$http({
					url: this.$http.adornUrl('orders/selectTearoomOrdersByGoodsIdAndTime'),
					method: 'get',
					params: this.$http.adornParams({
						'goodsId': this.goodsId,
						'time': this.info.stockDate,
						'flag': 1
					})
				}).then(({
					data
				}) => {
					this.tableDataLoadingR = false
					let returnData = data.data
					this.incomeR = returnData
				})
			},
			// 充电站周数据
			dataSelectCssjZ() {
				this.tableDataLoadingZ = true
				if (this.value1.length == 0) {
					var endTime = this.info.stockDate
					const start = new Date();
					start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);

					var year = start.getFullYear() //得到年份
					var month = start.getMonth() //得到月份
					var date = start.getDate() //得到日期
					month = month + 1
					month = month.toString().padStart(2, '0')
					date = date.toString().padStart(2, '0')
					var defaultDate = `${year}-${month}-${date}`

					var times = defaultDate
				} else {
					var times = this.value1[0]
					var endTime = this.value1[1]

				}
				this.$http({
					url: this.$http.adornUrl('orders/selectTearoomOrdersByGoodsIdAndTime'),
					method: 'get',
					params: this.$http.adornParams({
						'goodsId': this.goodsId,
						'time': times,
						'endTime': endTime,
						'flag': 4
					})
				}).then(({
					data
				}) => {
					this.tableDataLoadingZ = false
					let returnData = data.data
					this.incomeZ = returnData
				})
			},
			// 充电站月数据
			dataSelectCssjY() {
				this.tableDataLoadingY = true
				this.$http({
					url: this.$http.adornUrl('orders/selectTearoomOrdersByGoodsIdAndTime'),
					method: 'get',
					params: this.$http.adornParams({
						'goodsId': this.goodsId,
						'time': this.info.stockDate,
						'flag': 2
					})
				}).then(({
					data
				}) => {
					this.tableDataLoadingY = false
					let returnData = data.data
					this.incomeY = returnData
				})
			},
			// 充电站年数据
			dataSelectCssjN() {
				this.tableDataLoadingN = true
				this.$http({
					url: this.$http.adornUrl('orders/selectTearoomOrdersByGoodsIdAndTime'),
					method: 'get',
					params: this.$http.adornParams({
						'goodsId': this.goodsId,
						'time': this.info.stockDate,
						'flag': 3
					})
				}).then(({
					data
				}) => {
					this.tableDataLoadingN = false
					let returnData = data.data
					this.incomeN = returnData
				})
			},
			timeDateR() {
				this.dataSelectCssjR()
			},
			timeDateZ() {
				console.log('value1', this.value1)
				this.dataSelectCssjZ()
			},

			timeDateY() {
				this.dataSelectCssjY()
			},
			timeDateN() {
				this.dataSelectCssjN()
			},


			// 获取用户列表弹框
			userselect(index, row) {
				this.indexType = index
				if (row) {
					this.ordersId = row.ordersId
				} else {
					this.ordersId = ''
				}

				this.dialogFormVisible3 = true
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
			// 获取保洁数据
			baojieSelect() {
				let storeId = this.$route.query.storeId
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('user/selectUserList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.size,
						'tearoomId': storeId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.baojieData = returnData
				})
			},
			// 确认选择 用户
			confirmBj(row) {
				// 确定转单
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
								this.orderSelect()
							}
						})
						this.dialogFormVisible3 = false
					} else {
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {}
						})
					}

				})
				// 确认选择保洁
				// let storeId = this.$route.query.storeId
				// this.$http({
				// 	url: this.$http.adornUrl('user/updateTearoomUser'),
				// 	method: 'post',
				// 	// data: this.$http.adornData({
				// 	params: this.$http.adornParams({
				// 		'userId': row.userId,
				// 		'tearoomId': storeId
				// 	})
				// }).then(({
				// 	data
				// }) => {
				// 	if (data.code == 0) {
				// 		this.$message({
				// 			message: '添加成功',
				// 			type: 'success',
				// 			duration: 1500,
				// 			onClose: () => {
				// 				this.baojieSelect()
				// 				this.dialogFormVisible3 = false
				// 			}
				// 		})
				// 	} else {
				// 		this.$message.error(data.msg)
				// 	}

				// })
			},
			refreshBj() {
				this.page = 1
				this.baojieSelect()
			},
			// 删除保洁
			deleteqBj(row) {
				this.$confirm(`确定删除此保洁?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('user/updateTearoomUser'),
						method: 'post',
						// data: this.$http.adornData({
						params: this.$http.adornParams({
							'userId': row.userId,
							'tearoomId': ''
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
									this.baojieSelect()
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
			// 查看二维码
			erweima(row) {
				this.titles = '开台二维码'
				this.dialogFormVisible2 = true
				let storeId = this.$route.query.storeId
				this.erweimaImg = this.$http.adornUrl('app/invite/mpCreateQr?invitationCode=' + storeId + ',' + row
					.goodsId + '&page=pages/index/order')
			},
			erweimaDc(row) {
				this.titles = '点餐二维码'
				this.dialogFormVisible2 = true
				let storeId = this.$route.query.storeId
				this.erweimaImg = this.$http.adornUrl('app/invite/mpCreateQr?invitationCode=' + storeId + ',' + row
					.goodsId + '&page=shopPackage/mealinfo/mealinfo')
			},
			chakan() {

				window.open(this.erweimaImg, '_blank');
			},
			downloadImage(e) {
				let hreLocal = "";
				hreLocal = this.erweimaImg;
				this.downloadByBlob(hreLocal, this.erweimaImgName)

			},
			downloadByBlob(url, name) {
				let image = new Image()
				image.setAttribute('crossOrigin', 'anonymous')
				image.src = url
				image.onload = () => {
					let canvas = document.createElement('canvas')
					canvas.width = image.width
					canvas.height = image.height
					let ctx = canvas.getContext('2d')
					ctx.drawImage(image, 0, 0, image.width, image.height)
					canvas.toBlob((blob) => {
						let url = URL.createObjectURL(blob)
						this.download(url, name)
						// 用完释放URL对象
						URL.revokeObjectURL(url)
					})
				}
			},
			download(href, name) {
				let eleLink = document.createElement('a')
				eleLink.download = name
				eleLink.href = href
				eleLink.click()
				eleLink.remove()
				this.$message({
					message: '下载完成！',
					type: 'success',
					duration: 1500,
					onClose: () => {}
				})
				this.shopSelect()

			},
			// 多选
			changeFun(val) {
				this.checkBoxData = val
			},
			// 批量下载
			transferClcik(index, id) {

				var ids = id ? [id] : this.checkBoxData.map(item => {

					return item.id
				})
				var names = this.checkBoxData.map(item => {
					return item.goodsName
				})

				for (var i in ids) {

					this.erweimaImg = this.$http.adornUrl('invite/mpCreateQr?relation=' + ids[i] +
						'&page=my/cabinet/cabinet')
					this.erweimaImgName = names[i]
					this.downloadImage()
				}
				this.checkBoxData = []
			},
			//开灯
			kaideng(row, index) {
				this.$confirm(`确定要开灯?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('room/lightControl'),
						method: 'get',
						params: this.$http.adornParams({
							'goodsId': row.goodsId,
							'status': index,
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
									this.shopSelect()
								}
							})
						} else {
							this.$message({
								message: data.msg,
								type: 'warning',
								duration: 1500,
								onClose: () => {
									this.shopSelect()
								}
							})
						}

					})
				}).catch(() => {})
			},
			// 充电时长统计
			kaitaishichangSelect() {
				let storeId = this.$route.query.storeId
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('orders/selectOrdersGoodsHourByTearoomId'),
					method: 'get',
					params: this.$http.adornParams({
						'startTime': this.startTime,
						'endTime': this.endTime,
						'tearoomId': storeId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					for (var i in data.data) {
						if (data.data[i].sumSecond) {
							const seconds = Number(data.data[i].sumSecond)
							const hours = Math.floor(seconds / 3600); // 10 / 3600 约等于 0.0027，Math.floor 取整后为 0
							// 计算剩余的秒数
							seconds = seconds % 3600; // 10 % 3600 结果为 10
							// 计算分钟数
							const minutes = Math.floor(seconds / 60); // 10 / 60 约等于 0.167，Math.floor 取整后为 0
							// 计算最终剩余的秒数
							seconds = seconds % 60; // 10 % 60 结果为 10
							// 将结果格式化为字符串，确保每个部分都是两位数
							const formattedHours = String(hours).padStart(2, '0'); // 结果为 "00"
							const formattedMinutes = String(minutes).padStart(2, '0'); // 结果为 "00"
							const formattedSeconds = String(seconds).padStart(2, '0'); // 结果为 "10"

							data.data[i].sumSecond = formattedHours + '时' + formattedMinutes + '分' +
								formattedSeconds + '秒'
						}
					}
					let returnData = data.data;
					this.ktsctjData = returnData
				})
			},
			//搜索
			select4() {
				this.kaitaishichangSelect()
			},
			// 批量修改价格 弹框
			jiageClcik() {
				this.hourPrice = ''
				this.minutePrice = ''
				this.subscription = ''
				this.cashPledge = ''
				this.dialogFormVisibleJe = true

			},
			// 批量修改金额
			StairNoticeToJe(id) {
				var ids = id ? [id] : this.checkBoxData.map(item => {
					return item
				})
				console.log('ids', ids)
				this.indTy = ids.length
				for (var i in ids) {
					ids[i].hourPrice = this.hourPrice
					ids[i].minutePrice = this.minutePrice
					ids[i].subscription = this.subscription
					ids[i].cashPledge = this.cashPledge
					this.$http({
						url: this.$http.adornUrl("goods/updateGoods "),
						method: "post",
						data: this.$http.adornData(ids[i])
					}).then(({
						data
					}) => {
						if (data.code == 0) {


						} else {
							this.indTixt = data.msg
							this.$notify({
								title: '提示',
								duration: 1800,
								message: this.indTixt,
								type: 'error'
							})
						}

					})
					if (this.indTy === (Number(i) + 1)) {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '操作成功',
							type: 'warning'
						})
						this.dialogFormVisibleJe = false
						this.checkBoxData = []
						this.shopSelect()
					}
				}

			},
			// 特殊时间段 配置弹框
			discussTime(row) {
				this.goodsId = row.goodsId
				this.page1 = 1
				this.timeClass()
				this.dialogFormVisibleTssj = true
			},
			timeclick() {
				this.page1 = 1
				this.timeClass()
			},
			timeClass() {
				this.tableDataLoading4 = true
				let storeId = this.$route.query.storeId
				this.$http({
					url: this.$http.adornUrl('goodsTime/selectGoodsTimeList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page1,
						'limit': this.size1,
						'tearoomId': storeId,
					})

				}).then(({
					data
				}) => {
					this.tableDataLoading4 = false
					let returnData = data.data
					this.userIds = returnData
				})
			},
			// 添加、修改 时间弹框
			timeadd(row) {
				if (row) {
					this.titles = '修改'
					this.goodsTimeId = row.goodsTimeId
					this.startTime = row.startTime
					this.endTime = row.endTime
					this.hourPrice = row.hourPrice
					this.minutePrice = row.minutePrice
					this.memberHourPrice = row.memberHourPrice
					this.memberMinutePrice = row.memberMinutePrice
					this.electricityPrice = row.electricityPrice
					this.memberElectricityPrice = row.memberElectricityPrice
				} else {
					this.titles = '添加'
					this.goodsTimeId = ''
					this.startTime = ''
					this.endTime = ''
					this.hourPrice = ''
					this.minutePrice = ''
					this.memberHourPrice = ''
					this.memberMinutePrice = ''
					this.electricityPrice = ''
					this.memberElectricityPrice = ''
				}
				this.dialogFormVisibleTs = true
			},
			timeNoticeToJe() {
				if (this.startTime === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '开始时间不能为空',
						type: 'warning'
					});
					return
				}
				if (this.endTime === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '结束时间不能为空',
						type: 'warning'
					});
					return
				}
				if(this.tearoomType==1){
					if (this.minutePrice === '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '分钟价不能为空',
							type: 'warning'
						});
						return
					}
					if (this.memberMinutePrice === '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '会员分钟价不能为空',
							type: 'warning'
						});
						return
					}
					
				}else{
					if (this.electricityPrice === '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '用户每度电价格不能为空',
							type: 'warning'
						});
						return
					}
					if (this.memberElectricityPrice === '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '会员每度电价格不能为空',
							type: 'warning'
						});
						return
					}
					// if (this.hourPrice === '') {
					// 	this.$notify({
					// 		title: '提示',
					// 		duration: 1800,
					// 		message: '小时价不能为空',
					// 		type: 'warning'
					// 	});
					// 	return
					// }
					// if (this.memberHourPrice === '') {
					// 	this.$notify({
					// 		title: '提示',
					// 		duration: 1800,
					// 		message: '会员小时价不能为空',
					// 		type: 'warning'
					// 	});
					// 	return
					// }
				}
				
				
				
				if (this.titles == '添加') {
					var url = 'goodsTime/insertGoodsTime'
				} else {
					var url = 'goodsTime/updateGoodsTime'
				}
				let storeId = this.$route.query.storeId
				this.$http({
					url: this.$http.adornUrl(url),
					method: 'post',
					data: this.$http.adornData({
						// 'goodsId': this.goodsId,
						'tearoomId': storeId,
						'goodsTimeId': this.goodsTimeId,
						'startTime': this.startTime,
						'endTime': this.endTime,
						'hourPrice': this.hourPrice,
						'minutePrice': this.minutePrice,
						'memberHourPrice': this.memberHourPrice,
						'memberMinutePrice': this.memberMinutePrice,
						'electricityPrice': this.electricityPrice,
						'memberElectricityPrice': this.memberElectricityPrice,
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
								this.dialogFormVisibleTs = false
								this.timeClass()
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {
								this.timeClass()
							}
						})
					}

				})
			},
			timedelete(row) {
				this.$confirm(`确定删除?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('goodsTime/deleteGoodsTimeById'),
						method: 'post',
						// data: this.$http.adornData({
						params: this.$http.adornParams({
							'goodsTimeId': row.goodsTimeId,
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
									this.timeClass()
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
			// 获取员工用户列表弹框
			userselectYg(index) {
				this.typeIndex = index
				this.indexType = 2
				this.dialogFormVisibleYg = true
				this.page1 = 1
				this.phone1 = ''
				this.nickName2 = ''
				this.userClass()
			},
			// 删除员工
			ygClose(index) {
				this.employeeUserIds.splice(index, 1);
				this.userEntityList.splice(index, 1);
			},
			// 多选
			changeFun(val) {
				this.checkBoxData = val
			},
			// 选择用户
			userClcik() {

				for (var i in this.checkBoxData) {
					this.employeeUserIds.push(this.checkBoxData[i].userId)
					this.userEntityList.push(this.checkBoxData[i])
				}
				this.dialogFormVisibleYg = false
			},

			qingkongYhq() {
				this.luckyCouponIds = []
				this.luckyCouponList = []
			},
			// 获取已发布列表弹框
			userselectYhq() {
				this.page = 1
				this.typeY = 2
				this.statusY = ''
				this.couponNameY = ''
				this.dataSelectYhq()

				this.dialogFormVisible5 = true
			},
			// 获取优惠券数据列表
			dataSelectYhq() {
				this.tableDataLoadingY = true
				let storeId = this.$route.query.storeId
				this.$http({
					url: this.$http.adornUrl('selfCouponIssue/list'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page1 - 1,
						'size': this.size1,
						'type': this.typeY,
						'tearoomName': '',
						'couponName': this.couponNameY,
						'tearoomId': storeId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoadingY = false
					let returnData = data.data
					this.tableDataYhq = returnData
				})
			},
			handleSizeChangeYhq(val) {
				this.size1 = val;
				this.dataSelectYhq()
			},
			handleCurrentChangeYhq(val) {
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
			// 多选优惠券
			changeFun1(val) {
				console.log('多选', val)
				this.xzList = val

			},
			closes1() {
				console.log('-----1', this.luckyCouponList)
				for (var i in this.xzList) {
					this.luckyCouponIds.push(this.xzList[i].couponIssueId)
					this.luckyCouponList.push(this.xzList[i])
				}
				console.log('-----2', this.luckyCouponList)
				this.dialogFormVisible5 = false
			},
			// 删除优惠券
			yhqClose(index) {
				this.luckyCouponIds.splice(index, 1);
				this.luckyCouponList.splice(index, 1);
			},
			// 选择优惠券
			yhqClcik() {

				for (var i in this.checkBoxData) {
					this.luckyCouponIds.push(this.checkBoxData[i].userId)
					this.luckyCouponList.push(this.checkBoxData[i])
				}
				this.dialogFormVisibleYg = false
			},



			// 桌台直播弹框
			liveBtn(row) {

				if (row.goodsTips == '' || row.goodsTips == null) {
					this.$notify({
						message: '暂无推流链接，请先添加或者修改推流链接',
						type: 'warning',
						duration: 1500,
						onClose: () => {}
					})
				} else {
					this.dialogFormVisibleZb = true
					this.goodsTips = row.goodsTips
					this.playHttpFlvStream()
				}

			},
			playHttpFlvStream() {
				if (flvjs.isSupported()) {
					this.$nextTick(() => {
						const videoElement = this.$refs.videoElement;
						const httpFlvUrl = this.goodsTips

						flvPlayer = flvjs.createPlayer({
							type: 'flv',
							url: this.$http.adornUrlLive(httpFlvUrl), // 替换为你的直播流URL
							isLive: true,
							cors: true,

						}); // 配置选项，可根据需要调整
						console.log('1111', this.flvPlayer, '-------------', flvPlayer, )
						flvPlayer.attachMediaElement(videoElement);
						flvPlayer.load(); // 加载直播流
						flvPlayer.play(); // 开始播放
					})

				} else {
					this.$message({
						message: '视频加载失败',
						type: 'warning',
						duration: 1500,
						onClose: () => {}
					})
				}
			},
			clonkVideo() {
				flvPlayer.destroy();
				this.dialogFormVisibleZb = false
			},
			// 获取商铺列表
			shopClass() {
				this.tableDataLoading4 = true
				this.$http({
					url: this.$http.adornUrl('admin/takeoutGoods/selectAllShop'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page1,
						'limit': this.size1,
						'shopName': this.nickName2
					})

				}).then(({
					data
				}) => {
					this.tableDataLoading4 = false
					let returnData = data.data
					this.userIds = returnData
				})
			},
			// 获取商铺弹框
			shopselectYg() {
				this.indexType = 2
				this.dialogFormVisibleSp = true
				this.page1 = 1
				this.nickName2 = ''
				this.shopClass()
			},
			// 删除商铺
			spClose(index) {
				this.shopsList.splice(index, 1);
				this.shopIds.splice(index, 1);
			},
			// 多选
			changeFun(val) {
				this.checkBoxData = val
			},
			// 选择商铺
			shopClcik() {

				for (var i in this.checkBoxData) {
					this.shopsList.push(this.checkBoxData[i])
					this.shopIds.push(this.checkBoxData[i].shopId)
				}
				this.dialogFormVisibleSp = false
			},
			// 获取商铺订单数据列表
			taskdataSelect() {

				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/order/selectAllOrder'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'phone': this.phone,
						'userName': this.userName,
						'orderNumber': this.orderNumber,
						'status': this.statusT,
						'shopId': this.shopId,
						'orderType': '',
						'tearoomName': '',
						'tearoomId': this.storeId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.tableData2 = returnData
				})
			},
			// 查询
			phoneSelect() {
				this.page = 1
				this.limit = 10
				this.taskdataSelect()
			},
			// 重置
			cleans2() {
				this.page = 1
				this.phone = ''
				this.userName = ''
				this.orderNumber = ''
				this.tearoomName = ''
				this.statusT = ''
				this.orderType = ''
				this.taskdataSelect()
			},
			handleSizeChangeSp(val) {
				this.limit = val
				this.taskdataSelect()
			},
			handleCurrentChangeSp(val) {
				this.page = val
				this.taskdataSelect()
			},
			// 订单商品弹框
			orderGoodsBtn(row) {
				// this.orderGoodsList = row
				this.orderId = row.orderId
				this.ordersSelect()
				this.shopOrderGoods = true
			},
			ordersSelect() {
				this.$http({
					url: this.$http.adornUrl('admin/order/selectOrderById'),
					method: 'get',
					params: this.$http.adornParams({
						'orderId': this.orderId,
					})
				}).then(({
					data
				}) => {
					let returnData = data.data
					this.orderGoodsList = returnData
				})
			},
			// 打印小票
			dayin(row) {
				this.$http({
					url: this.$http.adornUrl('admin/order/print'),
					method: 'post',
					params: this.$http.adornParams({
						'orderId': row.orderId
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
								this.taskdataSelect()
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
			// 修改排序
			zhiition(row) {
				// this.$confirm(`确定要修改直属用户佣金比例吗?`, '提示', {
				// 	confirmButtonText: '确定',
				// 	cancelButtonText: '取消',
				// 	type: 'warning'
				// }).then(() => {

				this.$http({
					url: this.$http.adornUrl("goods/updateGoods "),
					method: "post",
					data: this.$http.adornData({
						'sort': row.sort,
						'goodsId': row.goodsId
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
								this.shopSelect()
							}
						})
					} else {
						this.$message.error(data.msg)
					}
				})
				// }).catch(() => {})
			},
			// 返回上一级
			prev() {
				this.$router.back()
			},
			// 换台
			huantaiClick(row) {
				this.hourPrice = Number(row.hourMoney)
				this.ordersId = row.ordersId
				this.tearoomId = row.tearoomId
				this.zhuotaiSelect()
				this.dialogFormVisiblezt = true
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
						'tearoomId': this.tearoomId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoadingU = false
					let returnData = data.data;
					this.goodslist = returnData.list
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
								this.orderSelect()
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
			// 其他金额 弹框
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
			// 核销统计
			dongtaiSelectHx() {
				let storeId = this.$route.query.storeId
				var flagA = 1
				if (this.way == 1) {
					flagA = 1
				}
				if (this.way == 2) {
					flagA = 2
				}
				if (this.way == 3) {
					flagA = 3
				}
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('selfCouponUser/selectTearoomCount'),
					method: 'get',
					params: this.$http.adornParams({
						'time': this.info.stockDate,
						'flag': flagA,
						'tearoomId': this.storeId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.hexiaoCount = returnData
				})
			},
			animeDatHx() {
				this.dongtaiSelectHx()
			},
			// 退全款
			refundClick(row) {
				if (row.refundMoney) {
					var payMoney = Number(row.payMoney) - Number(row.refundMoney)
				} else {
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
									this.orderSelect()
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
				if (row.refundMoney) {
					this.payMoney = Number(row.payMoney) - Number(row.refundMoney)
				} else {
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
								this.orderSelect()
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
			// 获取收益明细
			shopshouyiSelect() {
				let storeId = this.$route.query.storeId
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('orders/selectOrdersMoneyByTearoomIdAndDate'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': 10,
						'way': this.way,
						'tearoomId': storeId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableDataq = []
					for (var i in data.data.dateTimeList) {
						for (var j in data.data.ordersMoneyList) {
							if (i == j) {
								var datas = {
									dateTime: data.data.dateTimeList[i],
									money: data.data.ordersMoneyList[j]
								}
								// console.log('-------',datas)
								this.tableDataq.push(datas)
							}

						}
					}
					console.log('-------', this.tableDataq)
				})
			},
			animeDatSy() {
				this.page = 1
				this.shopshouyiSelect()
			},
			// 收益详情 弹框
			orderSysBtn(row) {
				this.page = 1
				// this.orderGoodsList = row
				this.dateTimes = row.dateTime
				this.sySelect()
				this.shopOrderGoodsSy = true
			},
			sySelect() {
				let storeId = this.$route.query.storeId
				this.$http({
					url: this.$http.adornUrl('orders/selectOrdersList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						// 'phone': this.orderType,
						// 'userName': this.userName,
						// 'goodsName': this.ordersUserName,
						// 'ordersNo': this.orderNum,
						'status': 4,
						// 'startTime': this.startTime,
						// 'endTime': this.endTime,
						// 'tearoomId': storeId
						'dateTime': this.dateTimes,
						'tearoomId': storeId
					})

				}).then(({
					data
				}) => {
					let returnData = data.data
					this.orderGoodsList = returnData
				})
			},
			handleSizeChangeSy(val) {
				this.limit = val
				this.sySelect()
			},
			handleCurrentChangeSy(val) {
				this.page = val
				this.sySelect()
			},
			// 排号管理
			paihaoSelect() {
				let storeId = this.$route.query.storeId
				this.$http({
					url: this.$http.adornUrl('queueUp/selectQueueUpList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'status': this.statusP,
						'tearoomId': storeId,
						'createTime': this.createTime,
						'goodsCategory': this.goodsCategory,
					})

				}).then(({
					data
				}) => {
					let returnData = data.data
					this.tableDatPh = returnData
				})
			},
			animeDatPh() {
				this.page = 1
				this.paihaoSelect()
			},
			animeDatCz() {
				this.page = 1
				this.statusP = 1
				this.goodsCategory = ''
				this.createTime = this.info.stockDate
				this.paihaoSelect()
			},
			handleSizeChangeSh(val) {
				this.limit = val
				this.paihaoSelect()
			},
			handleCurrentChangeSh(val) {
				this.page = val
				this.paihaoSelect()
			},
			// 过号
			guohaoBtn(row) {
				this.$confirm(`确定要过号?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('queueUp/updateQueueUp'),
						method: 'post',
						// params: this.$http.adornParams({
						data: this.$http.adornData({
							'queueUpId': row.queueUpId,
							'status': 3
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
									this.paihaoSelect()
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
			// 是否是现在开台
			changeTg(row) {
				this.$http({
					url: this.$http.adornUrl("goods/updateGoods "),
					method: "post",
					data: this.$http.adornData({
						'goodsId': row.goodsId,
						'tearoomId': row.tearoomId,
						'goodsName': row.goodsName,
						'goodsType': row.goodsType,
						'price': row.price,
						'roomPwd': row.roomPwd,
						'goodsCoverImage': row.goodsCoverImage,
						'goodsTitleImage': row.goodsTitleImage,
						'iconImg': row.iconImg,
						'goodsDetails': row.goodsDetails,
						'roomNo': row.roomNo,
						'endTime': row.endTime,
						'goodsTips': row.goodsTips,
						'roomWifi': row.roomWifi,
						'productIds': row.productIds, //充电桩套餐id
						'labels': row.labels, //标签
						'sales': row.sales,
						'channels': row.channels,
						'goodsCategory': row.goodsCategory,
						'hourPrice': row.hourPrice,
						'minutePrice': row.minutePrice,
						'cashPledge': row.cashPledge,
						'subscription': row.subscription,
						'yyToken': row.yyToken,
						'yyId': row.yyId,
						'goodsType': row.goodsType,
						'sort': row.sort,
						'cameraId': row.cameraId,
						'isOpen': row.isOpen,

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
								this.shopSelect()
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {
								this.shopSelect()
							}
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
			// 爱心钱包
			dongtaiSelectAxqb() {
				let storeId = this.$route.query.storeId
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('welfareMoney/selectWelfareMoney'),
					method: 'get',
					params: this.$http.adornParams({
						'tearoomId': this.storeId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.hexiaoCount = returnData
					this.qbId = returnData.id
				})
			},
			aixinpaihaoSelect() {
				let storeId = this.$route.query.storeId
				this.tableDataLoadingA = true
				this.$http({
					url: this.$http.adornUrl('welfareMoney/selectWelfareMoneyDetailsList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'tearoomId': storeId,
						'userName': this.userNameT,
						'phone': this.phoneT,
						'title': this.titleT,
						'content': this.contentT,
						'moneyType': this.moneyTypeT,
					})

				}).then(({
					data
				}) => {
					this.tableDataLoadingA = false
					let returnData = data.data
					this.tableDatPh = returnData
				})
			},
			animeDatAxqb() {
				this.page = 1
				this.dongtaiSelectAxqb()
				this.aixinpaihaoSelect()
			},
			animeDatAxqbCz() {
				this.page = 1
				this.userNameT = ''
				this.phoneT = ''
				this.titleT = ''
				this.contentT = ''
				this.moneyTypeT = ''
				this.dongtaiSelectAxqb()
				this.aixinpaihaoSelect()
			},
			handleSizeChangeAx(val) {
				this.limit = val
				this.aixinpaihaoSelect()
			},
			handleCurrentChangeAx(val) {
				this.page = val
				this.aixinpaihaoSelect()
			},
			// 获取爱心公示
			aixingongshiSelect() {
				let storeId = this.$route.query.storeId
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('welfareMoney/selectWelfareWordList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'welfareWordTitle': this.titleT,
						'tearoomId': storeId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableDataq = returnData

				})
			},
			animeDatAxgs() {
				this.page = 1
				this.aixingongshiSelect()
			},
			handleSizeChangeAxgs(val) {
				this.limit = val
				this.aixingongshiSelect()
			},
			handleCurrentChangeAxgs(val) {
				this.page = val
				this.aixingongshiSelect()
			},
			// 编辑文章弹框
			updateswz(rows) {
				if (rows) {
					this.titles = '修改公示'
					this.formwz.welfareWordId = rows.welfareWordId;
					this.formwz.sort = rows.sort;
					this.formwz.welfareWordTitle = rows.welfareWordTitle;
					this.formwz.welfareWordContent = rows.welfareWordContent;
					if (rows.welfareImg) {
						this.formwz.welfareImg = rows.welfareImg.split(',')
					} else {
						this.formwz.welfareImg = []
					}

				} else {
					this.titles = '添加公示'
					this.formwz.welfareWordId = ''
					this.formwz.sort = ''
					this.formwz.welfareWordTitle = ''
					this.formwz.welfareWordContent = ''
					this.formwz.welfareImg = []
				}

				this.dialogFormVisibleAxgs = true;
			},
			// 修改文章
			amendNoticeTowz() {
				if (this.titles == '添加公示') {
					var url = 'welfareMoney/insertWelfareWord'
				} else {
					var url = 'welfareMoney/updateWelfareWord'
				}
				let storeId = this.$route.query.storeId
				this.$http({
					url: this.$http.adornUrl(url),
					method: 'post',
					data: this.$http.adornData({
						'welfareWordId': this.formwz.welfareWordId,
						'welfareWordTitle': this.formwz.welfareWordTitle,
						'welfareWordContent': this.formwz.welfareWordContent,
						'tearoomId': storeId,
						'sort': this.formwz.sort,
						'welfareImg': this.formwz.welfareImg.toString(),
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisibleAxgs = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.aixingongshiSelect()
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
			// 删除文章
			deleteswz(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('welfareMoney/deleteWelfareWord'),
						method: 'post',
						params: this.$http.adornParams({
							'welfareWordId': row.welfareWordId
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
									this.aixingongshiSelect()
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
			handleUploadSuccess4(file, fileList) {
				this.formwz.welfareImg.push(file.data)
				console.log('this.contentImg', this.formwz.welfareImg)
			},
			onprogress2(event, file, fileList) {
				console.log('event, file, fileList', parseInt(event.percent))
				this.percentage2 = parseInt(event.percent)
			},
			clear2(index) {
				this.formwz.welfareImg.splice(index, 1);
			},
			// 获取 会员钱包
			vipWalletSelect() {
				let storeId = this.$route.query.storeId
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('memberUserMoney/selectMemberUserMoneyList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'phone': this.titleT,
						'tearoomId': storeId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableDataWallet = returnData

				})
			},
			animeDatVipW() {
				this.page = 1
				this.vipWalletSelect()
			},
			handleSizeChangeVipW(val) {
				this.limit = val
				this.vipWalletSelect()
			},
			handleCurrentChangeVipW(val) {
				this.page = val
				this.vipWalletSelect()
			},
			// 编辑 会员钱包
			updatesVipW(rows) {
				if (rows) {
					this.titles = '修改'
					this.memberUserId = rows.memberUserId;
					this.phone = rows.phone;
					this.money = rows.money;

				} else {
					this.titles = '添加'
					this.memberUserId = ''
					this.phone = ''
					this.money = ''
				}

				this.dialogFormVisibleVipW = true;
			},
			// 修改
			amendNoticeToVipW() {
				if (this.titles == '添加') {
					var url = 'memberUserMoney/insertMemberUserMoney'
				} else {
					var url = 'memberUserMoney/updateMemberUserMoney'
				}
				let storeId = this.$route.query.storeId
				this.$http({
					url: this.$http.adornUrl(url),
					method: 'post',
					data: this.$http.adornData({
						'memberUserId': this.memberUserId,
						'phone': this.phone,
						'money': this.money,
						'tearoomId': storeId,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisibleVipW = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.vipWalletSelect()
								this.dongtaiSelectVip()
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
			// 删除
			deletesVipW(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('memberUserMoney/deleteMemberUserMoney'),
						method: 'post',
						params: this.$http.adornParams({
							'memberUserMoneyId': row.memberUserId
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
									this.vipWalletSelect()
									this.dongtaiSelectVip()
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
			// 获取 会员钱包明细
			vipWalletSelectMx() {
				let storeId = this.$route.query.storeId
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('memberUserMoneyDetailsDetails/selectMemberUserMoneyDetailsList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'phone': this.titleT,
						'tearoomId': storeId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableDataWalletMx = returnData

				})
			},
			animeDatVipW() {
				this.page = 1
				this.vipWalletSelectMx()
			},
			handleSizeChangeVipWmx(val) {
				this.limit = val
				this.vipWalletSelectMx()
			},
			handleCurrentChangeVipWmx(val) {
				this.page = val
				this.vipWalletSelectMx()
			},
			// 编辑 会员钱包
			updatesVipWmx(index, rows) {
				this.typeIndex = index
				if (index == 1) {

					if (rows) {
						this.titles = '修改钱包'
						this.memberUserId = rows.memberUserId;
						this.money = ''
						this.type = ''
						this.money = ''
						this.titleAx = ''
						this.contentAx = ''

					}
				} else {
					if (rows) {
						this.titles = '修改'
						this.memberUserId = rows.memberUserId;
						this.phone = rows.phone;
						this.money = rows.money;
						this.type = rows.type;
						this.titleAx = rows.title;
						this.contentAx = rows.content;
						this.id = rows.id

					} else {
						this.titles = '添加'
						this.memberUserId = ''
						this.phone = ''
						this.money = ''
						this.type = ''
						this.money = ''
						this.titleAx = ''
						this.contentAx = ''
						this.id = ''
					}
				}


				this.dialogFormVisibleVipWmx = true;
			},
			// 修改 会员钱包明细
			amendNoticeToVipWmx() {
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
				if (this.titles == '添加') {
					var url = 'memberUserMoneyDetailsDetails/insertMemberUserMoneyDetails'
				} else {
					var url = 'memberUserMoneyDetailsDetails/updateMemberUserMoneyDetails'
				}
				let storeId = this.$route.query.storeId
				this.$http({
					url: this.$http.adornUrl(url),
					method: 'post',
					data: this.$http.adornData({
						'memberUserId': this.memberUserId,
						'phone': this.phone,
						'tearoomId': storeId,
						'type': this.type,
						'money': this.money,
						'title': this.titleAx,
						'content': this.contentAx,
						'id': this.id,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisibleVipWmx = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.vipWalletSelectMx()
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
			// 删除会员钱包明细
			deletesVipWmx(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(
							'memberUserMoneyDetailsDetails/deleteMemberUserMoneyDetails'),
						method: 'post',
						params: this.$http.adornParams({
							'memberUserMoneyDetailsId': row.id
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
									this.vipWalletSelectMx()
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
			// 获取 会员钱包列表 弹框
			phoneListselect() {

				this.dialogFormVisibleHyqb = true
				this.page1 = 1
				this.phone1 = ''
				this.phoneClass()
			},
			// 获取用户列表
			phoneClass() {
				this.tableDataLoading4 = true

				let storeId = this.$route.query.storeId
				this.$http({
					url: this.$http.adornUrl('memberUserMoney/selectMemberUserMoneyList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page1,
						'limit': this.size1,
						'phone': this.phone1,
						'tearoomId': storeId
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
			phoneclick() {
				this.page1 = 1
				this.phoneClass()
			},
			// 确定用户
			phoneconfirm(row) {
				this.memberUserId = row.memberUserId
				this.phone = row.phone
				this.dialogFormVisibleHyqb = false
			},
			handleSizeChangeHyqb(val) {
				this.size1 = val
				this.phoneClass()
			},
			handleCurrentChangeHyqb(val) {
				this.page1 = val
				this.phoneClass()
			},
			// 修改会员钱包金额
			amendNoticeToVipQb() {
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
				let storeId = this.$route.query.storeId
				this.$http({
					url: this.$http.adornUrl('memberUserMoney/updateMemberUserMoneyByType'),
					method: 'post',
					// data: this.$http.adornData({
					params: this.$http.adornParams({
						'memberUserMoneyId': this.memberUserId,
						'tearoomId': storeId,
						'type': this.type,
						'money': this.money,
						'title': this.titleAx,
						'content': this.contentAx,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisibleVipWmx = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.vipWalletSelect()
								this.dongtaiSelectVip()
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
			// 会员钱包统计
			dongtaiSelectVip() {
				let storeId = this.$route.query.storeId
				this.tableDataLoading1 = true
				this.$http({
					url: this.$http.adornUrl('memberUserMoney/selectMemberUserMoneyCount'),
					method: 'get',
					params: this.$http.adornParams({
						'tearoomId': this.storeId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading1 = false
					let returnData = data.data;
					this.vipCount = returnData
				})
			},
			// 获取 收藏歌单
			musicPlaytSelect() {
				let storeId = this.$route.query.storeId
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('shopCollectMusic/selectShopCollectMusic'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'search': this.titleT,
						'tearoomId': storeId,

					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableDataWalletMx = returnData

				})
			},
			animeDatmusic() {
				this.page = 1
				this.musicPlaytSelect()
			},
			handleSizeChangemusic(val) {
				this.limit = val
				this.musicPlaytSelect()
			},
			handleCurrentChangemusic(val) {
				this.page = val
				this.musicPlaytSelect()
			},
			// ----音乐库
			// 获取用户列表弹框
			musicselect() {
				this.dialogFormVisibleM = true
				this.page1 = 1
				this.musicTypeIdYyk = ''
				this.tearoomNameYyk = ''
				this.musicNameYyk = ''
				this.musicAuthorYyk = ''
				this.classSelectYyk()
				this.fenleiSelect()
			},
			// 获取活动类型数据
			classSelectYyk() {
				this.tableDataLoading1 = true
				this.$http({
					url: this.$http.adornUrl('musicLibrary/selectMusicLibraryList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page1,
						'limit': this.size1,
						'musicTypeId': this.musicTypeIdYyk,
						'musicName': this.musicNameYyk,
						'musicAuthor': this.musicAuthorYyk,
						'tearoomName': this.tearoomNameYyk,
					})
				}).then(({
					data
				}) => {
					console.log('', data)
					if (data.code == 0) {
						this.tableDataLoading1 = false
						let returnData = data.data
						this.tableDataYyk = returnData
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
			handleSizeChangeYyk(val) {
				this.size1 = val
				this.classSelectYyk()
			},
			handleCurrentChangeYyk(val) {
				this.page1 = val
				this.classSelectYyk()
			},
			handleClickYyk() {
				this.page1 = 1
				this.classSelectYyk()

			},
			clearClickYyk() {
				this.musicTypeIdYyk = ''
				this.tearoomNameYyk = ''
				this.musicNameYyk = ''
				this.musicAuthorYyk = ''
				this.page1 = 1
				this.classSelectYyk()
			},
			YykAdd(row) {

				let storeId = this.$route.query.storeId

				this.$http({
					url: this.$http.adornUrl('shopCollectMusic/insertShopCollectMusic'),
					method: 'post',
					// params: this.$http.adornParams({
					data: this.$http.adornData({
						'musicLibraryId': row.musicLibraryId,
						'musicUrl': row.musicUrl,
						'musicName': row.musicName,
						'tearoomId': storeId,
						'musicPicUrl': row.musicPicUrl,
						'musicAuthor': row.musicAuthor,

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
								this.musicPlaytSelect()
								this.dialogFormVisibleM = false
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
			fenleiSelect() {
				this.$http({
					url: this.$http.adornUrl('musicType/selectMusicTypeList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': 1,
						'limit': 50,
					})
				}).then(({
					data
				}) => {
					console.log('', data)
					if (data.code == 0) {
						let returnData = data.data
						this.tableDataType = returnData.records
					}
				})
			},
			// 删除会员钱包明细
			deletesmusic(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('shopCollectMusic/deleteCollectMusic'),
						method: 'post',
						params: this.$http.adornParams({
							'shopCollectMusicId': row.shopCollectMusicId
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
									this.musicPlaytSelect()
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
			// 员工列表
			classSelectYg() {
				this.tableDataLoading1 = true
				let storeId = this.$route.query.storeId
				this.$http({
					url: this.$http.adornUrl('user/selectUserList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page1,
						'limit': this.size1,
						'tearoomUser': storeId,
						// 'musicTypeId': this.musicTypeIdYyk,
						// 'musicName':this.musicNameYyk,
						// 'musicAuthor':this.musicAuthorYyk,
						// 'tearoomName':this.tearoomNameYyk,
					})
				}).then(({
					data
				}) => {
					console.log('', data)
					if (data.code == 0) {
						this.tableDataLoading1 = false
						let returnData = data.data
						this.tableDataYg = returnData
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
			// 添加员工
			userClcik2(row) {
				let storeId = this.$route.query.storeId
				this.$http({
					url: this.$http.adornUrl('user/insertTearoomUser'),
					method: 'post',
					params: this.$http.adornParams({
						'phone': row.phone,
						'tearoomId': storeId,
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

								this.classSelectYg()
								this.dialogFormVisibleYg = false
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
			// 删除 员工
			deletesYg(row) {
				let storeId = this.$route.query.storeId
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('user/deleteTearoomUser'),
						method: 'post',
						params: this.$http.adornParams({
							'userId': row.userId,
							'tearoomId': storeId,
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
									this.classSelectYg()
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
			// 员工权限设置
			ygClick(row) {
				this.userId = row.userId
				this.startTime = row.startTime
				this.endTime = row.endTime
				if (row.tearoomUserStatus) {
					this.tearoomUserStatus = row.tearoomUserStatus
				} else {
					this.tearoomUserStatus = 1
				}


				this.dialogFormVisibleYgqx = true
			},
			// 修改员工权限
			amendNoticeToYg() {
				if (this.startTime == '' || this.startTime == null) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择开始时间',
						type: 'warning'
					})
					return
				}
				if (this.endTime == '' || this.endTime == null) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择结束时间',
						type: 'warning'
					})
					return
				}
				if (this.tearoomUserStatus == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择用户状态',
						type: 'warning'
					})
					return
				}
				let storeId = this.$route.query.storeId
				this.$http({
					url: this.$http.adornUrl('user/updateTearoomUserTime'),
					method: 'post',
					// data: this.$http.adornData({
					params: this.$http.adornParams({
						'userId': this.userId,
						'endTime': this.endTime,
						'startTime': this.startTime,
						'tearoomId': storeId,
						'status': this.tearoomUserStatus
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisibleYgqx = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.classSelectYg()
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
			// 开台统计
			ktClick(row) {
				this.userId = row.userId
				this.kaitaiSelectYg()
				this.dialogFormVisibleKt = true
			},
			// 员工开台统计
			kaitaiSelectYg() {
				let storeId = this.$route.query.storeId
				this.tableDataLoading1 = true
				this.$http({
					url: this.$http.adornUrl('orders/selectYgOpenOrdersCount'),
					method: 'get',
					params: this.$http.adornParams({
						'tearoomId': storeId,
						'userId': this.userId,
						'time': this.info.stockDate,
						'type': this.way
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading1 = false
					let returnData = data.data;
					this.totalRevenueKtTj = returnData
				})
			},
			// 日期选择
			animeDatKtYg(value) {
				this.kaitaiSelectYg()
			},
			// 查看权限
			quanxianBtn(row){
				this.userId = row.userId
				this.quanxianSelect()
				this.isshow = true
				this.isbtn = true
				this.dialogFormVisibleQx = true
			},
			// 获取员工权限
			quanxianSelect() {
				let storeId = this.$route.query.storeId
				this.tableDataLoading1 = true
				this.$http({
					url: this.$http.adornUrl('user/selectTearoomUserTimeByUserId'),
					method: 'get',
					params: this.$http.adornParams({
						'tearoomId': storeId,
						'userId': this.userId,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading1 = false
					let returnData = data.data;
					if(returnData.isDaijiesuan){
						returnData.isDaijiesuan = returnData.isDaijiesuan
					}else{
						returnData.isDaijiesuan = 2
					}
					if(returnData.isDiancanguanli){
						returnData.isDiancanguanli = returnData.isDiancanguanli
					}else{
						returnData.isDiancanguanli = 2
					}
					if(returnData.isDiange){
						returnData.isDiange = returnData.isDiange
					}else{
						returnData.isDiange = 2
					}
					if(returnData.isDingdanguanli){
						returnData.isDingdanguanli = returnData.isDingdanguanli
					}else{
						returnData.isDingdanguanli = 2
					}
					if(returnData.isHexiaotongji){
						returnData.isHexiaotongji = returnData.isHexiaotongji
					}else{
						returnData.isHexiaotongji = 2
					}
					if(returnData.isHuiyuanguanli){
						returnData.isHuiyuanguanli = returnData.isHuiyuanguanli
					}else{
						returnData.isHuiyuanguanli = 2
					}
					if(returnData.isHuiyuanquanyi){
						returnData.isHuiyuanquanyi = returnData.isHuiyuanquanyi
					}else{
						returnData.isHuiyuanquanyi = 2
					}
					if(returnData.isHuodongzhongxin){
						returnData.isHuodongzhongxin = returnData.isHuodongzhongxin
					}else{
						returnData.isHuodongzhongxin = 2
					}
					if(returnData.isKaitaishouyi){
						returnData.isKaitaishouyi = returnData.isKaitaishouyi
					}else{
						returnData.isKaitaishouyi = 2
					}
					if(returnData.isMendianhuiyuan){
						returnData.isMendianhuiyuan = returnData.isMendianhuiyuan
					}else{
						returnData.isMendianhuiyuan = 2
					}
					if(returnData.isPaihao){
						returnData.isPaihao = returnData.isPaihao
					}else{
						returnData.isPaihao = 2
					}
					if(returnData.isShangjiakaitai){
						returnData.isShangjiakaitai = returnData.isShangjiakaitai
					}else{
						returnData.isShangjiakaitai = 2
					}
					if(returnData.isShangjiakaitaitongji){
						returnData.isShangjiakaitaitongji = returnData.isShangjiakaitaitongji
					}else{
						returnData.isShangjiakaitaitongji = 2
					}
					if(returnData.isShichangtongji){
						returnData.isShichangtongji = returnData.isShichangtongji
					}else{
						returnData.isShichangtongji = 2
					}
					if(returnData.isShiwuhexiao){
						returnData.isShiwuhexiao = returnData.isShiwuhexiao
					}else{
						returnData.isShiwuhexiao = 2
					}
					if(returnData.isShouyi){
						returnData.isShouyi = returnData.isShouyi
					}else{
						returnData.isShouyi = 2
					}
					if(returnData.isShouyimingxi){
						returnData.isShouyimingxi = returnData.isShouyimingxi
					}else{
						returnData.isShouyimingxi = 2
					}
					if(returnData.isStatus){
						returnData.isStatus = returnData.isStatus
					}else{
						returnData.isStatus = 2
					}
					if(returnData.isTixian){
						returnData.isTixian = returnData.isTixian
					}else{
						returnData.isTixian = 2
					}
					if(returnData.isXiaofeipaihang){
						returnData.isXiaofeipaihang = returnData.isXiaofeipaihang
					}else{
						returnData.isXiaofeipaihang = 2
					}
					if(returnData.isYuangongguanli){
						returnData.isYuangongguanli = returnData.isYuangongguanli
					}else{
						returnData.isYuangongguanli = 2
					}
					if(returnData.isYuyin){
						returnData.isYuyin = returnData.isYuyin
					}else{
						returnData.isYuyin = 2
					}
					if(returnData.isZengsongyouhuijuan){
						returnData.isZengsongyouhuijuan = returnData.isZengsongyouhuijuan
					}else{
						returnData.isZengsongyouhuijuan = 2
					}
					if(returnData.isZhangdantongji){
						returnData.isZhangdantongji = returnData.isZhangdantongji
					}else{
						returnData.isZhangdantongji = 2
					}
					if(returnData.isZhuotaiguanli){
						returnData.isZhuotaiguanli = returnData.isZhuotaiguanli
					}else{
						returnData.isZhuotaiguanli = 2
					}
					if(returnData.isZhuotaishuju){
						returnData.isZhuotaishuju = returnData.isZhuotaishuju
					}else{
						returnData.isZhuotaishuju = 2
					}
					
					this.totalRevenueQx = returnData
				})
			},
			// 编辑
			updatesQx() {
				this.isshow = false
				this.isbtn = false
			},
			// 取消
			cancelQx() {
				this.isshow = true
				this.isbtn = true
				this.quanxianSelect()
			},
			// 保存
			saveClickQx() {
				
				this.$http({
					url: this.$http.adornUrl('user/adminUpdateTearoomUserTime'),
					method: 'post',
					data: this.$http.adornParams(this.totalRevenueQx)
				}).then(({
					data
				}) => {
					if(data.code==0){
						this.$message({
							message: '修改成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.isshow = true
								this.isbtn = true
								this.quanxianSelect()
							}
						})
					}else{
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {
							}
						})
					}
					
				})
			},
			// 上班记录
			shangbanBtn(row){
				this.userId = row.userId
				this.sbjlSelect()
				this.dialogFormVisibleSbjl= true
			},
			// 上班记录 
			sbjlSelect() {
				let storeId = this.$route.query.storeId
				this.tableDataLoading1 = true
				this.$http({
					url: this.$http.adornUrl('tearoomUserRecord/selectTearoomUserRecordList'),
					method: 'get',
					params: this.$http.adornParams({
						'tearoomId': storeId,
						'userId': this.userId,
						'page': this.page1,
						'limit': this.size1,
						
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading1 = false
					let returnData = data.data;
					
					this.totalRevenueSbjl = returnData
				})
			},
		},
		activated() {
			let activeName = this.$route.query.activeName
			if (activeName != undefined) {
				this.activeName = activeName

			}
			this.dataSelect()
			this.shopSelect()

		},
		beforeDestroy() {
			if (this.flvPlayer) {
				this.flvPlayer.destroy(); // 销毁播放器实例，释放资源
			}
		}

	}
</script>
<style scoped="scoped">
.merch-income-page__back {
	display: inline-flex;
	align-items: center;
	margin-bottom: 12px;
	font-size: 14px;
	color: #606266;
	text-decoration: none;
}

.merch-income-page__back:hover {
	color: #5cc477;
}

.merch-income-page__back-icon {
	width: 1.2em;
	height: 1.2em;
	margin-right: 4px;
}

.merch-income-page__tabs {
	margin-bottom: 0;
}

.merch-income-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.merch-income-page__tabs >>> .el-tabs__item.is-active {
	color: #5cc477;
}

.merch-income-page__tabs >>> .el-tabs__active-bar {
	background-color: #5cc477;
}

.merch-income-page__tabs >>> .el-tab-pane:not(:has(> .nf-table-card)) {
	background: #fff;
	border: 1px solid #e8eaed;
	border-radius: 8px;
	padding: 16px;
	box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
	margin-top: 12px;
}

.merch-income-page__tabs >>> .el-tab-pane:has(> .nf-table-card) {
	background: transparent;
	border: none;
	padding: 0;
	box-shadow: none;
	margin-top: 12px;
}

.merch-income-page__tabs .nf-table-card {
	margin-top: 0;
	margin-bottom: 0;
}

.merch-income-page__form-card {
	padding: 16px;
}

.merch-income-page__section-head {
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

.merch-income-page__section-icon {
	color: #5cc477;
	font-size: 16px;
}

.merch-income-page__toolbar {
	display: flex;
	justify-content: flex-end;
	flex-wrap: wrap;
	gap: 12px;
	margin-bottom: 12px;
}

.merch-income-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

.merch-income-page >>> .el-button--primary:not(.is-plain):not(.el-button--text) {
	background-color: transparent;
	border-color: #5cc477;
	color: #5cc477;
}

.merch-income-page >>> .el-button--primary:not(.is-plain):not(.el-button--text):hover,
.merch-income-page >>> .el-button--primary:not(.is-plain):not(.el-button--text):focus {
	background-color: #ebf8ef;
	border-color: #3fae5c;
	color: #3fae5c;
}

.merch-income-page >>> .el-table th {
	background: #f7f8fa;
}

.merch-income-page >>> .el-table td,
.merch-income-page >>> .el-table th {
	border-color: #e8eaed;
}

.merch-income-page >>> .el-table::before {
	background-color: #e8eaed;
}

.merch-income-page >>> .el-table__body tr:hover > td {
	background-color: #ebf8ef;
}

.imgs {
		position: relative;
		border-radius: 6px;
		width: 148px;
		height: 148px;
		margin-right: 10px;
		display: inline-block;
	}

	.dels {
		position: absolute;
		top: 0;
		left: 0;
		display: none;
	}

	.dels .el-icon-delete {
		line-height: 148px;
		padding-left: 58px;
		font-size: 25px;
		color: #fff;
	}

	.imgs:hover .dels {
		width: 100%;
		height: 100%;
		background: #000;
		display: block;
		opacity: 0.5;
	}

.menusStateTrue {
	background: #5cc477;
	color: white;
}

.menusStateTrue:hover {
	background: #3fae5c;
	color: white;
}

.content_item {
	margin-bottom: 16px;
	font-size: 14px;
}

.content_item > span:first-child {
	color: #909399;
}

.box {
	height: auto;
	min-height: 96px;
	padding: 16px;
	border: 1px solid #e8eaed;
	border-radius: 8px;
	margin: 0 0 12px;
	background: #f7f8fa;
	box-shadow: 0 1px 4px rgba(0, 0, 0, 0.04);
}

.box_num {
	font-size: 14px;
	color: #303133;
}

.box_num .box_color {
	color: #909399;
	font-size: 13px;
	margin-bottom: 8px;
}

.box_num div span {
	font-size: 22px;
	font-weight: 600;
	margin-left: 0;
}

.text_color {
	color: #5cc477;
}

.cards {
	padding: 0 6px;
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
		padding-top: 0;
		border-radius: 6px;
		background-color: rgba(0, 0, 0, .5);
		-webkit-transition: opacity .3s;
		transition: opacity .3s;
		z-index: 9999;
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
	padding-left: 0;
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