package com.template.common.collect;

import com.template.common.mail.MailService;
import com.template.common.model.batch.BatchConstants;
import com.template.common.model.batch.BatchLog;
import com.template.common.model.email.Email;
import com.template.repository.batch.BatchLogDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import static com.template.common.util.StringUtil.makeStackTrace;
import static com.template.common.util.TimeUtil.getNowDateToString;
import static com.template.common.util.TimeUtil.getNowDateToString;

public abstract class AbstractBaseCollect<T> {
	private final Logger logger = LoggerFactory.getLogger(AbstractBaseCollect.class);
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	public BatchLogDAO batchLogDAO;
	private MailService mailService;

	public BatchLog batchLog;

	private String BATCH = "BATCH JOB_NAME : ";
	private String jobName = "BASE_JOB_NAME";
	private int commitCount = 0;
	private boolean setAfter = false;
	private boolean bulk = false;
	private boolean sendEmail = true;
	private String batchMessage = "";


	protected AbstractBaseCollect(BatchLogDAO batchLogDAO, MailService mailService) {
		this.batchLogDAO = batchLogDAO;
		this.mailService = mailService;
	}

	protected List<T> read() throws IOException {
		return null;
	}

	@SuppressWarnings (value="unchecked")
	private List<T> doRead() throws  IOException, BatchException {
		List<T> batchData = null;
		int readCount = 0;
		try {
			batchData = read();
			readCount = batchData == null ? 0 : batchData.size();
		} catch (Exception e) {
			throw new BatchException(BatchErrorEnum.READ);
		}
		logger.info(BATCH+this.jobName+" - Read Count : {}", readCount);
		setBatchLogMessage(batchData);
		this.batchLog.setMessage(batchMessage);
		this.batchLog.setStatus(BatchConstants.BATCH_STATUS_READ + (readCount == 0 ? "_ZERO" : ""));
		this.batchLog.setEndTime(getNowDateToString());
		this.batchLog.setReadCount(readCount);
		batchLogDAO.updateBatchLog(this.batchLog);
		return batchData;
	}

	protected List<T> process(List<T> list) throws IOException {
		return list;
	}

	@SuppressWarnings (value="unchecked")
	private void doProcess(List<T> list) throws IOException, BatchException {
		if(list != null) {
			process(list);
		}
	}

	protected int write(T data) throws IOException {
		return 0;
	}

	protected int bulkWrite(List<T> data) throws IOException {
		return 0;
	}

	@SuppressWarnings (value="unchecked")
	private void doWrite(List<T> batchData) throws IOException, BatchException {
		if(batchData != null && !batchData.isEmpty()) {
			int writeCount = 0;
			try {
				if(bulk) {
					writeCount = bulkWrite(batchData);
				} else {
					for(T data : batchData) {
						writeCount += write(data);
					}
				}
			} catch (Exception e) {
				logger.info(e.getMessage());
				throw new BatchException(BatchErrorEnum.WRITE);
			}
			// WRITE COUNT LOG
			logger.info(BATCH+this.jobName+" - Write Count : {}", writeCount);
			setBatchLogMessage(batchData);
			this.batchLog.setMessage(batchMessage);
			this.batchLog.setStatus(BatchConstants.BATCH_STATUS_WRITE);
			this.batchLog.setEndTime(getNowDateToString());
			this.batchLog.setWriteCount(writeCount);
			batchLogDAO.updateBatchLog(this.batchLog);
		}
	}

	protected boolean setAfter() throws IOException {
		return setAfter;
	}

	/**
	 * setRank() 가 True여야 정상처리, False일때 BatchException처리
	 */
	private void setAfterHandle() throws IOException, BatchException {
		if(setAfter) {
			logger.info(BATCH+this.jobName+" - Procedure Call");
			offSetAfter();

			// SET RANK LOG
			logger.info(BATCH+this.jobName+" - dup_idx : {}",getNowDateToString("HHmm"));
			this.batchLog.setStatus(BatchConstants.BATCH_STATUS_RANK);
			this.batchLog.setEndTime(getNowDateToString());
			batchLogDAO.updateBatchLog(this.batchLog);

			if(!setAfter()) {
				throw new BatchException(BatchErrorEnum.CALLPROCEDURE);
			}
		}
	}

