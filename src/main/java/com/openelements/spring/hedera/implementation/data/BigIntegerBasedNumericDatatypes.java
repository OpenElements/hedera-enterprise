package com.openelements.spring.hedera.implementation.data;

import com.hedera.hashgraph.sdk.ContractFunctionParameters;
import java.math.BigInteger;
import java.util.function.BiConsumer;

public enum BigIntegerBasedNumericDatatypes implements ParamSupplier<BigInteger> {

    INT72("int72", (v, params) -> params.addInt72(v), new BigInteger("-2361183241434822606848"), new BigInteger("2361183241434822606847")),
    INT80("int80", (v, params) -> params.addInt80(v), new BigInteger("-1208925819614629174706176"), new BigInteger("1208925819614629174706175")),
    INT88("int88", (v, params) -> params.addInt88(v), new BigInteger("-309485009821345068724781056"), new BigInteger("309485009821345068724781055")),
    INT96("int96", (v, params) -> params.addInt96(v), new BigInteger("-79228162514264337593543950336"), new BigInteger("79228162514264337593543950335")),
    INT104("int104", (v, params) -> params.addInt104(v), new BigInteger("-20282409603651670423947251286016"), new BigInteger("20282409603651670423947251286015")),
    INT112("int112", (v, params) -> params.addInt112(v), new BigInteger("-5192296858534827628530496329220096"), new BigInteger("5192296858534827628530496329220095")),
    INT120("int120", (v, params) -> params.addInt120(v), new BigInteger("-1329227995784915872903807060280344576"), new BigInteger("1329227995784915872903807060280344575")),
    INT128("int128", (v, params) -> params.addInt128(v), new BigInteger("-340282366920938463463374607431768211456"), new BigInteger("340282366920938463463374607431768211455")),
    INT136("int136", (v, params) -> params.addInt136(v), new BigInteger("-87112285931760246646623899502532662132736"), new BigInteger("87112285931760246646623899502532662132735")),
    INT144("int144", (v, params) -> params.addInt144(v), new BigInteger("-22300745198530623141535718272648361505980416"), new BigInteger("22300745198530623141535718272648361505980415")),
    INT152("int152", (v, params) -> params.addInt152(v), new BigInteger("-5708990770823839524233143877797980545530986496"), new BigInteger("5708990770823839524233143877797980545530986495")),
    INT160("int160", (v, params) -> params.addInt160(v), new BigInteger("-1461501637330902918203684832716283019655932542976"), new BigInteger("1461501637330902918203684832716283019655932542975")),
    INT168("int168", (v, params) -> params.addInt168(v), new BigInteger("-374144419156711147060143317175368453031918731001856"), new BigInteger("374144419156711147060143317175368453031918731001855")),
    INT176("int176", (v, params) -> params.addInt176(v), new BigInteger("-95780971304118053647396689196894323976171195136475136"), new BigInteger("95780971304118053647396689196894323976171195136475135")),
    INT184("int184", (v, params) -> params.addInt184(v), new BigInteger("-24519928653854221733733552434404946937899825954937634816"), new BigInteger("24519928653854221733733552434404946937899825954937634815")),
    INT192("int192", (v, params) -> params.addInt192(v), new BigInteger("-6277101735386680763835789423207666416102355444464034512896"), new BigInteger("6277101735386680763835789423207666416102355444464034512895")),
    INT200("int200", (v, params) -> params.addInt200(v), new BigInteger("-1606938044258990275541962092341162602522202993782792835301376"), new BigInteger("1606938044258990275541962092341162602522202993782792835301375")),
    INT208("int208", (v, params) -> params.addInt208(v), new BigInteger("-411376139330301510538742295639337626245683966408394965837152256"), new BigInteger("411376139330301510538742295639337626245683966408394965837152255")),
    INT216("int216", (v, params) -> params.addInt216(v), new BigInteger("-105312291668557186697918027683670432318895095400549111254310977536"), new BigInteger("105312291668557186697918027683670432318895095400549111254310977535")),
    INT224("int224", (v, params) -> params.addInt224(v), new BigInteger("-26959946667150639794667015087019630673637144422540572481103610249216"), new BigInteger("26959946667150639794667015087019630673637144422540572481103610249215")),
    INT232("int232", (v, params) -> params.addInt232(v), new BigInteger("-6901746346790563787434755862277025452451108972170386555162524223799296"), new BigInteger("6901746346790563787434755862277025452451108972170386555162524223799295")),
    INT240("int240", (v, params) -> params.addInt240(v), new BigInteger("-1766847064778384329583297500742918515827483896875618958121606201292619776"), new BigInteger("1766847064778384329583297500742918515827483896875618958121606201292619775")),
    INT248("int248", (v, params) -> params.addInt248(v), new BigInteger("-452312848583266388373324160190187140051835877600158453279131187530910662656"), new BigInteger("452312848583266388373324160190187140051835877600158453279131187530910662655")),
    INT256("int256", (v, params) -> params.addInt256(v), new BigInteger("-115792089237316195423570985008687907853269984665640564039457584007913129639936"), new BigInteger("115792089237316195423570985008687907853269984665640564039457584007913129639935")),
    UINT72("uint72", (v, params) -> params.addUint72(v), BigInteger.ZERO, new BigInteger("4722366482869645213695")),
    UINT80("uint80", (v, params) -> params.addUint80(v), BigInteger.ZERO, new BigInteger("1208925819614629174706175")),
    UINT88("uint88", (v, params) -> params.addUint88(v), BigInteger.ZERO, new BigInteger("309485009821345068724781055")),
    UINT96("uint96", (v, params) -> params.addUint96(v), BigInteger.ZERO, new BigInteger("79228162514264337593543950335")),
    UINT104("uint104", (v, params) -> params.addUint104(v), BigInteger.ZERO, new BigInteger("20282409603651670423947251286015")),
    UINT112("uint112", (v, params) -> params.addUint112(v), BigInteger.ZERO, new BigInteger("5192296858534827628530496329220095")),
    UINT120("uint120", (v, params) -> params.addUint120(v), BigInteger.ZERO, new BigInteger("1329227995784915872903807060280344575")),
    UINT128("uint128", (v, params) -> params.addUint128(v), BigInteger.ZERO, new BigInteger("340282366920938463463374607431768211455")),
    UINT136("uint136", (v, params) -> params.addUint136(v), BigInteger.ZERO, new BigInteger("87112285931760246646623899502532662132735")),
    UINT144("uint144", (v, params) -> params.addUint144(v), BigInteger.ZERO, new BigInteger("22300745198530623141535718272648361505980415")),
    UINT152("uint152", (v, params) -> params.addUint152(v), BigInteger.ZERO, new BigInteger("5708990770823839524233143877797980545530986495")),
    UINT160("uint160", (v, params) -> params.addUint160(v), BigInteger.ZERO, new BigInteger("1461501637330902918203684832716283019655932542975")),
    UINT168("uint168", (v, params) -> params.addUint168(v), BigInteger.ZERO, new BigInteger("374144419156711147060143317175368453031918731001855")),
    UINT176("uint176", (v, params) -> params.addUint176(v), BigInteger.ZERO, new BigInteger("95780971304118053647396689196894323976171195136475135")),
    UINT184("uint184", (v, params) -> params.addUint184(v), BigInteger.ZERO, new BigInteger("24519928653854221733733552434404946937899825954937634815")),
    UINT192("uint192", (v, params) -> params.addUint192(v), BigInteger.ZERO, new BigInteger("6277101735386680763835789423207666416102355444464034512895")),
    UINT200("uint200", (v, params) -> params.addUint200(v), BigInteger.ZERO, new BigInteger("1606938044258990275541962092341162602522202993782792835301375")),
    UINT208("uint208", (v, params) -> params.addUint208(v), BigInteger.ZERO, new BigInteger("411376139330301510538742295639337626245683966408394965837152255")),
    UINT216("uint216", (v, params) -> params.addUint216(v), BigInteger.ZERO, new BigInteger("105312291668557186697918027683670432318895095400549111254310977535")),
    UINT224("uint224", (v, params) -> params.addUint224(v), BigInteger.ZERO, new BigInteger("26959946667150639794667015087019630673637144422540572481103610249215")),
    UINT232("uint232", (v, params) -> params.addUint232(v), BigInteger.ZERO, new BigInteger("6901746346790563787434755862277025452451108972170386555162524223799295")),
    UINT240("uint240", (v, params) -> params.addUint240(v), BigInteger.ZERO, new BigInteger("1766847064778384329583297500742918515827483896875618958121606201292619775")),
    UINT248("uint248", (v, params) -> params.addUint248(v), BigInteger.ZERO, new BigInteger("452312848583266388373324160190187140051835877600158453279131187530910662655")),
    UINT256("uint256", (v, params) -> params.addUint256(v), BigInteger.ZERO, new BigInteger("115792089237316195423570985008687907853269984665640564039457584007913129639935"));
    private final BiConsumer<BigInteger, ContractFunctionParameters> addParam;

    private final BigInteger minValue;

    private final BigInteger maxValue;

    private final String nativeType;

    BigIntegerBasedNumericDatatypes(final String nativeType, BiConsumer<BigInteger, ContractFunctionParameters> addParam, BigInteger minValue, BigInteger maxValue) {
       this.nativeType = nativeType;
        this.addParam = addParam;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    public void addParam(BigInteger value, ContractFunctionParameters params) {
        if(value.compareTo(minValue) < 0 || value.compareTo(maxValue) > 0) {
            throw new IllegalArgumentException("value out of range for type '" + this + "': " + value);
        }
        addParam.accept(value, params);
    }

    @Override
    public boolean isValidParam(BigInteger value) {
        if(value == null) {
            return false;
        }
        return value.compareTo(minValue) >= 0 && value.compareTo(maxValue) <= 0;
    }

    @Override
    public String getNativeType() {
        return nativeType;
    }


}
