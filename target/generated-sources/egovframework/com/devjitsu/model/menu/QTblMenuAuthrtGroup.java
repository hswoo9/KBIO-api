package egovframework.com.devjitsu.model.menu;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QTblMenuAuthrtGroup is a Querydsl query type for TblMenuAuthrtGroup
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTblMenuAuthrtGroup extends EntityPathBase<TblMenuAuthrtGroup> {

    private static final long serialVersionUID = 1962292316L;

    public static final QTblMenuAuthrtGroup tblMenuAuthrtGroup = new QTblMenuAuthrtGroup("tblMenuAuthrtGroup");

    public final StringPath actvtnYn = createString("actvtnYn");

    public final StringPath authrtGroupNm = createString("authrtGroupNm");

    public final NumberPath<Long> authrtGroupSn = createNumber("authrtGroupSn", Long.class);

    public final StringPath authrtType = createString("authrtType");

    public final NumberPath<Long> creatrSn = createNumber("creatrSn", Long.class);

    public final StringPath delAuthrt = createString("delAuthrt");

    public final DateTimePath<java.time.LocalDateTime> frstCrtDt = createDateTime("frstCrtDt", java.time.LocalDateTime.class);

    public final StringPath inqAuthrt = createString("inqAuthrt");

    public final StringPath mdfcnAuthrt = createString("mdfcnAuthrt");

    public final DateTimePath<java.time.LocalDateTime> mdfcnDt = createDateTime("mdfcnDt", java.time.LocalDateTime.class);

    public final NumberPath<Long> mdfrSn = createNumber("mdfrSn", Long.class);

    public final StringPath wrtAuthrt = createString("wrtAuthrt");

    public QTblMenuAuthrtGroup(String variable) {
        super(TblMenuAuthrtGroup.class, forVariable(variable));
    }

    public QTblMenuAuthrtGroup(Path<? extends TblMenuAuthrtGroup> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTblMenuAuthrtGroup(PathMetadata metadata) {
        super(TblMenuAuthrtGroup.class, metadata);
    }

}

