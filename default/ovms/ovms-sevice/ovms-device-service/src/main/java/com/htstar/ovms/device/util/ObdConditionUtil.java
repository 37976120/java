package com.htstar.ovms.device.util;

import com.htstar.ovms.common.core.util.ByteDataUtil;
import com.htstar.ovms.device.protoco.ConditionModel;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * Description:
 * Author: flr
 * Date: Created in 2020/6/22
 * Company: 航通星空
 * Modified By:
 */
@UtilityClass
@Slf4j
public class ObdConditionUtil {
    public ConditionModel getConditionModel(String pid,byte[] data,int index){
        ConditionModel cond = new ConditionModel();
        switch (pid) {

            case "2100":// 原始数据
            case "2120":// 原始数据
            case "2140":// 原始数据
            case "215f":// 车辆设计的排放要求 预留
            case "2160":// 原始数据
            case "2101":// 故障码清除之后的监测状态 U8[4]
            case "2141":// 当前驾驶循环的监测状态
            case "9fff":// 就绪状态
                cond.setByteLenght(8);
                break;
            // float 乘用车PID
            case "005b":// 加速踏板位置
            case "0060":// 剩余油量
            case "00b6":// 行程油耗
            case "00b7":// 瞬时油耗
            case "00f5":// 低分辨率总里程
            case "00fa":// 总油耗
            case "0084":// 空气流量
            case "0396":// 高分辨率的行程里程
            case "0664":// 高精度进气温度
            case "0665":// 高精度发动机冷却液的温度
            case "03ce":// 远程加速踏板位置
            case "001d":// 加速踏板位置2
            case "0d1d":// 实际最大可用发动机扭矩百分比
            case "046e":// 发动机中冷器电热调节器开放
            case "0016":// 发动机曲轴箱漏气压力
            case "0062":// 发动机机油位置
            case "006F":// 发动机冷却液位置
            case "006b":// 发动机空气滤清器1压力差
            case "0070":// 发动机冷却液过滤器压力差
            case "0209":// 制动踏板位置
            case "0395":// 高分辨率的总里程
            case "00f4":// 低分辨率行程里程
            case "00f7":// 发动机运转的总时间
            case "00b8":// 发动机瞬时燃油消耗率
            case "00b9":// 发动机平均燃油消耗率
            case "00ec":// 发动机总怠速消耗燃油
            case "03e9":// 使用燃油驱动旅程
            case "03ea":// 使用燃油动力输出装置移动旅程
            case "03eb":// 旅程动力输出装置非工作燃油消耗
            case "03ec":// 旅程车辆怠速燃油消耗
            case "03ed":// 旅程巡航燃油消耗
            case "03ee":// 旅程行驶燃油经济性
            case "0404":// 总的发动机动力输出装置消耗燃油
            case "0405":// 旅程平均燃油消耗率
            case "10e0": // 进入辅助马达电流
            case "1150": // 发动机 ECU 温度
            case "1160": // 外伸的曲轴箱漏气压力
            case "1170": // 发电机机油压力
            case "1250": // 变速器箱空气压力
            case "1510": // 微粒捕集器进口压力
            case "15f0": // 燃油过滤器压力差
            case "1630": // 发动机机油过滤器压力差
            case "1640": // 发动机机油压力
            case "1680": // 涡轮机油压力
            case "16b0": // 空气过滤器压力差
            case "1700": // 冷却液过滤器压力差
            case "1720": // 电瓶净电流
            case "1730": // 发电机电流
            case "1740": // 制动实施压力
            case "1750": // 制动初级压力
            case "1760": // 制动次级压力
            case "1770": // 液压减速器压力
            case "17b0": // 离合器压力
            case "17f0": // 变速器机油压力
            case "1820": // 功率燃油经济性
            case "1830": // 排气背压
            case "1840": // 空气流量
            case "1850": // 平均油耗
            case "1870": // 扩展范围的供油压力(绝对)
            case "1880": // 辅助真空压力读数
            case "1890": // 辅助表压力读数#1
            case "18a0": // 辅助绝对压力读数
            case "18d0": // 拖车#1, 随动桥#1,或推进通道#1轮胎压力-目标
            case "18e0": // 驱动通道轮胎压力目标
            case "18f0": // 转向通道轮胎压力目标
            case "1900": // 拖车#1,随动桥#1,或推进通道#1轮胎压力
            case "1910": // 驱动桥通道轮胎压力
            case "1920": // 转向桥通道轮胎压力
            case "1930": // 平均燃油经济性(天然气)
            case "1940": // 瞬时燃油经济性(天然气
            case "1950": // 空气流量(天然气)
            case "1990": // 曲轴箱压力
            case "19c0": // 喷油器正时轨压力
            case "19d0": // 喷油器计量轨压力
            case "19e0": // 电池电压—切换
            case "19f0": // 供气压力
            case "1a40": // 喷射控制压力
            case "1a50": // 罗盘方位
            case "1a70": // 发电机电压
            case "1a80": // 蓄电池电压
            case "1a90": // 货箱环境温度
            case "1aa0": // 驾驶室内部温度
            case "1ab0": // 环境气温
            case "1ac0": // 进气口温度
            case "1ad0": // 排气温度
            case "1ae0": // 燃油温度
            case "1af0": // 发动机机油温度
            case "1b00": // 涡轮增压器机油温度
            case "1b10": // 变速器#1 机油温度
            case "1b60": // 旅程油耗
            case "1b70": // 瞬时油耗
            case "1b80": // 瞬时燃油经济性
            case "1b90": // 平均燃油经济性
            case "1f40": // 旅程
            case "1f50": // 总行程
            case "1f60": // 车辆行驶总时间
            case "1f70": // 总发动机时间-小时
            case "1f80": // 总取力器工作时间-小时
            case "1fa0": // 总油耗
            // S32 乘用车PID
            case "0392":// 剩余保养里程
            case "14f0":// 路面温度
            case "15d0":// 输出扭矩
            // U32 乘用车PID
            case "00f9":// 发动机总转数
            case "1120":// 扩展范围的燃油压力
            case "1130":// 扩展范围的发动机机油压力
            case "12e0":// 车辆湿箱压力
            case "1490": // 辅助水泵压力
            case "14c0": // 车桥#1提升空气压力
            case "1520": // 空气启动压力
            case "15e0": // 供油压力
            case "1670": // 涡轮转速
            case "1b20": // 前桥重量
            case "1b30": // 后桥重量
            case "1b40": // 拖车重量
            case "1b50": // 货物重量
            case "1e40": // 速度传感器标定
            case "1e50": // 总油耗(天然气)
            case "1e60": // 总怠速油耗(天然气)
            case "1e70": // 旅程油耗(天然气)
            case "1eb0": // 总怠速时间-小时
            case "1ec0": // 总怠速油耗
            case "1f90": // 总的发动机转数
            case "2102":// 对应所存储的冻结桢的故障码
            case "2103":// 燃油系统状态
            case "2104":// 计算负荷值
                cond.setByteLenght(1);
                break;
            case "2106":// 短时燃油修正(气缸列1和3)
                cond.setByteLenght(1);
                break;
            case "2107":// 长期燃油修正(气缸列1和3)
                cond.setByteLenght(1);
                break;
            case "2108":// 短时燃油修正(气缸列2和4)
                cond.setByteLenght(1);
                break;
            case "2109":// 长期燃油修正(气缸列2和4)
                cond.setByteLenght(1);
                break;
            case "210b":// 进气歧管绝对压力
                cond.setByteLenght(1);
                break;
            case "210d":// 车速
                byte[] data_0x210d = ByteDataUtil.bytesFromBytes(data, index, 1);
                int speed = data_0x210d[0] & 0xFF;
                cond.setConValue(String.valueOf(speed));
                cond.setByteLenght(1);
                break;

            case "2111":// 绝对节气门位置
                cond.setByteLenght(1);
                break;
            case "211c": // OBD系统的车辆设计要求
                cond.setByteLenght(1);
                break;
            case "211e": // 辅助输入状态
                cond.setByteLenght(1);
                break;
            case "212c": // EGR指令开度
                cond.setByteLenght(1);
                break;
            case "212e": // 蒸发冲洗控制指令
                cond.setByteLenght(1);
                break;
            case "212f": // 燃油液位输入
                byte[] data_0x212f = ByteDataUtil.bytesFromBytes(data, index, 1);
                int data_212f = (int) data_0x212f[0];
                cond.setConValue(String.valueOf(data_212f));
                cond.setByteLenght(1);
                break;

            case "2130": // 自故障码被清除之后经历的暖机循环次数
                cond.setByteLenght(1);
                break;
            case "2133": // 大气压
                cond.setByteLenght(1);
                break;
            case "2143": // 绝对负荷值
                cond.setByteLenght(1);
                break;
            case "2145": // 相对节气门位置
                cond.setByteLenght(1);
                break;
            case "2147": // 绝对节气门位置B
                cond.setByteLenght(1);
                break;
            case "2148": // 绝对节气门位置C
                cond.setByteLenght(1);
                break;
            case "2149": // 加速踏板位置D
                cond.setByteLenght(1);
                break;
            case "214a": // 加速踏板位置E
                cond.setByteLenght(1);
                break;
            case "214b": // 加速踏板位置F
                cond.setByteLenght(1);
                break;
            case "214c": // 节气门执行器控制指令
                cond.setByteLenght(1);
                break;
            case "2151": // 当前车辆使用的燃料类型
                byte[] data_0x2151 = ByteDataUtil.bytesFromBytes(data, index, 1);
                int data_2151 = (int) data_0x2151[0];
                cond.setConValue(String.valueOf(data_2151));
                cond.setByteLenght(1);
                break;

            case "2152": // 酒精在燃料的百分比
                cond.setByteLenght(1);
                break;
            case "215a": // 加速踏板相对位置
                cond.setByteLenght(1);
                break;
            case "215b": // 混合动力电池剩余寿命
                cond.setByteLenght(1);
                break;
            // 商用车PID数据定义
            case "0054": // 车速
                cond.setByteLenght(1);
                break;
            case "005c": // 当前转速下发动机负荷百分比
                cond.setByteLenght(1);
                break;
            case "05cb": // 发动机控制装置源地址
                cond.setByteLenght(1);
                break;
            case "0056": // 巡航控制设置速度
                cond.setByteLenght(1);
                break;
            case "0051": // 发动机微粒捕集器进口压力
                cond.setByteLenght(1);
                break;
            case "1081": // 制动系统空气压力低警告开关状态-应急蓄压器
                cond.setByteLenght(1);
                break;
            case "1082": // 制动系统空气压力低警告开关状态-行车制动蓄压器
                cond.setByteLenght(1);
                break;
            case "1083": // 制动系统空气压力低警告开关状态-副蓄压器
                cond.setByteLenght(1);
                break;
            case "1084": // 制动系统空气压力低警告开关状态-主蓄压器
                cond.setByteLenght(1);
                break;
            case "1091": // 车桥提升状态-车桥提升位置
                cond.setByteLenght(1);
                break;
            case "1092": // 车桥提升状态-车桥提升开关状态
                cond.setByteLenght(1);
                break;
            case "10a1": // 车桥滑套状态-车桥滑套锁止状态
                cond.setByteLenght(1);
                break;
            case "10a2": // 车桥滑套状态-车桥滑套锁止开关状态
                cond.setByteLenght(1);
                break;
            case "10d0": // 进入辅助位置/展开
                cond.setByteLenght(1);
                break;
            case "1110": // 发动机机油位置远处存储器
                cond.setByteLenght(1);
                break;
            case "1180": // 发电机冷却液温度
                cond.setByteLenght(1);
                break;
            case "1191": // 空调系统状态 #2-压缩机出口侧
                cond.setByteLenght(1);
                break;
            case "1192": // 空调系统状态 #2-压缩机出口侧
                cond.setByteLenght(1);
                break;
            case "1193": // 空调系统状态 #2-压缩机吸口侧
                cond.setByteLenght(1);
                break;
            case "1194": // 空调系统状态 #2-蒸发器温度
                cond.setByteLenght(1);
                break;
            case "11a0": // 估计的风扇速度百分比
                cond.setByteLenght(1);
                break;
            case "11b0": // 废气再循环阀 #1 位置百分比
                cond.setByteLenght(1);
                break;
            case "11c0": // 加速踏板位置 #3 百分比
                cond.setByteLenght(1);
                break;
            case "11d0": // 加速踏板位置 #2 百分比
                cond.setByteLenght(1);
                break;
            case "11f0": // 变速器档位位置
                cond.setByteLenght(1);
                break;
            case "1200": // 变速器分离器位置
                cond.setByteLenght(1);
                break;
            case "1210": // 离合器缸位置
                cond.setByteLenght(1);
                break;
            case "1221": // 离合器缸执行器状态-粗略啮合执行器状态
                cond.setByteLenght(1);
                break;
            case "1222": // 离合器缸执行器状态-精确啮合执行器状态
                cond.setByteLenght(1);
                break;
            case "1223": // 离合器缸执行器状态-粗略啮合执行器状态
                cond.setByteLenght(1);
                break;
            case "1224": // 离合器缸执行器状态-精确啮合执行器状态
                cond.setByteLenght(1);
                break;
            case "1231": // 换档拨叉执行器状态 #2-档位执行器 #3 状态
                cond.setByteLenght(1);
                break;
            case "1232": // 换档拨叉执行器状态 #2-变速器轨执行器 #3状态
                cond.setByteLenght(1);
                break;
            case "1240": // 离合器盘磨损情况
                cond.setByteLenght(1);
                break;
            case "1260": // 第二燃油液位（右侧）
                cond.setByteLenght(1);
                break;
            case "1270": // 轮胎压力检查间隔
                cond.setByteLenght(1);
                break;
            case "1281": // 发动机减速器开关状态-发动机减速器级别开关
                cond.setByteLenght(1);
                break;
            case "1282": // 发动机减速器开关状态-发动机减速器开关
                cond.setByteLenght(1);
                break;
            case "1291": // 巡航控制开关状态-巡航控制开/关开关状态
                cond.setByteLenght(1);
                break;
            case "1292": // 巡航控制开关状态-巡航控制设置开关状态
                cond.setByteLenght(1);
                break;
            case "1293": // 巡航控制开关状态-巡航控制恢复开关状态
                cond.setByteLenght(1);
                break;
            case "12a0": // Tire pressure supply switch status
                cond.setByteLenght(1);
                break;
            case "12b1": // 点火开关状态-启动辅助触点状态
                cond.setByteLenght(1);
                break;
            case "12b2": // 点火开关状态-启动触点状态
                cond.setByteLenght(1);
                break;
            case "12b3": // 点火开关状态-运行触点状态
                cond.setByteLenght(1);
                break;
            case "12b4": // 点火开关状态-附件触点状态
                cond.setByteLenght(1);
                break;
            case "12c1": // 注意/警告指示灯状态-保护灯状态
                cond.setByteLenght(1);
                break;
            case "12c2": // 注意/警告指示灯状态-琥珀色灯状态
                cond.setByteLenght(1);
                break;
            case "12c3": // 注意/警告指示灯状态-红色灯状态
                cond.setByteLenght(1);
                break;
            case "12d1": // 进气加热器状态-等待启动灯
                cond.setByteLenght(1);
                break;
            case "12d2": // 进气加热器状态-加热器2状态
                cond.setByteLenght(1);
                break;
            case "12d3": // 进气加热器状态-加热器1状态
                cond.setByteLenght(1);
                break;
            case "12f0": // 减速器状态
                cond.setByteLenght(1);
                break;
            case "1300": // 大气压力扩展范围
                cond.setByteLenght(1);
                break;
            case "1311": // ABS 控制状态-ABS 越野功能开关
                cond.setByteLenght(1);
                break;
            case "1312": // ABS 控制状态-ABS 减速器控制
                cond.setByteLenght(1);
                break;
            case "1313": // ABS 控制状态-ABS 制动控制
                cond.setByteLenght(1);
                break;
            case "1314": // ABS 控制状态-ABS 警告灯
                cond.setByteLenght(1);
                break;
            case "1321": // 空调系统状态/指令 #1-发动机风扇请求
                cond.setByteLenght(1);
                break;
            case "1322": // 空调系统状态/指令 #1-空调请求输入
                cond.setByteLenght(1);
                break;
            case "1323": // 空调系统状态/指令 #1-请求离合器暂时脱离
                cond.setByteLenght(1);
                break;
            case "1324": // 空调系统状态/指令 #1-离合器啮合状态
                cond.setByteLenght(1);
                break;
            case "1330": // 节气门位置
                cond.setByteLenght(1);
                break;
            case "1340": // 发动机中冷器温度
                cond.setByteLenght(1);
                break;
            case "1350": // 变速器同步器离合器值
                cond.setByteLenght(1);
                break;
            case "1360": // 变速器同步器制动值
                cond.setByteLenght(1);
                break;
            case "1371": // 换档拨叉位置状态-中间变速轨感测
                cond.setByteLenght(1);
                break;
            case "1372": // 换档拨叉位置状态-前/后感测
                cond.setByteLenght(1);
                break;
            case "1373": // 换档拨叉位置状态-空挡感测
                cond.setByteLenght(1);
                break;
            case "1381": // 变速器档位开关状态-低档感测
                cond.setByteLenght(1);
                break;
            case "1382": // 变速器档位开关状态-高档感测
                cond.setByteLenght(1);
                break;
            case "1391": // 变速器执行器状态 #2-惯性制动执行器状态
                cond.setByteLenght(1);
                break;
            case "1392": // 变速器执行器状态 #2-减油执行器状态
                cond.setByteLenght(1);
                break;
            case "1393": // 变速器执行器状态 #2-锁止离合器执行器状态
                cond.setByteLenght(1);
                break;
            case "1394": // 变速器执行器状态 #2-离合器执行器状态
                cond.setByteLenght(1);
                break;
            case "13a1": // 换档拨叉执行器状态-档位执行器#2状态
                cond.setByteLenght(1);
                break;
            case "13a2": // 换档拨叉执行器状态-变速轨执行器#2状态
                cond.setByteLenght(1);
                break;
            case "13a3": // 换档拨叉执行器状态-档位执行器 #1状态
                cond.setByteLenght(1);
                break;
            case "13a4": // 换档拨叉执行器状态-变速轨执行器 #1 状态
                cond.setByteLenght(1);
                break;
            case "13b0": // 换档拨叉档位
                cond.setByteLenght(1);
                break;
            case "13c0": // 换档拨叉变速轨位置
                cond.setByteLenght(1);
                break;
            case "13d1": // 驻车制动执行器状态-驻车制动关闭执行器状态
                cond.setByteLenght(1);
                break;
            case "13d2": // 驻车制动执行器状态-驻车制动打开执行器状态
                cond.setByteLenght(1);
                break;
            case "13e0": // 减速器限制状态
                cond.setByteLenght(1);
                break;
            case "13f1": // 变速器执行器状态 #1-分配器间接执行器状态
                cond.setByteLenght(1);
                break;
            case "13f2": // 变速器执行器状态 #1-分配器直接执行器状态
                cond.setByteLenght(1);
                break;
            case "13f3": // 变速器执行器状态 #1-低档位执行器状态
                cond.setByteLenght(1);
                break;
            case "13f4": // 变速器执行器状态 #1-高档位执行器状态
                cond.setByteLenght(1);
                break;
            case "1401": // 方向开关状态-前进档开关状态
                cond.setByteLenght(1);
                break;
            case "1402": // 方向开关状态-空挡开关状态
                cond.setByteLenght(1);
                break;
            case "1403": // 方向开关状态-倒档开关状态
                cond.setByteLenght(1);
                break;
            case "1411": // 制动开关状态-制动系统开关状态
                cond.setByteLenght(1);
                break;
            case "1412": // 制动开关状态-行车制动开关状态
                cond.setByteLenght(1);
                break;
            case "1421": // 车辆起作用部件状态-电源连接装置状态
                cond.setByteLenght(1);
                break;
            case "1422": // 车辆起作用部件状态-启动作用装 置状态
                cond.setByteLenght(1);
                break;
            case "1423": // 车辆起作用部件状态-点火开关状态
                cond.setByteLenght(1);
                break;
            case "1431": // 换档请求开关状态-降档开关状态
                cond.setByteLenght(1);
                break;
            case "1432": // 换档请求开关状态-升档开关状态
                cond.setByteLenght(1);
                break;
            case "1440": // 扭矩限制因素
                cond.setByteLenght(1);
                break;
            case "1450": // 双速车桥开关状态
                cond.setByteLenght(1);
                break;
            case "1460": // 驻车制动开关状态
                cond.setByteLenght(1);
                break;
            case "1471": // 怠速关闭定时器状态
                cond.setByteLenght(1);
                break;
            case "1472": // 怠速关闭定时器功能
                cond.setByteLenght(1);
                break;
            case "1473": // 怠速关闭定时器超出
                cond.setByteLenght(1);
                break;
            case "1474": // 发动机已经被怠速定时器关闭
                cond.setByteLenght(1);
                break;
            case "1475": // 司机提醒模式
                cond.setByteLenght(1);
                break;
            case "1480": // 鼓风机旁通阀位置
                cond.setByteLenght(1);
                break;
            case "14a0": // 最高车速限制
                cond.setByteLenght(1);
                break;
            case "1500": // 洗涤器液体位置
                cond.setByteLenght(1);
                break;
            case "1530": // 车速限制状态
                cond.setByteLenght(1);
                break;
            case "1540": // 车速
                cond.setByteLenght(1);
                break;
            case "1551": // 巡航控制状态-巡航模式
                cond.setByteLenght(1);
                break;
            case "1552": // 巡航控制状态-离合器开关
                cond.setByteLenght(1);
                break;
            case "1553": // 巡航控制状态-制动开关
                cond.setByteLenght(1);
                break;
            case "1554": // 巡航控制状态-加速开关
                cond.setByteLenght(1);
                break;
            case "1555": // 巡航控制状态-恢复开关
                cond.setByteLenght(1);
                break;
            case "1556": // 巡航控制状态-滑行开关
                cond.setByteLenght(1);
                break;
            case "1557": // 巡航控制状态-设置开关
                cond.setByteLenght(1);
                break;
            case "1558": // 巡航控制状态-巡航控制开关
                cond.setByteLenght(1);
                break;
            case "1560": // 巡航控制设置速度
                cond.setByteLenght(1);
                break;
            case "1570": // 巡航控制设置限制速度-高速
                cond.setByteLenght(1);
                break;
            case "1580": // 巡航控制设置限制速度-低速
                cond.setByteLenght(1);
                break;
            case "1591": // 取力器状态-取力器模式
                cond.setByteLenght(1);
                break;
            case "1592": // 取力器状态-离合器开关
                cond.setByteLenght(1);
                break;
            case "1593": // 取力器状态-制动开关
                cond.setByteLenght(1);
                break;
            case "1594": // 取力器状态-加速开关
                cond.setByteLenght(1);
                break;
            case "1595": // 取力器状态-恢复开关
                cond.setByteLenght(1);
                break;
            case "1596": // 取力器状态-滑行开关
                cond.setByteLenght(1);
                break;
            case "1597": // 取力器状态-设置开关
                cond.setByteLenght(1);
                break;
            case "1598": // 取力器状态-取力器控制开关
                cond.setByteLenght(1);
                break;
            case "15b0": // 加速踏板位置百分比
                cond.setByteLenght(1);
                break;
            case "15c0": // 发动机负荷百分比
                cond.setByteLenght(1);
                break;
            case "1600": // 燃油位
                cond.setByteLenght(1);
                break;
            case "1610": // 燃油含水指示器
                cond.setByteLenght(1);
                break;
            case "1620": // 发动机机油位置
                cond.setByteLenght(1);
                break;
            case "1660": // 增压压力
                cond.setByteLenght(1);
                break;
            case "1690": // 进气歧管温度
                cond.setByteLenght(1);
                break;
            case "16c0": // 大气压力
                cond.setByteLenght(1);
                break;
            case "16d0": // 冷却液压力
                cond.setByteLenght(1);
                break;
            case "16e0": // 发动机冷却液温度
                cond.setByteLenght(1);
                break;
            case "16f0": // 冷却液液位
                cond.setByteLenght(1);
                break;
            case "1791": // 发动机减速器状态
                cond.setByteLenght(1);
                break;
            case "1792": // 发动机减速器状态-8 缸
                cond.setByteLenght(1);
                break;
            case "1793": // 发动机减速器状态-6 缸
                cond.setByteLenght(1);
                break;
            case "1794": // 发动机减速器状态4 缸
                cond.setByteLenght(1);
                break;
            case "1795": // 发动机减速器状态-3 缸
                cond.setByteLenght(1);
                break;
            case "1796": // 发动机减速器状态-2 缸
                cond.setByteLenght(1);
                break;
            case "17a0": // 发动机缓速器百分比
                cond.setByteLenght(1);
                break;
            case "17c0": // 变速器机油位置
                cond.setByteLenght(1);
                break;
            case "1810": // 喷油器计量轨#2压力
                cond.setByteLenght(1);
                break;
            case "1861": // 轮速传感器状态-车轮传感器 ABS 车桥: 1 左
                cond.setByteLenght(1);
                break;
            case "1862": // 轮速传感器状态-车轮传感器 ABS 车桥: 2 左
                cond.setByteLenght(1);
                break;
            case "1863": // 轮速传感器状态-车轮传感器 ABS 车桥: 3 左
                cond.setByteLenght(1);
                break;
            case "1864": // 轮速传感器状态-车轮传感器 ABS 车桥: 4 左
                cond.setByteLenght(1);
                break;
            case "1865": // 轮速传感器状态-车轮传感器 ABS 车桥: 1 右
                cond.setByteLenght(1);
                break;
            case "1866": // 轮速传感器状态-车轮传感器 ABS 车桥: 2 右
                cond.setByteLenght(1);
                break;
            case "1867": // 轮速传感器状态-车轮传感器 ABS 车桥: 3 右
                cond.setByteLenght(1);
                break;
            case "1868": // 轮速传感器状态-车轮传感器 ABS 车桥: 4 右
                cond.setByteLenght(1);
                break;
            case "18b1": // 轮胎压力控制系统通道功能模式-#1 转向桥通道模式
                cond.setByteLenght(1);
                break;
            case "18b2": // 轮胎压力控制系统通道功能模式-#2 驱动桥通道模式
                cond.setByteLenght(1);
                break;
            case "18b3": // 轮胎压力控制系统通道功能模式-#2 拖车/随动桥通道模式
                cond.setByteLenght(1);
                break;
            case "18c1": // 轮胎压力控制系统电磁阀状态-#1 PCU 转向桥电磁阀状态
                cond.setByteLenght(1);
                break;
            case "18c2": // 轮胎压力控制系统电磁阀状态-#1 PCU 驱动桥电磁阀状态
                cond.setByteLenght(1);
                break;
            case "18c3": // 轮胎压力控制系统电磁阀状态-#2 PCU 拖车，随动桥或推动电磁阀状态
                cond.setByteLenght(1);
                break;
            case "18c4": // 轮胎压力控制系统电磁阀状态-#2 PCU 供给电磁阀状态
                cond.setByteLenght(1);
                break;
            case "18c5": // 轮胎压力控制系统电磁阀状态-#2 PCU 控制电磁阀状态
                cond.setByteLenght(1);
                break;
            case "18c6": // 轮胎压力控制系统电磁阀状态-#2 PCU 放气电磁阀状态
                cond.setByteLenght(1);
                break;
            case "1961": // 取力器啮合控制状态-PTO #2 啮合控制开关状态
                cond.setByteLenght(1);
                break;
            case "1962": // 取力器啮合控制状态-PTO #1 啮合控制开关状态
                cond.setByteLenght(1);
                break;
            case "1963": // 取力器啮合控制状态-PTO #2 啮合执行器状态
                cond.setByteLenght(1);
                break;
            case "1964": // 取力器啮合控制状态-PTO #1 啮合执行器状态
                cond.setByteLenght(1);
                break;
            case "1971": // 自动牵引力控制（ATC）控制状态-自动牵引力控制（ATC）回转信号探测
                cond.setByteLenght(1);
                break;
            case "1972": // 自动牵引力控制（ATC）控制状态-发动机控制
                cond.setByteLenght(1);
                break;
            case "1973": // 自动牵引力控制（ATC）控制状态-制动控制
                cond.setByteLenght(1);
                break;
            case "1974": // 自动牵引力控制（ATC）控制状态-状态灯
                cond.setByteLenght(1);
                break;
            case "1975": // 自动牵引力控制（ATC）控制状态-深雪地/泥浆功能开关
                cond.setByteLenght(1);
                break;
            case "19a1": // 辅助输入和输出状态 #2-辅助输入 #8
                cond.setByteLenght(1);
                break;
            case "19a2": // 辅助输入和输出状态 #2-辅助输入 #7
                cond.setByteLenght(1);
                break;
            case "19a3": // 辅助输入和输出状态 #2-辅助输入 #6
                cond.setByteLenght(1);
                break;
            case "19a4": // 辅助输入和输出状态 #2辅助输入 #5
                cond.setByteLenght(1);
                break;
            case "19a5": // 辅助输入和输出状态 #2-辅助输出 #8
                cond.setByteLenght(1);
                break;
            case "19a6": // 辅助输入和输出状态 #2-辅助输出 #7
                cond.setByteLenght(1);
                break;
            case "19a7": // 辅助输入和输出状态 #2-辅助输出 #6
                cond.setByteLenght(1);
                break;
            case "19a8": // 辅助输入和输出状态 #2-辅助输出 #5
                cond.setByteLenght(1);
                break;
            case "19b1": // 辅助输入和输出状态 #1-辅助输入 #4
                cond.setByteLenght(1);
                break;
            case "19b2": // 辅助输入和输出状态 #1-辅助输入 #3
                cond.setByteLenght(1);
                break;
            case "19b3": // 辅助输入和输出状态 #1-辅助输入 #2
                cond.setByteLenght(1);
                break;
            case "19b4": // 辅助输入和输出状态 #1-辅助输入 #1
                cond.setByteLenght(1);
                break;
            case "19b5": // 辅助输入和输出状态 #1-辅助输出 #4
                cond.setByteLenght(1);
                break;
            case "19b6": // 辅助输入和输出状态 #1-辅助输出 #3
                cond.setByteLenght(1);
                break;
            case "19b7": // 辅助输入和输出状态 #1-辅助输出 #2
                cond.setByteLenght(1);
                break;
            case "19b8": // 辅助输入和输出状态 #1-辅助输出 #1
                cond.setByteLenght(1);
                break;
            case "1d11": // ABS 控制状态, 拖车-ABS制动控制状态,拖车# 1
                cond.setByteLenght(1);
                break;
            case "1d12": // ABS 控制状态, 拖车-ABS 警告灯, 拖车# 1
                cond.setByteLenght(1);
                break;
            case "1d13": // ABS 控制状态, 拖车-拖车 ABS 控制状态
                cond.setByteLenght(1);
                break;
            case "1d14": // ABS 控制状态, 拖车-牵引车安装拖车 ABS灯
                cond.setByteLenght(1);
                break;
            case "1d15": // ABS 控制状态, 拖车-ABS 制动控制状态, 拖车 # 3
                cond.setByteLenght(1);
                break;
            case "1d16": // ABS 控制状态, 拖车-ABS 警告灯, 拖车 # 3
                cond.setByteLenght(1);
                break;
            case "1d17": // ABS 控制状态, 拖车-ABS 制动控制状态, 拖车 # 2
                cond.setByteLenght(1);
                break;
            case "1d18": // ABS 控制状态, 拖车-ABS 警告灯, 拖车 # 2
                cond.setByteLenght(1);
                break;
            case "1d19": // ABS 控制状态, 拖车-ABS 制动控制状态, 拖车 # 5
                cond.setByteLenght(1);
                break;
            case "1d1a": // ABS 控制状态, 拖车-ABS 警告灯, 拖车 #5
                cond.setByteLenght(1);
                break;
            case "1d1b": // ABS 控制状态, 拖车-ABS 制动控制状态, 拖车 # 4
                cond.setByteLenght(1);
                break;
            case "1d1c": // ABS 控制状态, 拖车-ABS 警告灯, 拖车 # 4
                cond.setByteLenght(1);
                break;
            case "1fb1": // 时钟-秒
                cond.setByteLenght(1);
                break;
            case "1fb2": // 时钟-分
                cond.setByteLenght(1);
                break;
            case "1fb3": // 时钟-时
                cond.setByteLenght(1);
                break;
            case "1fc1": // 日期-天
                cond.setByteLenght(1);
                break;
            case "1fc2": // 日期-月
                cond.setByteLenght(1);
                break;
            case "1fc3": // 日期-年
                cond.setByteLenght(1);
                break;
            case "1fd1": // 飞逝时间-秒
                cond.setByteLenght(1);
                break;
            case "1fd2": // 飞逝时间-分
                cond.setByteLenght(1);
                break;
            case "1fd3": // 飞逝时间-时
                cond.setByteLenght(1);
                break;
            case "1fd4": // 飞逝时间-天
                cond.setByteLenght(1);
                break;
            case "2112": // 二次空气状态指令
                cond.setByteLenght(1);
                break;
            case "2113": // 氧传感器位置
            case "211d": // 氧传感器的位置
                cond.setByteLenght(1);
                break;
            case "022e": // 加速踏板1低怠速开关
                cond.setByteLenght(1);
                break;
            case "0253": // 巡航控制开关
                cond.setByteLenght(1);
                break;
            case "0ba3": // 车辆加速度极限状态
                cond.setByteLenght(1);
                break;
            case "0045": // 双速驱动桥开关
                cond.setByteLenght(1);
                break;
            case "0257": // 巡航控制 设置 开关
                cond.setByteLenght(1);
                break;
            case "03c8": // 发动机怠速提速开关
                cond.setByteLenght(1);
                break;
            case "0255": // 制动开关
                cond.setByteLenght(1);
                break;
            case "059d": // 车速限制状态
                cond.setByteLenght(1);
                break;
            case "0661": // 巡航控制暂停开关
                cond.setByteLenght(1);
                break;
            case "0259": // 巡航控制
                cond.setByteLenght(1);
                break;
            case "03c6": // 发动机测试模式开关 //20151104
                cond.setByteLenght(1);
                break;

            case "0256": // 离合开关
                cond.setByteLenght(1);
                break;
            case "0b9a": // 加速踏板2低怠速开关
                cond.setByteLenght(1);
                break;
            case "025a": // 巡航控制加速开关
                cond.setByteLenght(1);
                break;
            case "04d5": // 发动机关闭超越开关

                cond.setByteLenght(1);
                break;
            case "03d0": // PTO状态
                cond.setByteLenght(1);

                break;

            case "0383": // 发动机扭矩模式
            case "068b": // 发动机启动机模式
                cond.setByteLenght(1);
                break;

            case "022f": // 加速踏板强制降档开关
                cond.setByteLenght(1);
                break;
            case "0046": // 驻车制动开关
                cond.setByteLenght(1);
                break;
            case "0254": // 巡航控制
                cond.setByteLenght(1);
                break;
            case "0258": // 巡航控制滑行(减速)开关
                cond.setByteLenght(1);
                break;
            case "03c7": // 发动机怠速降速开关
                cond.setByteLenght(1);
                break;
            case "020f": // 巡航控制状态
                cond.setByteLenght(1);
                break;
            // s8
            case "2105":// 发动机冷却液温度
                byte[] s_Data = ByteDataUtil.bytesFromBytes(data, index, 1);
                int s = s_Data[0] & 0xFF;
                cond.setConValue(String.valueOf(s));
                cond.setByteLenght(1);
                break;

            case "210e":// 第一缸点火正时提前角(不包括机械提前)
                cond.setByteLenght(1);
                break;
            case "210f":// 进气温度
                byte[] data_0x210f = ByteDataUtil.bytesFromBytes(data, index, 1);
                int data_210f = data_0x210f[0] & 0xFF;
                cond.setConValue(String.valueOf(data_210f));
                cond.setByteLenght(1);
                break;

            case "212d":// EGR开度误差 (实际开度 — 指令开度)/指令开度*100%
                cond.setByteLenght(1);
                break;
            case "2146":// 环境空气温度
                byte[] data_0x2146 = ByteDataUtil.bytesFromBytes(data, index, 1);
                int data_2146 = data_0x2146[0] & 0xFF;
                cond.setConValue(String.valueOf(data_2146));
                cond.setByteLenght(1);
                break;

            case "215c":// 发动机机油温度
                byte[] data_0x215c = ByteDataUtil.bytesFromBytes(data, index, 1);
                int data_215c = data_0x215c[0] & 0xFF;
                cond.setConValue(String.valueOf(data_215c));
                cond.setByteLenght(1);
                break;
            case "2161":// 司机请求的发动机扭矩百分比
                cond.setByteLenght(1);
                break;
            case "2162":// 实际的发动机扭矩百分比
                cond.setByteLenght(1);
                break;
            // 乘用车
            case "0200":// 司机需求的发动机扭矩百分比
                cond.setByteLenght(1);
                break;
            case "0201":// 发动机实际的扭矩百分比
                cond.setByteLenght(1);
                break;
            case "0980":// 发动机请求－扭矩百分比
                cond.setByteLenght(1);
                break;
            case "006e":// 低精度发动机冷却液温度
                cond.setByteLenght(1);
                break;
            case "11e0":// 曲轴箱漏气压力
                cond.setByteLenght(1);
                break;
            case "1650":// 曲轴箱压力
                cond.setByteLenght(1);
                break;
            case "17d0": // 变速器机油位置高/低
                cond.setByteLenght(1);
                break;

            // u16
            case "210a": // 燃油压力计量
                cond.setByteLenght(2);
                break;
            case "210c":// 发动机转速 U16
                byte[] data_0x210c = ByteDataUtil.bytesFromBytes(data, index, 2);
                short data_210c = ByteDataUtil.bytesToShortLittle(data_0x210c);
                cond.setConValue(String.valueOf(data_210c));
                cond.setByteLenght(2);
                break;

            case "2110":// 空气流量传感器的空气流量
                cond.setByteLenght(2);
                break;

            case "211f":// 自发动机起动的时间
                byte[] data_0x211f = ByteDataUtil.bytesFromBytes(data, index, 2);
                short data_211f = ByteDataUtil.bytesToShortLittle(data_0x211f);
                cond.setConValue(String.valueOf(data_211f));
                cond.setByteLenght(2);
                break;

            case "2121":// 在MIL激活状态下行驶的里程
                byte[] data_0x2121 = ByteDataUtil.bytesFromBytes(data, index, 2);
                short data_2121 = ByteDataUtil.bytesToShortLittle(data_0x2121);
                cond.setConValue(String.valueOf(data_2121));
                cond.setByteLenght(2);
                break;

            case "2122":// 相对于歧管真空度的油轨压力
                cond.setByteLenght(2);
                break;
            case "2123":// 相对于大气压力的油轨压力
                cond.setByteLenght(2);
                break;
            case "2131":// 自故障码被清除之后的行驶里程
                cond.setByteLenght(2);
                break;
            case "2142":// 控制模块电压
                cond.setByteLenght(2);
                break;
            case "2144":// 等效比指令
                cond.setByteLenght(2);
                break;
            case "214d":// MIL处于激活状态下的发动机运转时间
                byte[] data_0x214d = ByteDataUtil.bytesFromBytes(data, index, 2);
                short data_214d = ByteDataUtil.bytesToShortLittle(data_0x214d);
                cond.setConValue(String.valueOf(data_214d));
                cond.setByteLenght(2);
                break;

            case "214e":// 自故障码清除之后的时间
                cond.setByteLenght(2);
                break;
            case "2150":// 来自空气流量传感器的最大流量
                cond.setByteLenght(2);
                break;
            case "2153":// 蒸发系统蒸气压力绝对值
                cond.setByteLenght(2);
                break;
            case "2154":// 蒸发系统蒸气压力
                cond.setByteLenght(2);
                break;
            case "2159":// 油轨绝对压力
                cond.setByteLenght(2);
                break;
            case "215e":// 发动机燃油消耗率
                cond.setByteLenght(2);
                break;
            case "2163":// 发动机参考扭矩
                cond.setByteLenght(2);
                break;
            ////////////////////// 乘用车////////////////////////////////////////////////////
            case "006a":// 进气压力
                cond.setByteLenght(2);
                break;
            case "00be":// 转速
                cond.setByteLenght(2);
                break;
            case "005e":// 发动机燃油系统输送压力
                cond.setByteLenght(2);
                break;
            case "0064":// 发动机机油压力
                cond.setByteLenght(2);
                break;
            case "006D":// 发动机冷却液压力
                cond.setByteLenght(2);
                break;
            case "0066":// 发动机增压器压力
                cond.setByteLenght(2);
                break;
            case "00ba":// 动力输出装置的转速
                cond.setByteLenght(2);
                break;
            case "0203":// 发动机的期望运转速度
                cond.setByteLenght(2);
                break;
            case "00a1":// 输入轴转速
                cond.setByteLenght(2);
                break;
            case "059c":// 发动机的实际点火提前角
                cond.setByteLenght(2);
                break;
            case "0bfd":// Distance Traveled while MIL activated
                cond.setByteLenght(2);
                break;
            case "0c47":// Distance Since DTCs Cleared
                cond.setByteLenght(2);
                break;
            case "0c48":// Minutes Run by Engine While MIL activated
                cond.setByteLenght(2);
                break;
            case "0c49":// Time Since DTCs Cleared (协议没有写长度，猜测是U16)
                cond.setByteLenght(2);
                break;
            case "0c4e":// Time Since DTCs Cleared
                cond.setByteLenght(2);
                break;
            case "0c4f":// Time Since DTCs Cleared
                cond.setByteLenght(2);
                break;
            case "10f0":// 供油泵进口压力
                cond.setByteLenght(2);
                break;
            case "1100":// 吸油侧燃油过滤器压力差
                cond.setByteLenght(2);
                break;
            case "1140":// 扩展范围的发动机冷却液压力
                cond.setByteLenght(2);
                break;
            case "14b0": // 转向桥温度
                cond.setByteLenght(2);
                break;
            case "14d0": // 前-后驱动桥温度
                cond.setByteLenght(2);
                break;
            case "14e0": // 后-后驱动桥温度
                cond.setByteLenght(2);
                break;
            case "15a0": // 动力输出装置机油温度
                cond.setByteLenght(2);
                break;
            case "16a0": // 进气压力
                cond.setByteLenght(2);
                break;
            case "1710": // 调速器降速
                cond.setByteLenght(2);
                break;
            case "1780": // 液压减速器机油温度
                cond.setByteLenght(2);
                break;
            case "17e0": // 变速器过滤器压力差
                cond.setByteLenght(2);
                break;
            case "1980": // ECU复位次数
                cond.setByteLenght(2);
                break;
            case "1a00": // 主轴速度
                cond.setByteLenght(2);
                break;
            case "1a10": // 输入轴转速
                cond.setByteLenght(2);
                break;
            case "1a60": // 额定发动机功率
                cond.setByteLenght(2);
                break;
            case "1ba0": // 动力输出速度
                cond.setByteLenght(2);
                break;
            case "1bb0": // 动力输出装置设置速度
                cond.setByteLenght(2);
                break;
            case "1bc0": // 发动机怠速转速
                cond.setByteLenght(2);
                break;
            case "1bd0": // 额定发动机转速
                cond.setByteLenght(2);
                break;
            case "1be0": // 发动机转速
                cond.setByteLenght(2);
                break;
            case "1bf0": // 变速器输出轴转速
                cond.setByteLenght(2);
                break;
            case "1d20": // 轮胎温度(通过序列号决定)
                cond.setByteLenght(2);
                break;
            case "1d30": // 轮胎压力(通过序列号决定)
                cond.setByteLenght(2);
                break;
            case "1d40": // 轮胎压力目标值(通过序列号决定)

                cond.setByteLenght(2);

                break;

            case "9ffe": // 一个故障码 //例如：0x0102 显示应该为：SPN 258
                cond.setByteLenght(2);

                break;

            // s16
            case "2132": // 蒸发系统的蒸气压力
            case "213c": // 催化器温度 B1S1
            case "213d": // 催化器温度 B2S1
            case "213e": // 催化器温度 B1S2
            case "213f": // 催化器温度 B2S2
            case "215d": // 燃油喷射正时
            case "00ae": // 发动机燃油温度 1
            case "00af": // 发动机机油温度 1
            case "00b0": // 发动机增压器机油温度
            case "0034": // 发动机中冷器温度
            case "0065": // 发动机曲轴箱压力
            case "0069": // 低精度发动机进气温度
            case "00ad": // 发动机排气口温度
                cond.setByteLenght(2);
                break;

            // ar U8 2
            case "2114":// 传统0到1V氧传感器输出电压(B1-S1)及与此传感器关联的短时燃油修正(B1-S1)
                cond.setByteLenght(2);
                break;
            case "2115":// 传统0到1V氧传感器输出电压(B1-S2)及与此传感器关联的短时燃油修正(B1-S2)
                cond.setByteLenght(2);
                break;
            case "2116":// 传统0到1V氧传感器输出电压(B2-S1)及与此传感器关联的短时燃油修正(B2-S1)
                cond.setByteLenght(2);
                break;
            case "2117":// 传统0到1V氧传感器输出电压(B2-S2)及与此传感器关联的短时燃油修正(B2-S2)
                cond.setByteLenght(2);
                break;
            case "2118":// 传统0到1V氧传感器输出电压(B3-S1)及与此传感器关联的短时燃油修正(B3-S1)
                cond.setByteLenght(2);
                break;
            case "2119":// 传统0到1V氧传感器输出电压(B3-S2)及与此传感器关联的短时燃油修正(B3-S2)
                cond.setByteLenght(2);
                break;
            case "211a":// 传统0到1V氧传感器输出电压(B4-S1)及与此传感器关联的短时燃油修正(B4-S1)
                cond.setByteLenght(2);
                break;
            case "211b":// 传统0到1V氧传感器输出电压(B4-S2)及与此传感器关联的短时燃油修正(B4-S2)
            case "2155":// 第二个氧传感器的短时燃油修正(Bank 1和Bank 3)
                cond.setByteLenght(2);
                break;
            case "2156":// 第二个氧传感器的长期燃油修正(Bank 1和Bank 3)
                cond.setByteLenght(2);
                break;
            case "2157":// 第二个氧传感器的短时燃油修正(Bank 2和Bank 4)
                cond.setByteLenght(2);
                break;
            case "2158":// 第二个氧传感器的长期燃油修正(Bank 2和Bank 4)
                cond.setByteLenght(2);
                break;
            case "214f":// 等效比的最大值及对应的氧传感器电压
            case "2164":// 发动机扭矩百分比
            case "2124": // 线性或宽带式氧传感器的等效比(lambda)和电压（B1S1）
            case "2125": // 线性或宽带式氧传感器的等效比(lambda)和电压(B1S2)
            case "2126": // 线性或宽带式氧传感器的等效比(lambda)和电压(B2S1)
            case "2127": // 线性或宽带式氧传感器的等效比(lambda)和电压
            case "2128": // 线性或宽带式氧传感器的等效比(lambda)和电压
            case "2129": // 线性或宽带式氧传感器的等效比(lambda)和电压
            case "212a": // 线性或宽带式氧传感器的等效比(lambda)和电压
            case "212b": // 线性或宽带式氧传感器的等效比(lambda)和电压
            case "2134": // 线性或宽带式氧传感器的等效比(lambda)和电流(B1S1)
            case "2135": // 线性或宽带式氧传感器的等效比(lambda)和电流(B1S2)
            case "2136": // 线性或宽带式氧传感器的等效比(lambda)和电流(B2S1)
            case "2137": // 线性或宽带式氧传感器的等效比(lambda)和电流
            case "2138": // 线性或宽带式氧传感器的等效比(lambda)和电流
            case "2139": // 线性或宽带式氧传感器的等效比(lambda)和电流
            case "213a": // 线性或宽带式氧传感器的等效比(lambda)和电流
            case "213b": // 线性或宽带式氧传感器的等效比(lambda)和电流
                log.info("未解析的工况数据：" + pid);
                cond.setByteLenght(4); // 这里会出错，按照4个字节处理,走到这里说明有问题了
                break;
        }
        return cond;
    }
}