	public void doCollectTask() {
		baseCollectTask();
	}

	private void baseCollectTask() {
		// BATCH START
		this.batchLog = new BatchLog(this.jobName);
		batchLogDAO.insertBatchLog(this.batchLog);
		logger.info(BATCH+this.jobName+" - Start");

		List<T> batchData = null;
		try {
			// BATCH DATA READ
			batchData = doRead();

			// BATCH DATA PROCESS
			doProcess(batchData);

			// BATCH DATA WRITE
			doWrite(batchData);

			// setRank Procedure Call
			setAfterHandle();

			this.commitCount += 1;
		} catch (IOException e) {
			// BATCH FAIL
			setBatchLogMessage(batchData);
			batchData = null;
			logger.error("ERROR : ",e.getMessage(),	e);e.printStackTrace();
			this.batchLog.setMessage(batchMessage);
			this.batchLog.setStatus(BatchConstants.BATCH_STATUS_FAIL);
			batchLogDAO.updateBatchLog(this.batchLog);
			sendAlertByEmail(e);
			return;
		} catch (BatchException e) {
			// BATCH FAIL
			setBatchLogMessage(batchData);
			batchData = null;
			logger.error("ERROR : ",e.getMessage(),	e);e.printStackTrace();
			this.batchLog.setMessage(batchMessage);
			this.batchLog.setStatus(e.getMessage());
			batchLogDAO.updateBatchLog(this.batchLog);
			sendAlertByEmail(e);
			return;
		}
		// BATCH COMPLETE
		logger.info(BATCH+this.jobName+" - COMPLETE!!!! Commit Count : 1");
		setBatchLogMessage(batchData);
		batchData = null;
		this.batchLog.setMessage(batchMessage);
		this.batchLog.setStatus(batchLog.getStatus()+"_"+BatchConstants.BATCH_STATUS_COMPLETE);
		this.batchLog.setEndTime(getNowDateToString());
		this.batchLog.setCommitCount(this.commitCount);
		batchLogDAO.updateBatchLog(this.batchLog);
	}












	public void sendAlertByEmail(Throwable t) {
		if(sendEmail) {
			Email email = new Email();
			email.setReceiver(BatchConstants.ALERT_EMAIL);
			String title = this.batchLog.getStatus();
			email.setSubject(title);
			String content = "<br/><br/>"+t.toString() + "<br/><br/>\r\n" + makeStackTrace(t);
			email.setContent(title+content);

			logger.info("url.." + title+content);
			mailService.sendAlertEmail(email);
		}
	}

	public void sendAlertByEmail(String inTitle, String inContent) {
		if(sendEmail) {
			Email email = new Email();
			email.setReceiver(BatchConstants.ALERT_EMAIL);
			String title = inTitle;
			email.setSubject(title);
			String content = "<br/><br/>"+inContent;
			email.setContent(title+content);

			logger.info("url.." + title+content);
			mailService.sendAlertEmail(email);
		}
	}



	protected void setBatchLogMessage(List<T> dataList) {
		this.batchMessage = "READ_COUNT : " + batchLog.getReadCount() + ", WRITE_COUNT : " + batchLog.getWriteCount();
	}

	protected void setBatchMessage(String msg) {
		this.batchMessage = msg;
	}

	public void onSetAfter() {
		this.setAfter = true;
	}

	public void offSetAfter() {
		this.setAfter = false;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobName() {
		return jobName;
	}

	protected void setBulk(boolean bulk) {
		this.bulk = bulk;
	}

	protected int getReadCount() {
		return this.batchLog.getReadCount();
	}

	public void setSendEmail(boolean sendEmail) {
		this.sendEmail = sendEmail;
	}

}
