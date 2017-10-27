package com.share.service;


import com.share.controller.LoginController;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import sun.text.normalizer.Trie;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 敏感词过滤服务   前缀树
 */
@Service
public class SensitiveService implements InitializingBean{

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private TrieNode rootNode = new TrieNode();
    private String DEFAULT_REPLACEMENT = "***";
    @Override
    public void afterPropertiesSet() throws Exception {

        rootNode = new TrieNode();

        InputStreamReader reader = null;
        try{
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWord.txt");
            reader = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String lineText;
            while((lineText = bufferedReader.readLine()) != null){
                lineText = lineText.trim();
                addWord(lineText);
            }

        }catch (Exception e){
            logger.error("敏感词文本读取失败" + e.getMessage());
        }finally {
            try{
                reader.close();
            }catch (Exception e){
                logger.error("关闭reader失败" + e.getMessage());
            }
        }
    }

    /**
     * create the sensitive TrieNode
     * @param lineText
     */
    private void addWord(String lineText) {

        TrieNode tempNode = rootNode;

        for(int i = 0; i < lineText.length(); i++){  //一行为一个敏感词
            Character c = lineText.charAt(i);

            TrieNode node = tempNode.getSubNode(c);
            if(node == null){
                node = new TrieNode();
                tempNode.addSubNode(c,node);
            }

            tempNode = node;

            if(i == lineText.length() - 1){  //敏感词结束要设置结束标志
                tempNode.setKeywordEnd(true);
            }
        }

    }

    private class TrieNode{
        private boolean end = false;

        private Map<Character, TrieNode> subNodes = new HashMap<>();

        void addSubNode(Character key,TrieNode node){
            subNodes.put(key,node);
        }

        TrieNode getSubNode(Character key){
            return subNodes.get(key);
        }

        boolean isKeywordEnd(){
            return end;
        }

        public void setKeywordEnd(boolean end){
            this.end = end;
        }
    }

    public String filter(String text){
        if(StringUtils.isBlank(text)){  //如果文本为空  不做处理
            return text;
        }

        String replacement = DEFAULT_REPLACEMENT;
        StringBuilder result = new StringBuilder();

        TrieNode tempNode = rootNode;
        int begin = 0;
        int position = 0;


        while(position < text.length()){
            Character c = text.charAt(position);

            tempNode = tempNode.getSubNode(c);
            if(tempNode == null){
                //非敏感词
                result.append(c);
                position = begin + 1;
                begin = position;
                tempNode = rootNode;
            }else if(tempNode.isKeywordEnd()){
                //敏感词
                result.append(replacement);
                position = position + 1;
                begin = position;
                tempNode = rootNode;
            }else{
                ++position;
            }
        }

        result.append(text.substring(begin));
        return result.toString();
    }

    public static void main(String[] argv){
        SensitiveService sensitiveService = new SensitiveService();
        sensitiveService.addWord("色情");
        System.out.println(sensitiveService.filter("你好色情"));
    }
}
