package egovframework.com.devjitsu.model.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QTblRelInst is a Querydsl query type for TblRelInst
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTblRelInst extends EntityPathBase<TblRelInst> {

    private static final long serialVersionUID = -1524134119L;

    public static final QTblRelInst tblRelInst = new QTblRelInst("tblRelInst");

    public final StringPath actno = createString("actno");

    public final StringPath actvtnYn = createString("actvtnYn");

    public final StringPath bankNm = createString("bankNm");

    public final StringPath brno = createString("brno");

    public final StringPath bzentyEmlAddr = createString("bzentyEmlAddr");

    public final StringPath bzentyExpln = createString("bzentyExpln");

    public final StringPath clsf = createString("clsf");

    public final NumberPath<Long> cptl = createNumber("cptl", Long.class);

    public final NumberPath<Long> creatrSn = createNumber("creatrSn", Long.class);

    public final StringPath crno = createString("crno");

    public final StringPath dlngSeNm = createString("dlngSeNm");

    public final StringPath dpstrNm = createString("dpstrNm");

    public final NumberPath<Long> empCnt = createNumber("empCnt", Long.class);

    public final StringPath empJoinYn = createString("empJoinYn");

    public final StringPath entAddr = createString("entAddr");

    public final StringPath entDaddr = createString("entDaddr");

    public final StringPath entTelno = createString("entTelno");

    public final StringPath euseCnpl = createString("euseCnpl");

    public final StringPath fctryAddr = createString("fctryAddr");

    public final StringPath fctryDaaddr = createString("fctryDaaddr");

    public final StringPath fctryZip = createString("fctryZip");

    public final StringPath fndnYmd = createString("fndnYmd");

    public final StringPath fnstCd = createString("fnstCd");

    public final DateTimePath<java.time.LocalDateTime> frstCrtDt = createDateTime("frstCrtDt", java.time.LocalDateTime.class);

    public final StringPath hmpgAddr = createString("hmpgAddr");

    public final StringPath indvCorpSeNm = createString("indvCorpSeNm");

    public final StringPath mainHstry = createString("mainHstry");

    public final StringPath mainPrdtNm = createString("mainPrdtNm");

    public final StringPath mblTelno = createString("mblTelno");

    public final DateTimePath<java.time.LocalDateTime> mdfcnDt = createDateTime("mdfcnDt", java.time.LocalDateTime.class);

    public final NumberPath<Long> mdfrSn = createNumber("mdfrSn", Long.class);

    public final StringPath relInstNm = createString("relInstNm");

    public final StringPath relInstNmEng = createString("relInstNmEng");

    public final NumberPath<Long> relInstSn = createNumber("relInstSn", Long.class);

    public final StringPath rlsBgngYmd = createString("rlsBgngYmd");

    public final StringPath rlsEndYmd = createString("rlsEndYmd");

    public final StringPath rlsYn = createString("rlsYn");

    public final StringPath rmrkCn = createString("rmrkCn");

    public final StringPath rprsFxno = createString("rprsFxno");

    public final StringPath rprsItemNm = createString("rprsItemNm");

    public final StringPath rprsvAddr = createString("rprsvAddr");

    public final StringPath rprsvDaaddr = createString("rprsvDaaddr");

    public final StringPath rprsvZip = createString("rprsvZip");

    public final StringPath rpsvNm = createString("rpsvNm");

    public final StringPath rrno = createString("rrno");

    public final StringPath rschDvlpItemNm = createString("rschDvlpItemNm");

    public final StringPath tpbiz = createString("tpbiz");

    public final StringPath zip = createString("zip");

    public QTblRelInst(String variable) {
        super(TblRelInst.class, forVariable(variable));
    }

    public QTblRelInst(Path<? extends TblRelInst> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTblRelInst(PathMetadata metadata) {
        super(TblRelInst.class, metadata);
    }

}

