package com.financingplat.web.util;

import com.financingplat.web.entity.Encrypt;
import com.financingplat.web.entity.YunPianResult;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * @Description String 工具类
 * @Param
 * @return
 **/
public class StringHelper {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(StringHelper.class);

	/**
	 * @Description 返回按规则转化后的id
	 * @Param [idStr]
	 * @return java.lang.Integer
	 **/
	public static Integer convertStrToId(String idStr) {
        if(!StringUtils.isNumeric(idStr)) {
        	LOGGER.info("method convertStrToId idStr is null!");
            return 0;
        }
        String id1Str = "";
        String idsStr = "";
        String id2Str = "";
        Integer id = 0;
        if(idStr != null && !"".equals(idStr) && idStr.length() >= 10) {
            id1Str = idStr.substring(0, 3);
            id2Str = idStr.substring(idStr.length() - 3, idStr.length());
            idsStr = idStr.substring(3, idStr.length() - 3);
            if(idsStr.length() >= 10) {
				//注意string存放的数字不能大于int的最大值
				LOGGER.info("method convertStrToId idsStr length gt 10");
                return 0;
            }
            if(StringUtils.isNumeric(idsStr)) {
                id = (Integer.parseInt(idsStr) - 3241)/7;
            }
            Integer ids = 3241 + id * 7;
            Integer id1 = ids * 13 + 326;
            Integer id2 = ids * 17 + 538;
            String id11Str = id1.toString().substring(id1.toString().length() - 3, id1.toString().length());
            String id22Str = id2.toString().substring(id2.toString().length() - 3, id2.toString().length());
            if(id1Str != null && !"".equals(id1Str)
                    && id2Str != null && !"".equals(id2Str)
                    && id11Str != null && !"".equals(id11Str)
                    && id22Str != null && !"".equals(id22Str)
                    && id1Str.equals(id11Str) && id2Str.equals(id22Str)) {
                return id;
            } else {
				LOGGER.info("method convertStrToId idStr is null!");
                return 0;
            }
        } else {
			LOGGER.info("method convertStrToId idStr is null!");
            return 0;
        }
    }
	
	/**
	 * @Description convertIdToStr
	 * @Param [id]
	 * @return java.lang.String
	 **/
	public static String convertIdToStr(Integer id) {
		if(id == null) {
			return "";
		}
		Integer ids = 3241 + id * 7;
		Integer id1 = ids * 13 + 326;
		Integer id2 = ids * 17 + 538;
		String idsStr = ids + "";
		String id1Str = id1.toString().substring(id1.toString().length() - 3, id1.toString().length());
		String id2Str = id2.toString().substring(id2.toString().length() - 3, id2.toString().length());
		return id1Str + idsStr + id2Str;
	}
	
	/**
	 * @Description 随机生成除了数字4以外的指定长度的字符串
	 * @Param [length]
	 * @return java.lang.String
	 **/
	public static String getRandomExcept4IntegerString(int length) {
		// length表示生成字符串的长度
		String base = "012356789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		System.out.println("新的验证代码为"+sb.toString()+"-----------------------");
		return sb.toString();
	}
	
