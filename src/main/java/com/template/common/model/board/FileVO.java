package com.template.common.model.board;

public class FileVO {
    private int file_idx;
    private String parentPK;
    private String file_name;
    private String real_name;
    private long file_size;
    
    /**
     * 파일 크기를 정형화하기.
     */
    public String size2String() {
        Integer unit = 1024;
        if (file_size < unit) {
            return String.format("(%d B)", file_size);
        }
        int exp = (int) (Math.log(file_size) / Math.log(unit));

        return String.format("(%.0f %s)", file_size / Math.pow(unit, exp), "KMGTPE".charAt(exp - 1));
    }
    

    public String getParentPK() {
        return parentPK;
    }
    
    public void setParentPK(String parentPK) {
        this.parentPK = parentPK;
    }

    public int getFile_idx() {
        return file_idx;
    }

    public void setFile_idx(int file_idx) {
        this.file_idx = file_idx;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public long getFile_size() {
        return file_size;
    }

    public void setFile_size(long file_size) {
        this.file_size = file_size;
    }
}
