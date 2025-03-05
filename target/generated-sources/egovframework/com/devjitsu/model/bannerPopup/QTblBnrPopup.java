package egovframework.com.devjitsu.model.bannerPopup;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QTblBnrPopup is a Querydsl query type for TblBnrPopup
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTblBnrPopup extends EntityPathBase<TblBnrPopup> {

    private static final long serialVersionUID = 802570091L;

    public static final QTblBnrPopup tblBnrPopup = new QTblBnrPopup("tblBnrPopup");

    public final StringPath actvtnYn = createString("actvtnYn");

    public final NumberPath<Long> atchFileSn = createNumber("atchFileSn", Long.class);

    public final StringPath bnrCn = createString("bnrCn");

    public final StringPath bnrPopupCn = createString("bnrPopupCn");

    public final StringPath bnrPopupFrm = createString("bnrPopupFrm");

    public final StringPath bnrPopupKnd = createString("bnrPopupKnd");

    public final NumberPath<Long> bnrPopupSn = createNumber("bnrPopupSn", Long.class);

    public final StringPath bnrPopupTtl = createString("bnrPopupTtl");

    public final StringPath bnrPopupUrlAddr = createString("bnrPopupUrlAddr");

    public final NumberPath<Long> creatrSn = createNumber("creatrSn", Long.class);

    public final DateTimePath<java.time.LocalDateTime> frstCrtDt = createDateTime("frstCrtDt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> mdfcnDt = createDateTime("mdfcnDt", java.time.LocalDateTime.class);

    public final NumberPath<Long> mdfrSn = createNumber("mdfrSn", Long.class);

    public final StringPath npagYn = createString("npagYn");

    public final DateTimePath<java.time.LocalDateTime> popupBgngDt = createDateTime("popupBgngDt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> popupEndDt = createDateTime("popupEndDt", java.time.LocalDateTime.class);

    public final NumberPath<Integer> popupPstnUpend = createNumber("popupPstnUpend", Integer.class);

    public final NumberPath<Integer> popupPstnWdth = createNumber("popupPstnWdth", Integer.class);

    public final NumberPath<Integer> popupVrtcSz = createNumber("popupVrtcSz", Integer.class);

    public final NumberPath<Integer> popupWdthSz = createNumber("popupWdthSz", Integer.class);

    public final StringPath useYn = createString("useYn");

    public final StringPath youtubeYn = createString("youtubeYn");

    public QTblBnrPopup(String variable) {
        super(TblBnrPopup.class, forVariable(variable));
    }

    public QTblBnrPopup(Path<? extends TblBnrPopup> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTblBnrPopup(PathMetadata metadata) {
        super(TblBnrPopup.class, metadata);
    }

}