	/**
	 * @Description 将Json字符串转换为Java对象
	 * @Param [jsonStr, clazz]
	 * @return T
	 **/
	public static <T> T decodeAsObject(final String jsonStr,
			final Class<T> clazz) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(jsonStr, clazz);
	}

	/**
	 * @Description 将Json字符串转换为MAP
	 * @Param [json]
	 * @return T
	 **/
	public static Map<String, Object> jsonToMap(String json) throws IOException {
		Object obj = decodeAsObject(json, Object.class);
		@SuppressWarnings("unchecked")
		Map<String, Object> jsonMap = (Map<String, Object>) obj;
		return jsonMap;
	}

	/**
	 * @Description 脱敏
	 * @Date 14:36 2018/8/22
	 * @Param [content]
	 * @return java.lang.String
	 **/
	public static String keyWordReplace(String content) {
		String keywordStr = "3P,4P,av,AV,3级片,A片,fuck,牛逼,牛叉,日你妹,一夜情,三公百家乐,三级,三级片,上我,专政,中共,丰胸翘臀,九评,习近平,乱伦,乳头,乳房,乳晕,乳沟,二B,二逼,人兽,伦功,作爱,你妈的,借点钱,借钱,做爱,傻逼,共产党,兽交,冰毒,凸点,卖比,卖逼,博彩,卡上,去你妈的,双乳,双峰,双沟,发伦,发伦功,发抡,发抡功,发春,发浪,发论,发论公,发论功,发骚,受孕,叫床,台湾独立,台独,吗啡,吮吸,呻吟,咪咪,四级片,处女膜,多党,夜激情,大B,大b,大法,大纪元,大花逼,大逼,天安门屠杀,失身,女优,奸尸,奸淫,妈逼,妓女,娇喘,婊子"+
						",媚妙,宽带钻,射精,小鸡鸡,屁眼,巨乳,巨波,干你娘,干死你,廿一点,廿五点,开苞,强奸,强暴,彩票,彩钻,性交,性奴,性欲,性游戏,性爱,性虐待,情欲,情色,我操你,手淫,打钱,抡功,招妓,挂马,摇头丸,操他,操你,操你妈,操比,操蛋,操逼,支出宝,收徒,新疆独立,旗峰,日你,日你妈,日死你,晨风,暴乱,暴乳,暴动,暴奶,木马,杂种,李洪志,欲火,毛泽东,民主,民运,氵去,氵去车仑工力,汇款,江泽民,法*功,法lun功,法仑,法伦,法功,法十轮十功,法愣,法论,法谪,法轮,法轮功,法轮大法,波波,波霸,注射毒品,泵波拿"+
						",海洛因,消魂夺魄,淫乱,淫妇,淫娃,淫欲,淫穴,淫荡,淫贱,淫靡,烂比,烂逼,燥热,爱液,牌九,狂操,狗b,狗操,猥亵,玉体,生殖器,番摊,疆独,病毒,白鸽票,百家乐,真善忍,破处,禁片,私处,精液,红薯,红袖添香,约炮,绳虐,网赚,罩杯,美乳,群P,群奸,联奖扑克,肉棒,肉缝,肏,肛,肛交,胡锦涛,胴体,臀沟,自慰,自摸,自焚,色图,色情,色欲,色诱,艹,艹尼玛,花蕊,芳穴,草泥马,荡妇,藏独,虐待,虐猫,虐畜,蜜液,裸体,裸女,裸戏,西藏独立,角子机,诱奸,豪乳,贞操,账上,贱人,贱比,贱货,贱逼,贱鸡,赚钱,赤裸,走私,转法轮,轮大,轮奸,软件,逐浪网"+
						",逼样,邓小平,酥胸,酷玩,闷骚,阳具,阴唇,阴水,阴茎,阴蒂,阴道,雀九,青云,靠你妈,风骚,骚妇,骚妖,骚妹,骚货,骚蹄子,骰宝赌大,骰宝赌小,高潮,鱼虾蟹骰子,鸡奸,鸡巴,鸦片,麻将牌九,黑客,阿扁推翻,阿宾,阿賓,挨了一炮,爱液横流,安街逆,安局办公楼,安局豪华,安门事,安眠藥,案的准确,八九民,八九学,八九政治,把病人整,把邓小平,把学生整,罢工门,白黄牙签,败培训,办本科,办理本科,办理各种,办理票据,办理文凭,办理真实,办理证书,办理资格,办文凭,办怔,办证,半刺刀,辦毕业,辦證,谤罪获刑,磅解码器,磅遥控器,宝在甘肃修,保过答案,报复执法"+
						",爆发骚,北省委门,被打死,被指抄袭,被中共,本公司担,本无码,毕业證,变牌绝,辩词与梦,冰毒,冰火毒,冰火佳,冰火九重,冰火漫,冰淫传,冰在火上,波推龙,博彩娱,博会暂停,博园区伪,不查都,不查全,不思四化,布卖淫女,部忙组阁,部是这样,才知道只生,财众科技,采花堂,踩踏事,苍山兰,苍蝇水,藏春阁,藏獨,操了嫂,操嫂子,策没有不,插屁屁,察象蚂,拆迁灭,车牌隐,成人电,成人卡通,成人聊,成人片,成人视,成人图,成人文,成人小,城管灭,惩公安,惩贪难,充气娃,冲凉死,抽着大中,抽着芙蓉,出成绩付,出售发票,出售军"+
						",穿透仪器,春水横溢,纯度白,纯度黄,次通过考,催眠水,催情粉,催情药,催情藥,挫仑,达毕业证,答案包,答案提供,打标语,打错门,打飞机专,打死经过,打死人,打砸办公,大鸡巴,大雞巴,大纪元,大揭露,大奶子,大批贪官,大肉棒,大嘴歌,代办发票,代办各,代办文,代办学,代办制,代辦,代表烦,代開,代考,代理发票,代理票据,代您考,代您考,代写毕,代写论,代孕,贷办,贷借款,贷开,戴海静,当代七整,当官要精,当官在于,党的官,党后萎,党前干劲,刀架保安,导的情人,导叫失,导人的最,导人最,导小商,到花心,得财兼,的同修,灯草和,等级證,等屁民,等人老百"+
						",等人是老,等人手术,邓爷爷转,邓玉娇,地产之歌,地下先烈,地震哥,帝国之梦,递纸死,点数优惠,电狗,电话监,电鸡,甸果敢,蝶舞按,丁香社,丁子霖,顶花心,东北独立,东复活,东京热,東京熱,洞小口紧,都当警,都当小姐,都进中央,毒蛇钻,独立台湾,赌球网,短信截,对日强硬,多美康,躲猫猫,俄羅斯,恶势力操,恶势力插,恩氟烷,儿园惨,儿园砍,儿园杀,儿园凶,二奶大,发牌绝,发票出,发票代,发票销,發票,法车仑,法伦功,法轮,法轮佛,法维权,法一轮,法院给废,法正乾,反测速雷,反雷达测,反屏蔽,范燕琼,方迷香,防电子眼,防身药水,房贷给废,仿真枪,仿真证"+
						",诽谤罪,费私服,封锁消,佛同修,夫妻交换,福尔马林,福娃的預,福娃頭上,福香巴,府包庇,府集中领,妇销魂,附送枪,复印件生,复印件制,富民穷,富婆给废,改号软件,感扑克,冈本真,肛交,肛门是邻,岡本真,钢针狗,钢珠枪,港澳博球,港馬會,港鑫華,高就在政,高考黑,高莺莺,搞媛交,告长期,告洋状,格证考试,各类考试,各类文凭,跟踪器,工程吞得,工力人,公安错打,公安网监,公开小姐,攻官小姐,共狗,共王储,狗粮,狗屁专家,鼓动一些,乖乖粉,官商勾,官也不容,官因发帖,光学真题,跪真相,滚圆大乳,国际投注,国家妓,国家软弱,国家吞得,国库折,国一九五七"+
						",國內美,哈药直销,海访民,豪圈钱,号屏蔽器,和狗交,和狗性,和狗做,黑火药的,红色恐怖,红外透视,紅色恐,胡江内斗,胡紧套,胡錦濤,胡适眼,胡耀邦,湖淫娘,虎头猎,华国锋,华门开,化学扫盲,划老公,还会吹萧,还看锦涛,环球证件,换妻,皇冠投注,黄冰,浑圆豪乳,活不起,火车也疯,机定位器,机号定,机号卫,机卡密,机屏蔽器,基本靠吼,绩过后付,激情电,激情短,激情妹,激情炮,级办理,级答案,急需嫖,集体打砸,集体腐,挤乳汁,擠乳汁,佳静安定,家一样饱,家属被打,甲虫跳,甲流了,奸成瘾,兼职上门,监听器,监听王,简易炸,江胡内斗,江太上,江系人,江贼民"+
						",疆獨,蒋彦永,叫自慰,揭贪难,姐包夜,姐服务,姐兼职,姐上门,金扎金,金钟气,津大地震,津地震,进来的罪,京地震,京要地震,经典谎言,精子射在,警察被,警察的幌,警察殴打,警察说保,警车雷达,警方包庇,警用品,径步枪,敬请忍,究生答案,九龙论坛,九评共,酒象喝汤,酒像喝汤,就爱插,就要色,举国体,巨乳,据说全民,绝食声,军长发威,军刺,军品特,军用手,开邓选,开锁工具,開碼,開票,砍杀幼,砍伤儿,康没有不,康跳楼,考答案,考后付款,考机构,考考邓,考联盟,考前答,考前答案,考前付,考设备,考试包过,考试保,考试答案,考试机构,考试联盟,考试枪"+
						",考研考中,考中答案,磕彰,克分析,克千术,克透视,空和雅典,孔摄像,控诉世博,控制媒,口手枪,骷髅死,快速办,矿难不公,拉登说,拉开水晶,来福猎,拦截器,狼全部跪,浪穴,老虎机,雷人女官,类准确答,黎阳平,李洪志,李咏曰,理各种证,理是影帝,理证件,理做帐报,力骗中央,力月西,丽媛离,利他林,连发手,聯繫電,炼大法,两岸才子,两会代,两会又三,聊视频,聊斋艳,了件渔袍,好帮手,猎枪销,猎槍,獵槍,领土拿,流血事,六合彩,六死,六四事,六月联盟,龙湾事件,隆手指,陆封锁,陆同修,氯胺酮,乱奸,乱伦类,乱伦小,亂倫,伦理大,伦理电影,伦理毛,伦理片"+
						",轮功,轮手枪,论文代,罗斯小姐,裸聊网,裸舞视,落霞缀,麻古,麻果配,麻果丸,麻将透,麻醉狗,麻醉枪,麻醉槍,麻醉藥,蟆叫专家,卖地财政,卖发票,卖银行卡,卖自考,漫步丝,忙爱国,猫眼工具,毛一鲜,媒体封锁,每周一死,美艳少妇,妹按摩,妹上门,门按摩,门保健,門服務,氓培训,蒙汗药,迷幻型,迷幻药,迷幻藥,迷昏口,迷昏药,迷昏藥,迷魂香,迷魂药,迷魂藥,迷奸药,迷情水,迷情药,迷藥,谜奸药,蜜穴,灭绝罪,民储害,民九亿商,民抗议,明慧网,铭记印尼,摩小姐,母乳家,木齐针,幕没有不,幕前戲,内射,南充针,嫩穴,嫩阴,泥马之歌,你的西域,拟涛哥,娘两腿之间"+
						",妞上门,浓精,怒的志愿,女被人家搞,女激情,女技师,女人和狗,女任职名,女上门,女優,鸥之歌,拍肩神药,拍肩型,牌分析,牌技网,炮的小蜜,陪考枪,配有消,喷尿,嫖俄罗,嫖鸡,平惨案,平叫到床,仆不怕饮,普通嘌,期货配,奇迹的黄,奇淫散,骑单车出,气狗,气枪,汽狗,汽枪,氣槍,铅弹,钱三字经,枪出售,枪的参,枪的分,枪的结,枪的制,枪货到,枪决女犯,枪决现场,枪模,枪手队,枪手网,枪销售,枪械制,枪子弹,强权政府,强硬发言,抢其火炬,切听器,窃听器,禽流感了,勤捞致,氢弹手,清除负面,清純壆,情聊天室,情妹妹,情视频,情自拍,氰化钾,氰化钠,请集会,请示威"+
						",如厕死,乳交,软弱的国,赛后骚,三挫,三级片,三秒倒,三网友,三唑,骚妇,骚浪,骚穴,骚嘴,扫了爷爷,色电影,色妹妹,色视频,色小说,杀指南,山涉黑,煽动不明,煽动群众,上门激,烧公安局,烧瓶的,韶关斗,韶关玩,韶关旭,射网枪,涉嫌抄袭,深喉冰,神七假,神韵艺术,生被砍,生踩踏,生肖中特,圣战不息,盛行在舞,尸博,失身水,失意药,狮子旗,十八等,十大谎,十大禁,十个预言,十类人不,十七大幕,实毕业证,实体娃,实学历文,士康事件,式粉推,视解密,是躲猫,手变牌,手答案,手狗,手机跟,手机监,手机窃,手机追,手拉鸡,手木仓,手槍,守所死法,兽交,售步枪"+
						",售纯度,售单管,弹簧刀,售防身,售狗子,售虎头,售火药,售假币,售健卫,售军用,售猎枪,售氯胺,售麻醉,售冒名,售枪支,售热武,售三棱,售手枪,售五四,售信用,售一元硬,售子弹,售左轮,书办理,熟妇,术牌具,双管立,双管平,水阎王,丝护士,丝情侣,丝袜保,丝袜恋,丝袜美,丝袜妹,丝袜网,丝足按,司长期有,司法黑,私房写真,死法分布,死要见毛,四博会,四大扯,个四小码,苏家屯集,诉讼集团,素女心,速代办,速取证,酸羟亚胺,蹋纳税,太王四神,泰兴幼,泰兴镇中,泰州幼,贪官也辛,探测狗,涛共产,涛一样胡,特工资,特码,特上门,体透视镜,替考,替人体,天朝特"+
						",天鹅之旅,天推广歌,田罢工,田田桑,田停工,庭保养,庭审直播,通钢总经,偷電器,偷肃贪,偷听器,偷偷贪,头双管,透视功能,透视镜,透视扑,透视器,透视眼镜,透视药,透视仪,秃鹰汽,突破封锁,突破网路,推油按,脱衣艳,瓦斯手,袜按摩,外透视镜,外围赌球,湾版假,万能钥匙,万人骚动,王立军,王益案,网民案,网民获刑,网民诬,微型摄像,围攻警,围攻上海,维汉员,维权基,维权人,维权谈,委坐船,谓的和谐,温家堡,温切斯特,温影帝,溫家寶,瘟加饱,瘟假饱,文凭证,文强,纹了毛,闻被控制,闻封锁,瓮安,我的西域,我搞台独,乌蝇水,无耻语录,无码专,五套功,五月天"+
						",午夜电,午夜极,武警暴,武警殴,武警已增,务员答案,务员考试,雾型迷,西藏限,西服进去,希脏,习进平,习晋平,席复活,席临终前,席指着护,洗澡死,喜贪赃,先烈纷纷,现大地震,现金投注,线透视镜,限制言,陷害案,陷害罪,相自首,香港论坛,香港马会,香港一类,香港总彩,硝化甘,小穴,校骚乱,协晃悠,写两会,泄漏的内,新建户,新疆叛,新疆限,新金瓶,新唐人,信访专班,信接收器,兴中心幼,星上门,行长王益,形透视镜,型手枪,姓忽悠,幸运码,性爱日,性福情,性感少,性推广歌,胸主席,徐玉元,学骚乱,学位證,學生妹,丫与王益,烟感器,严晓玲,言被劳教,言论罪,盐酸曲"+
						",颜射,恙虫病,姚明进去,要人权,要射精了,要射了,要泄了,夜激情,液体炸,一小撮别,遗情书,蚁力神,益关注组,益受贿,阴间来电,陰唇,陰道,陰戶,淫魔舞,淫情女,淫肉,淫騷妹,淫兽,淫兽学,淫水,淫穴,隐形耳,隐形喷剂,应子弹,婴儿命,咏妓,用手枪,幽谷三,游精佑,有奶不一,右转是政,幼齿类,娱乐透视,愚民同,愚民政,与狗性,玉蒲团,育部女官,冤民大,鸳鸯洗,园惨案,园发生砍,园砍杀,园凶杀,园血案,原一九五七,原装弹,袁腾飞,晕倒型,韵徐娘,遭便衣,遭到警,遭警察,遭武警,择油录,曾道人,炸弹教,炸弹遥控,炸广州,炸立交,炸药的制,炸药配,炸药制,张春桥"+
						",找枪手,找援交,找政法委副,赵紫阳,针刺案,针刺伤,针刺事,针刺死,侦探设备,真钱斗地,真钱投注真善忍,真实文凭,真实资格,震惊一个民,震其国土,证到付款,证件办,证件集团,证生成器,证书办,证一次性,政府操,政论区,證件,植物冰,殖器护,指纹考勤,指纹膜,指纹套,至国家高,志不愿跟,制服诱,制手枪,制证定金,制作证件,中的班禅,中共黑,中国不强,种公务员,种学历证,众像羔,州惨案,州大批贪,州三箭,宙最高法,昼将近,主席忏,住英国房,助考,助考网,专业办理,专业代,专业代写,专业助,转是政府,赚钱资料,装弹甲,装枪套,装消音,着护士的胸,着涛哥,姿不对死"+
						",资格證,资料泄,梓健特药,字牌汽,自己找枪,自慰用,自由圣,自由亚,总会美女,足球玩法,最牛公安,醉钢枪,醉迷药,醉乙醚,尊爵粉,左转是政,作弊器,作各种证,作硝化甘,唑仑,做爱小,做原子弹,做证件,请愿,琼花问,区的雷人,娶韩国,全真证,群奸暴,群起抗暴,群体性事,绕过封锁,惹的国,人权律,人体艺,人游行,人在云上,人真钱,认牌绝,任于斯国,柔胸粉,肉洞,肉棍";
		String[] keywords = keywordStr.split(",");
		for (String string : keywords) {
			if(content.contains(string)){
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < string.length(); i++) {
					sb.append("<span style=\"color:red;\">*</span>");
				}
				content = content.replace(string, sb.toString());
			}
		}
		return content.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
	}


	/**
	 * @Description 隐藏用户真实姓名
	 * @Param [name, gender]
	 * @return java.lang.String
	 **/
	public static String returnFistNameWithGender(String name, Integer gender) {
		String nickName = "";
		if(StringUtils.isNotBlank(name)){
			switch (gender) {
			case 0:
				nickName = name.substring(0, 1) + "先生";
				break;
			case 1:
				nickName = name.substring(0, 1) + "先生";
				break;
			case 2:
				nickName = name.substring(0, 1) + "女士";
				break;
			default:
				nickName = "用户";
				break;
			}
		} else {
			nickName = "用户";
		}
		return nickName;
	}

	/**
	 * @Description 时间_6位随机数
	 * @Date 14:44 2018/8/22
	 * @Param []
	 * @return java.lang.String
	 **/
	public static String generateRandomStr() {
		SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddHHmmssSSS");
		String dateStr = sdf.format(new Date());
		return dateStr + "_" + StringHelper.getRandomString(6);
	}

	/**
	 * @Description 随机数[a-z,0-9]
	 * @Date 14:45 2018/8/22
	 * @Param [length]
	 * @return java.lang.String
	 **/
	public static String getRandomString(int length) {
		// length表示生成字符串的长度
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	/**
	 * @Description 100000~200000-->10万~20万
	 * @Param [minAm, maxAm]
	 * @return java.lang.String
	 **/
	public static String getInvestAmount(Integer minAm, Integer maxAm) {
		if(minAm != null && maxAm != null) {
			return getProjectAmount(minAm) + "&nbsp;~&nbsp;" + getProjectAmount(maxAm);
		} else {
			return "保密";
		}
	}
	
	/**
	 * @Description 数字格式转换
	 * @Param [financeAmount]
	 * @return java.lang.String
	 **/
	public static String getProjectAmount(Integer financeAmount) {
		String result = "保密";
		if(financeAmount != null) {
			if(financeAmount >= 10000) {
				BigDecimal amount = new BigDecimal(financeAmount);
				amount = amount.divide(new BigDecimal(10000), 2, BigDecimal.ROUND_HALF_UP);
				if(amount.compareTo(new BigDecimal(amount.intValue())) == 0){
					result = amount.intValue() + "&nbsp;亿";
				} else {
					result = amount.doubleValue() + "&nbsp;亿";
				}
			} else { 
				result = financeAmount + "&nbsp;万";
			}
			return result;
		}
		
		return result;
	}
	/**
	 * @Description 将字符串转换为set,按空格截取
	 * @Param [label]
	 * @return java.util.Set<java.lang.String>
	 **/
	public static Set<String> getLabelSet(String label) {
		Set<String> tempSet = new TreeSet<String>();
		if(StringUtils.isNotBlank(label)){
			label = label.replaceAll(" ", ",").replaceAll("，", ",").replaceAll("、", ",");
			for(String labelStr : label.split(",")) {
				tempSet.add(labelStr);
			}
		}
		return tempSet;
	}

	/**
	 *  定义script的正则表达式
	 **/
	private static final String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>";
	/**
	 *  定义style的正则表达式
	 **/
    private static final String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>";
	/**
	 *  定义HTML标签的正则表达式
	 **/
    private static final String regEx_html = "<[^>]+>";
	/**
	 *  定义空格回车换行符
	 **/
    private static final String regEx_space = "\\s*|\t|\r|\n";
      

    /**
     * @Description 删除Html标签
     * @Param [htmlStr]
     * @return java.lang.String
     **/
    public static String delHTMLTag(String htmlStr) {  
        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);  
        Matcher m_script = p_script.matcher(htmlStr);
		// 过滤script标签
        htmlStr = m_script.replaceAll("");
        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
		// 过滤style标签
        htmlStr = m_style.replaceAll("");
        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
		// 过滤html标签
        htmlStr = m_html.replaceAll("");
        Pattern p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);
        Matcher m_space = p_space.matcher(htmlStr);
		// 过滤空格回车标签
        htmlStr = m_space.replaceAll("");
		// 返回文本字符串
        htmlStr = htmlStr.trim();
        htmlStr = htmlStr.replaceAll(" ", ""); 
        return htmlStr;
    } 
    
    /**
     * @Description 根绝参数url生成 六位随机字符串
     * @Param [url]
     * @return java.lang.String
     **/
	public static String createShortUrl(String url)  
    {  
        // 可以自定义生成 MD5 加密字符传前的混合 KEY  
        String key = "mingshiKEJI";  
        // 要使用生成 URL 的字符  
        String[] chars = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p",  
                "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",  
                "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",  
                "U", "V", "W", "X", "Y", "Z"  
  
        };  
        // 对传入网址进行 MD5 加密  
        String sMD5EncryptResult = Encrypt.md5(key + url);
        String hex = sMD5EncryptResult;  
  
        String[] resUrl = new String[4];  
        for (int i = 0; i < 4; i++)  
        {  
            // 把加密字符按照 8 位一组 16 进制与 0x3FFFFFFF 进行位与运算
            String sTempSubString = hex.substring(i * 8, i * 8 + 8);  
            // 这里需要使用 long 型来转换，因为 Inteper .parseInt() 只能处理 31 位 , 首位为符号位 , 如果不用
            // long ，则会越界  
            long lHexLong = 0x3FFFFFFF & Long.parseLong(sTempSubString, 16);  
            String outChars = "";  
            for (int j = 0; j < 6; j++)  
            {  
                // 把得到的值与 0x0000003D 进行位与运算，取得字符数组 chars 索引  
                long index = 0x0000003D & lHexLong;  
                // 把取得的字符相加  
                outChars += chars[(int) index];  
                // 每次循环按位右移 5 位  
                lHexLong = lHexLong >> 5;  
            }  
            // 把字符串存入对应索引的输出数组  
            resUrl[i] = outChars;  
        }  
        int j = new Random().nextInt(3);
        return resUrl[j];  
    }  
	
	/**
	 * @Description 将textarea中换行“\r\n”替换为“<br/>” 空格替换“&nbsp;”
	 * @Param [str]
	 * @return java.lang.String
	 **/
	public static String textareaStringToDiv(String str) {
		if (StringUtils.isNotBlank(str)) {
			return str.replaceAll("\r\n", "</p><p>").replaceAll(" ", "");
		} else {
			return "";
		}
	}
}


