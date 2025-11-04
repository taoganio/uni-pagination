package io.github.taoganio.unipagination.mybatis;


import jakarta.annotation.Nullable;

/**
 * 执行程序上下文
 */
class ExecutorContext {

    private static final ThreadLocal<Mark> EXECUTOR_CONTEXT = new ThreadLocal<>();

    public static Mark setExecutionMark(Mark mark) {
        EXECUTOR_CONTEXT.set(mark);
        return mark;
    }

    @Nullable
    public static Mark getMark() {
        return EXECUTOR_CONTEXT.get();
    }

    /**
     * 清除标记
     */
    public static void clearMark() {
        EXECUTOR_CONTEXT.remove();
    }

    public static Mark countMark() {
        return setExecutionMark(new Mark(true, false));
    }

    public static Mark pageMark() {
        return setExecutionMark(new Mark(false, true));
    }

    public static class Mark implements AutoCloseable {

        private final boolean countExecution;
        private final boolean pageExecution;

        public Mark() {
            this.countExecution = false;
            this.pageExecution = false;
        }

        public Mark(boolean countExecution, boolean pageExecution) {
            this.countExecution = countExecution;
            this.pageExecution = pageExecution;
        }

        public boolean isCountMark() {
            return countExecution;
        }

        public boolean isPageMark() {
            return pageExecution;
        }

        @Override
        public void close() {
            ExecutorContext.clearMark();
        }
    }
}
