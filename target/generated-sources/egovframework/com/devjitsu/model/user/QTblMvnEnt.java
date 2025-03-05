package egovframework.com.devjitsu.model.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QTblMvnEnt is a Querydsl query type for TblMvnEnt
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTblMvnEnt extends EntityPathBase<TblMvnEnt> {

    private static final long serialVersionUID = -730745108L;

    public static final QTblMvnEnt tblMvnEnt = new QTblMvnEnt("tblMvnEnt");

    public final StringPath actno = createString("actno");

    public final StringPath actvtnYn = createString("actvtnYn");

    public final StringPath bankNm = createString("bankNm");

    public final StringPath brno = createString("brno");

    public final StringPath bzentyEmlAddr = createString("bzentyEmlAddr");

    public final StringPath bzentyExpln = createString("bzentyExpln");

    public final NumberPath<Long> cptl = createNumber("cptl", Long.class);

    public final NumberPath<Long> creatrSn = createNumber("creatrSn", Long.class);

    public final StringPath crno = createString("crno");

    public final StringPath dlngSeNm = createString("dlngSeNm");

    public final StringPath dpstrNm = createString("dpstrNm");

    public final NumberPath<Long> empCnt = createNumber("empCnt", Long.class);

    public final StringPath empJoinYn = createString("empJoinYn");

    public final StringPath entAddr = createString("entAddr");

    public final StringPath entClsf = createString("entClsf");

    public final StringPath entDaddr = createString("entDaddr");

    public final StringPath entTelno = createString("entTelno");

    public final StringPath entTpbiz = createString("entTpbiz");

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

    public final StringPath mvnEntNm = createString("mvnEntNm");

    public final StringPath mvnEntNmEng = createString("mvnEntNmEng");

    public final NumberPath<Long> mvnEntSn = createNumber("mvnEntSn", Long.class);

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

    public final StringPath zip = createString("zip");

    public QTblMvnEnt(String variable) {
        super(TblMvnEnt.class, forVariable(variable));
    }

    public QTblMvnEnt(Path<? extends TblMvnEnt> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTblMvnEnt(PathMetadata metadata) {
        super(TblMvnEnt.class, metadata);
    }

}

